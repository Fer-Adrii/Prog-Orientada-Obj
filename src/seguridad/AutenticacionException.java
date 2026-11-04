package seguridad;

/**
 * Fallo de autenticación (credenciales incorrectas, cuenta inactiva, etc.).
 * El mensaje es apto para mostrarse al usuario.
 */
public class AutenticacionException extends Exception {
    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}
