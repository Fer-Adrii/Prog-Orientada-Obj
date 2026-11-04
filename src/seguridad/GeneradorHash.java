package seguridad;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Herramienta para crear usuarios nuevos: pide una contraseña y muestra el
 * texto que debes guardar en la columna clave_hash.
 * Ejecutar con clic derecho > Run File.
 */
public class GeneradorHash {
    public static void main(String[] args) {
        JPasswordField campo = new JPasswordField(20);
        int op = JOptionPane.showConfirmDialog(null, campo, "Contraseña a cifrar",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op != JOptionPane.OK_OPTION || campo.getPassword().length == 0) {
            return;
        }
        String hash = PasswordUtil.hash(campo.getPassword());

        System.out.println("clave_hash = " + hash);
        System.out.println();
        System.out.println("INSERT INTO usuarios (usuario, clave_hash, nombre_completo, rol) VALUES");
        System.out.println("('nuevo_usuario', '" + hash + "', 'Nombre Apellido', 'SECRETARIA');");

        JTextField salida = new JTextField(hash, 45);
        salida.setEditable(false);
        JOptionPane.showMessageDialog(null, salida, "Copia este valor en clave_hash",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
