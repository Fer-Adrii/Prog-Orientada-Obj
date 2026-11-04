package seguridad;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Roles del sistema y los módulos que cada uno puede usar.
 * El nombre de la constante es el valor que se guarda en la columna
 * "rol" de la tabla usuarios.
 */
public enum Rol {
    ADMINISTRADOR("Administrador", Modulo.values()),
    SECRETARIA("Secretaria", Modulo.REGISTRO_ESTUDIANTE, Modulo.REGISTRO_PAGOS),
    COORDINADOR("Coordinador de Clases", Modulo.AGENDAR_CLASE),
    ESTUDIANTE("Estudiante", Modulo.SIMULACRO_EXAMEN),
    INSTRUCTOR("Instructor", Modulo.FICHA_PEDAGOGICA),
    ENCARGADO_FLOTA("Encargado de Flota", Modulo.GESTION_FLOTA),
    TESORERA("Tesorera", Modulo.REGISTRO_PAGOS);

    private final String etiqueta;
    private final Set<Modulo> modulos;

    Rol(String etiqueta, Modulo... modulos) {
        this.etiqueta = etiqueta;
        this.modulos = EnumSet.noneOf(Modulo.class);
        this.modulos.addAll(Arrays.asList(modulos));
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public boolean puedeAcceder(Modulo modulo) {
        return modulos.contains(modulo);
    }

    /** Convierte el texto guardado en la BD a un Rol; devuelve null si no es válido. */
    public static Rol desdeBD(String valor) {
        if (valor == null) {
            return null;
        }
        try {
            return Rol.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
