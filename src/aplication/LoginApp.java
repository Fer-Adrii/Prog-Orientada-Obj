import seguridad.AutenticacionException;
import seguridad.Usuario;
import seguridad.UsuarioDAO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Pantalla de inicio de sesión de Luz Verde.
 * Valida usuario y contraseña contra la base de datos y, si son correctos,
 * abre el panel de funciones según el rol del usuario.
 */
public class LoginApp extends JFrame {

    private JTextField usuarioField;
    private JPasswordField claveField;
    private JCheckBox mostrarClave;
    private JLabel errorLabel;
    private JButton ingresarBtn;
    private final char echoPorDefecto;

    public LoginApp() {
        super("Luz Verde - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(UIStyle.buildHeader("Acceso al Sistema"), BorderLayout.NORTH);
        root.add(buildCentro(), BorderLayout.CENTER);

        echoPorDefecto = claveField.getEchoChar();

        setContentPane(root);
        getRootPane().setDefaultButton(ingresarBtn); // Enter inicia sesión
        setSize(440, 540);
        setResizable(false);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                usuarioField.requestFocusInWindow();
            }
        });
    }

    private JComponent buildCentro() {
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(UIStyle.BG_LIGHT);
        centro.add(buildTarjeta());
        return centro;
    }

    private JPanel buildTarjeta() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.CARD_BORDER, 1),
                new EmptyBorder(25, 30, 25, 30)));
        card.setPreferredSize(new Dimension(380, 420));

        // Logo
        JPanel logo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        logo.setOpaque(false);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        logo.add(new LuzVerdeApp.TrafficLightIcon(18, 40));
        JLabel logoTxt = new JLabel("Luz Verde");
        logoTxt.setFont(LuzVerdeApp.FONT_LOGO);
        logoTxt.setForeground(UIStyle.TEXT_DARK);
        logo.add(logoTxt);
        card.add(logo);
        card.add(Box.createVerticalStrut(18));

        JLabel titulo = new JLabel("Iniciar Sesión");
        titulo.setFont(UIStyle.FONT_H1);
        titulo.setForeground(UIStyle.GOLD_TEXT);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titulo);

        JLabel sub = new JLabel("Ingresa con tu usuario y contraseña");
        sub.setFont(UIStyle.FONT_SMALL);
        sub.setForeground(UIStyle.TEXT_GRAY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setBorder(new EmptyBorder(2, 0, 14, 0));
        card.add(sub);

        card.add(UIStyle.fieldLabel("Usuario"));
        card.add(Box.createVerticalStrut(4));
        usuarioField = new JTextField();
        UIStyle.styleField(usuarioField);
        card.add(usuarioField);
        card.add(Box.createVerticalStrut(12));

        card.add(UIStyle.fieldLabel("Contraseña"));
        card.add(Box.createVerticalStrut(4));
        claveField = new JPasswordField();
        UIStyle.styleField(claveField);
        card.add(claveField);
        card.add(Box.createVerticalStrut(6));

        mostrarClave = new JCheckBox("Mostrar contraseña");
        mostrarClave.setFont(UIStyle.FONT_SMALL);
        mostrarClave.setForeground(UIStyle.TEXT_GRAY);
        mostrarClave.setOpaque(false);
        mostrarClave.setFocusPainted(false);
        mostrarClave.setAlignmentX(Component.LEFT_ALIGNMENT);
        mostrarClave.addActionListener(e ->
                claveField.setEchoChar(mostrarClave.isSelected() ? (char) 0 : echoPorDefecto));
        card.add(mostrarClave);
        card.add(Box.createVerticalStrut(8));

        // Espacio reservado (2 líneas) para el mensaje de error: evita que el diseño "salte"
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UIStyle.FONT_SMALL);
        errorLabel.setForeground(UIStyle.RED_ALERT);
        errorLabel.setVerticalAlignment(SwingConstants.TOP);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorLabel.setPreferredSize(new Dimension(10, 32));
        errorLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(6));

        ingresarBtn = UIStyle.primaryButton("Ingresar");
        ingresarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        ingresarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ingresarBtn.addActionListener(e -> onIngresar());
        card.add(ingresarBtn);

        card.add(Box.createVerticalGlue());
        JLabel ayuda = new JLabel("¿Problemas? Contacta al administrador.");
        ayuda.setFont(UIStyle.FONT_SMALL);
        ayuda.setForeground(UIStyle.TEXT_GRAY);
        ayuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(ayuda);

        return card;
    }

    private void onIngresar() {
        String usuario = usuarioField.getText().trim();
        char[] clave = claveField.getPassword();

        if (usuario.isEmpty() || clave.length == 0) {
            mostrarError("Ingresa tu usuario y contraseña.");
            Arrays.fill(clave, '\0');
            return;
        }

        try {
            Usuario autenticado = new UsuarioDAO().autenticar(usuario, clave);
            errorLabel.setText(" ");
            new MenuUsuariosApp(autenticado).setVisible(true);
            dispose();
        } catch (AutenticacionException ex) {
            mostrarError(ex.getMessage());
            claveField.setText("");
            claveField.requestFocusInWindow();
        } catch (SQLException ex) {
            mostrarError("No se pudo consultar la base de datos.");
            JOptionPane.showMessageDialog(this,
                    ex.getMessage() + "\n\nVerifica que exista la tabla 'usuarios' "
                    + "(ejecuta sql/autoescuela_db_completa.sql).",
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(clave, '\0');
        }
    }

    private void mostrarError(String mensaje) {
        String seguro = mensaje.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        errorLabel.setText("<html>" + seguro + "</html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginApp().setVisible(true));
    }
}
