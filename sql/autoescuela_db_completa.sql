-- ====================================================================
--  LUZ VERDE - Base de datos COMPLETA (autoescuela_db)
--  Ejecutar completo, de una sola vez, en MySQL Workbench o phpMyAdmin.
--
--  Como ejecutarlo: NO selecciones ninguna base antes (pantalla de inicio de
--  phpMyAdmin > pestana SQL, o una conexion nueva en Workbench).
--
--  ATENCION: este script BORRA la base autoescuela_db si ya existe y
--  la vuelve a crear desde cero. Se pierden los datos que tenga.
-- ====================================================================

DROP DATABASE IF EXISTS autoescuela_db;
CREATE DATABASE autoescuela_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE autoescuela_db;
SET NAMES utf8mb4;

-- --------------------------------------------------------------------
--  1. USUARIOS (inicio de sesion)
--     Roles validos: ADMINISTRADOR, SECRETARIA, COORDINADOR, ESTUDIANTE,
--                    INSTRUCTOR, ENCARGADO_FLOTA, TESORERA
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.usuarios (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    usuario         VARCHAR(50)  NOT NULL UNIQUE,
    clave_hash      VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    rol             VARCHAR(30)  NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,
    creado_en       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  2. CATEGORIAS de licencia
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.categorias (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  3. ESTUDIANTES (HU-WEB-001)
--     valor_curso: lo que debe pagar en total (el saldo se calcula con pagos)
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.estudiantes (
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id             INT NULL UNIQUE,
    documento              VARCHAR(20)  NOT NULL UNIQUE,
    nombre_completo        VARCHAR(100) NOT NULL,
    categoria_id           INT NOT NULL,
    cedula_archivo         VARCHAR(255) NULL,
    examen_medico_archivo  VARCHAR(255) NOT NULL,
    valor_curso            DECIMAL(12,2) NOT NULL DEFAULT 0,
    estado                 ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    fecha_matricula        DATE NOT NULL,
    CONSTRAINT fk_est_usuario   FOREIGN KEY (usuario_id)   REFERENCES autoescuela_db.usuarios(id)   ON DELETE SET NULL,
    CONSTRAINT fk_est_categoria FOREIGN KEY (categoria_id) REFERENCES autoescuela_db.categorias(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  4. INSTRUCTORES (HU-WEB-004)
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.instructores (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id      INT NULL UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    activo          TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_ins_usuario FOREIGN KEY (usuario_id) REFERENCES autoescuela_db.usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  5. VEHICULOS / FLOTA (HU-WEB-005)
--     Estados: OPERATIVO, INHABILITADO_DOCUMENTACION, INHABILITADO_TALLER, BAJO_REVISION
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.vehiculos (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    placa              VARCHAR(10) NOT NULL UNIQUE,
    tipo               ENUM('Moto','Auto') NOT NULL,
    soat_vence         DATE NOT NULL,
    rev_tecnica_vence  DATE NOT NULL,
    estado             ENUM('OPERATIVO','INHABILITADO_DOCUMENTACION','INHABILITADO_TALLER','BAJO_REVISION')
                       NOT NULL DEFAULT 'OPERATIVO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  6. CLASES practicas agendadas (HU-WEB-002)
--     Estados: AGENDADA, COMPLETADA, CANCELADA_INASISTENCIA, CANCELADA
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.clases (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id  INT NOT NULL,
    instructor_id  INT NOT NULL,
    vehiculo_id    INT NOT NULL,
    fecha          DATE NOT NULL,
    hora           TIME NOT NULL,
    estado         ENUM('AGENDADA','COMPLETADA','CANCELADA_INASISTENCIA','CANCELADA')
                   NOT NULL DEFAULT 'AGENDADA',
    INDEX idx_clase_vehiculo (vehiculo_id, fecha, hora),
    INDEX idx_clase_instructor (instructor_id, fecha, hora),
    CONSTRAINT fk_cla_estudiante FOREIGN KEY (estudiante_id) REFERENCES autoescuela_db.estudiantes(id),
    CONSTRAINT fk_cla_instructor FOREIGN KEY (instructor_id) REFERENCES autoescuela_db.instructores(id),
    CONSTRAINT fk_cla_vehiculo   FOREIGN KEY (vehiculo_id)   REFERENCES autoescuela_db.vehiculos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  7. PAGOS (HU-WEB-006)
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.pagos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id    INT NOT NULL,
    monto            DECIMAL(12,2) NOT NULL,
    metodo           ENUM('Efectivo','Tarjeta','Transferencia') NOT NULL,
    soporte_archivo  VARCHAR(255) NULL,
    registrado_por   INT NULL,
    fecha_pago       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pag_estudiante FOREIGN KEY (estudiante_id) REFERENCES autoescuela_db.estudiantes(id),
    CONSTRAINT fk_pag_usuario    FOREIGN KEY (registrado_por) REFERENCES autoescuela_db.usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  8. COMPETENCIAS y avance de cada estudiante (HU-WEB-004, semaforo)
--     Estados: PENDIENTE (rojo), EN_PROCESO (amarillo), DOMINADO (verde)
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.competencias (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE autoescuela_db.estudiante_competencias (
    estudiante_id   INT NOT NULL,
    competencia_id  INT NOT NULL,
    estado          ENUM('PENDIENTE','EN_PROCESO','DOMINADO') NOT NULL DEFAULT 'PENDIENTE',
    actualizado_en  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (estudiante_id, competencia_id),
    CONSTRAINT fk_ec_estudiante  FOREIGN KEY (estudiante_id)  REFERENCES autoescuela_db.estudiantes(id)  ON DELETE CASCADE,
    CONSTRAINT fk_ec_competencia FOREIGN KEY (competencia_id) REFERENCES autoescuela_db.competencias(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  9. SIMULACRO DE EXAMEN (HU-WEB-003): banco de preguntas y resultados
--     Se aprueba con 80% o mas.
-- --------------------------------------------------------------------
CREATE TABLE autoescuela_db.preguntas_examen (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    enunciado          VARCHAR(255) NOT NULL,
    opcion_a           VARCHAR(150) NOT NULL,
    opcion_b           VARCHAR(150) NOT NULL,
    opcion_c           VARCHAR(150) NOT NULL,
    respuesta_correcta ENUM('A','B','C') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE autoescuela_db.resultados_simulacro (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id INT NOT NULL,
    aciertos      INT NOT NULL,
    total         INT NOT NULL,
    porcentaje    DECIMAL(5,2) NOT NULL,
    aprobado      TINYINT(1) NOT NULL,
    realizado_en  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_res_estudiante FOREIGN KEY (estudiante_id) REFERENCES autoescuela_db.estudiantes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------
--  VISTA: saldo pendiente de cada estudiante
-- --------------------------------------------------------------------
CREATE VIEW autoescuela_db.v_saldo_estudiantes AS
SELECT e.id, e.documento, e.nombre_completo, e.valor_curso,
       COALESCE(SUM(p.monto), 0) AS total_pagado,
       e.valor_curso - COALESCE(SUM(p.monto), 0) AS saldo_pendiente
FROM autoescuela_db.estudiantes e
LEFT JOIN autoescuela_db.pagos p ON p.estudiante_id = e.id
GROUP BY e.id, e.documento, e.nombre_completo, e.valor_curso;

-- ====================================================================
--  DATOS INICIALES
-- ====================================================================

-- Usuarios de prueba (cambia o elimina estas cuentas antes de usar el sistema en serio)
--   admin        / Admin123*   -> todas las funciones
--   secretaria   / Secre123*   -> Registrar Estudiante + Registro de Pagos
--   coordinador  / Coord123*   -> Agendar Clase Practica
--   estudiante   / Estud123*   -> Simulacro de Examen
--   instructor   / Instr123*   -> Ficha Pedagogica
--   flota        / Flota123*   -> Gestion de Flota
--   tesorera     / Tesor123*   -> Registro de Pagos
INSERT INTO autoescuela_db.usuarios (usuario, clave_hash, nombre_completo, rol) VALUES
('admin', 'PBKDF2$210000$UrFXeqwU1Te7TelR12lyAA==$FtvKWh8rUQssViO8NHfXZdSIDro2K85IafWjzWoZ73I=', 'Administrador General', 'ADMINISTRADOR'),
('secretaria', 'PBKDF2$210000$lF6Iiu8KdxMBZ/5nqw8O9g==$TVo7ebHt2vKE5bdrgR2QyeVDs0vWiPdPmxEGPayIxXo=', 'Laura Mendoza', 'SECRETARIA'),
('coordinador', 'PBKDF2$210000$vf1iFpO5zrWDp2tUnzhVIw==$z9w09FwEzNUbYkhVzC9QDD/54UKZG4/WaSx7PHrWMMA=', 'Camilo Rojas', 'COORDINADOR'),
('estudiante', 'PBKDF2$210000$YHB+nN+fBYfF7Qr6BVbESg==$00jY2tAMJ+kgXyLcwkjgpsWEADcJdQvuMVj64MvLbfk=', 'Carlos Ruiz', 'ESTUDIANTE'),
('instructor', 'PBKDF2$210000$x+u/DpOPO6ELgxWSRF41PQ==$cOOSWtyqzoMtEqD2yqjpcQ6Cq7Htx5ffkxxwvZ+DoQU=', 'Pedro Sánchez', 'INSTRUCTOR'),
('flota', 'PBKDF2$210000$3aUReHYVULoP4DGd3ZcMhg==$c1jZt1FktbD/h5lLugEcnK9Hj5IOV3zxT+ItqM/bF8w=', 'Marcela Duarte', 'ENCARGADO_FLOTA'),
('tesorera', 'PBKDF2$210000$JnP7j+Usx1cwc2CrQzWIpQ==$8arYbWLQO3NO0IF9DDhpIFjcWkvZ4RrdfS+QyB1r8zU=', 'Sandra Quintero', 'TESORERA');

INSERT INTO autoescuela_db.categorias (nombre) VALUES
('Auto B1'), ('Auto C1'), ('Moto A1'), ('Moto A2'), ('Combinado');

INSERT INTO autoescuela_db.instructores (usuario_id, nombre_completo) VALUES
((SELECT id FROM autoescuela_db.usuarios WHERE usuario = 'instructor'), 'Pedro Sánchez'),
(NULL, 'Diana Vélez');

INSERT INTO autoescuela_db.estudiantes (usuario_id, documento, nombre_completo, categoria_id,
                         cedula_archivo, examen_medico_archivo, valor_curso, fecha_matricula) VALUES
((SELECT id FROM autoescuela_db.usuarios   WHERE usuario = 'estudiante'), '123456789',  'Carlos Ruiz',
    (SELECT id FROM autoescuela_db.categorias WHERE nombre = 'Auto B1'),
    'cedula_carlos_ruiz.pdf', 'examen_medico_carlos_ruiz.pdf', 600000, '2026-09-01'),
(NULL, '987654321',  'Ana Torres',
    (SELECT id FROM autoescuela_db.categorias WHERE nombre = 'Auto B1'),
    'cedula_ana_torres.pdf', 'examen_medico_ana_torres.pdf', 600000, '2026-09-01'),
(NULL, '1020304050', 'Luis Peña',
    (SELECT id FROM autoescuela_db.categorias WHERE nombre = 'Moto A2'),
    'cedula_luis_pena.pdf', 'examen_medico_luis_pena.pdf', 600000, '2026-09-08'),
(NULL, '1030405060', 'María Gómez',
    (SELECT id FROM autoescuela_db.categorias WHERE nombre = 'Moto A1'),
    'cedula_maria_gomez.pdf', 'examen_medico_maria_gomez.pdf', 600000, '2026-09-15');

INSERT INTO autoescuela_db.vehiculos (placa, tipo, soat_vence, rev_tecnica_vence, estado) VALUES
('ABC-123', 'Moto', '2026-09-10', '2027-01-15', 'OPERATIVO'),
('XYZ-789', 'Auto', '2026-08-01', '2026-08-20', 'OPERATIVO'),
('DEF-456', 'Moto', '2026-06-05', '2026-06-05', 'INHABILITADO_DOCUMENTACION'),
('GHI-321', 'Auto', '2026-12-12', '2026-12-12', 'INHABILITADO_TALLER');

INSERT INTO autoescuela_db.clases (estudiante_id, instructor_id, vehiculo_id, fecha, hora) VALUES
((SELECT id FROM autoescuela_db.estudiantes  WHERE documento = '123456789'),
 (SELECT id FROM autoescuela_db.instructores WHERE nombre_completo = 'Pedro Sánchez'),
 (SELECT id FROM autoescuela_db.vehiculos    WHERE placa = 'ABC-123'), '2026-10-22', '08:00:00'),
((SELECT id FROM autoescuela_db.estudiantes  WHERE documento = '987654321'),
 (SELECT id FROM autoescuela_db.instructores WHERE nombre_completo = 'Diana Vélez'),
 (SELECT id FROM autoescuela_db.vehiculos    WHERE placa = 'XYZ-789'), '2026-10-22', '09:00:00');

-- Pagos: Carlos debe $150.000 (mora); Ana esta al dia
INSERT INTO autoescuela_db.pagos (estudiante_id, monto, metodo, registrado_por) VALUES
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '123456789'),  450000, 'Efectivo',
    (SELECT id FROM autoescuela_db.usuarios WHERE usuario = 'tesorera')),
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '987654321'),  600000, 'Transferencia',
    (SELECT id FROM autoescuela_db.usuarios WHERE usuario = 'tesorera')),
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '1020304050'), 600000, 'Tarjeta',
    (SELECT id FROM autoescuela_db.usuarios WHERE usuario = 'tesorera')),
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '1030405060'), 300000, 'Efectivo',
    (SELECT id FROM autoescuela_db.usuarios WHERE usuario = 'tesorera'));

INSERT INTO autoescuela_db.competencias (nombre) VALUES
('Parqueo en reversa'), ('Uso de direccionales'), ('Frenado de emergencia'),
('Cambios de carril'), ('Manejo en pendiente');

INSERT INTO autoescuela_db.estudiante_competencias (estudiante_id, competencia_id, estado) VALUES
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '123456789'), (SELECT id FROM autoescuela_db.competencias WHERE nombre = 'Parqueo en reversa'),   'PENDIENTE'),
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '123456789'), (SELECT id FROM autoescuela_db.competencias WHERE nombre = 'Uso de direccionales'), 'DOMINADO'),
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '123456789'), (SELECT id FROM autoescuela_db.competencias WHERE nombre = 'Frenado de emergencia'),'EN_PROCESO'),
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '123456789'), (SELECT id FROM autoescuela_db.competencias WHERE nombre = 'Cambios de carril'),    'DOMINADO'),
((SELECT id FROM autoescuela_db.estudiantes WHERE documento = '123456789'), (SELECT id FROM autoescuela_db.competencias WHERE nombre = 'Manejo en pendiente'),  'PENDIENTE');

INSERT INTO autoescuela_db.preguntas_examen (enunciado, opcion_a, opcion_b, opcion_c, respuesta_correcta) VALUES
('¿Qué indica una señal triangular de fondo amarillo?', 'Prohibición', 'Advertencia / Peligro', 'Información', 'B'),
('¿Cuál es la distancia mínima de seguimiento recomendada en vía urbana?', '1 segundo', '2-3 segundos', '10 segundos', 'B'),
('Un semáforo en amarillo intermitente significa:', 'Detenerse totalmente', 'Avanzar con precaución', 'Vía preferencial total', 'B'),
('¿Qué documento es obligatorio portar al conducir?', 'Licencia de conducción vigente', 'Solo la cédula', 'Ninguno', 'A'),
('El uso del cinturón de seguridad es obligatorio para:', 'Solo el conductor', 'Todos los ocupantes', 'Solo asientos traseros', 'B');

-- Para crear usuarios nuevos: ejecuta seguridad/GeneradorHash.java (Run File),
-- escribe la contrasena y copia el valor generado en clave_hash.
