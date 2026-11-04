package seguridad;

/**
 * Usuario autenticado. No guarda la contraseña.
 */
public class Usuario {
    private final int id;
    private final String nombreUsuario;
    private final String nombreCompleto;
    private final Rol rol;

    public Usuario(int id, String nombreUsuario, String nombreCompleto, Rol rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public int getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public Rol getRol() { return rol; }

    public boolean puedeAcceder(Modulo modulo) {
        return rol.puedeAcceder(modulo);
    }
}
