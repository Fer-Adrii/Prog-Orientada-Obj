package seguridad;

import basededatos.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Acceso a la tabla usuarios para validar el inicio de sesión.
 * Si tu tabla o columnas se llaman distinto, cambia solo la consulta SQL.
 */
public class UsuarioDAO {

    private static final String SQL_BUSCAR =
            "SELECT id, usuario, clave_hash, nombre_completo, rol, activo "
            + "FROM usuarios WHERE usuario = ?";

    private static final String MSG_INVALIDO = "Usuario o contraseña incorrectos.";

    /**
     * Valida usuario y contraseña contra la base de datos.
     *
     * @return el usuario autenticado
     * @throws AutenticacionException si las credenciales no son válidas o la cuenta no puede entrar
     * @throws SQLException si falla la consulta (por ejemplo, la tabla no existe)
     */
    public Usuario autenticar(String nombreUsuario, char[] clave)
            throws AutenticacionException, SQLException {

        Conexion conexion = Conexion.getInstancia();
        Connection cn = conexion.conectar();
        try (PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    // Se calcula un hash igual para que tardar lo mismo exista o no el usuario
                    PasswordUtil.hash(clave);
                    throw new AutenticacionException(MSG_INVALIDO);
                }
                if (!PasswordUtil.verificar(clave, rs.getString("clave_hash"))) {
                    throw new AutenticacionException(MSG_INVALIDO);
                }
                if (!rs.getBoolean("activo")) {
                    throw new AutenticacionException(
                            "Tu cuenta está inactiva. Comunícate con el administrador.");
                }
                Rol rol = Rol.desdeBD(rs.getString("rol"));
                if (rol == null) {
                    throw new AutenticacionException(
                            "Tu cuenta no tiene un rol válido. Comunícate con el administrador.");
                }
                return new Usuario(rs.getInt("id"), rs.getString("usuario"),
                        rs.getString("nombre_completo"), rol);
            }
        } finally {
            conexion.desconectar();
        }
    }
}
