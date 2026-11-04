import seguridad.Modulo;
import seguridad.Usuario;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * MenuUsuariosApp
 * Panel principal tras iniciar sesión. Solo muestra las pantallas de
 * Luz Verde (HU-WEB-001 a HU-WEB-006) que el rol del usuario puede usar.
 */
public class MenuUsuariosApp extends JFrame {

    private final Usuario usuario;
    private final List<JFrame> ventanasAbiertas = new ArrayList<>();

    public MenuUsuariosApp(Usuario usuario) {
        super("Luz Verde - Panel por Rol de Usuario");
        this.usuario = usuario;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.add(UIStyle.buildHeader("Panel de Usuarios"));
        norte.add(buildBarraSesion());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(norte, BorderLayout.NORTH);
        root.add(buildGrid(), BorderLayout.CENTER);

        setContentPane(root);
        setSize(560, 480);
        setLocationRelativeTo(null);
    }

    /** Barra con el usuario conectado y el botón de cerrar sesión. */
    private JComponent buildBarraSesion() {
        JPanel barra = new JPanel(new BorderLayout(10, 0));
        barra.setBackground(UIStyle.WHITE);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.CARD_BORDER),
                new EmptyBorder(8, 25, 8, 25)));
        barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JLabel info = new JLabel("<html><b>" + escapar(usuario.getNombreCompleto())
                + "</b> · " + escapar(usuario.getRol().getEtiqueta()) + "</html>");
        info.setFont(UIStyle.FONT_SMALL);
        info.setForeground(UIStyle.TEXT_DARK);
        barra.add(info, BorderLayout.CENTER);

        JButton salir = UIStyle.secondaryButton("Cerrar sesión");
        salir.addActionListener(e -> cerrarSesion());
        barra.add(salir, BorderLayout.EAST);
        return barra;
    }

    private JComponent buildGrid() {
        JPanel grid = new JPanel(new GridLayout(0, 2, 15, 15));
        grid.setBackground(UIStyle.BG_LIGHT);

        agregarCard(grid, Modulo.REGISTRO_ESTUDIANTE, "HU-WEB-001 · Secretaria",
                "Registrar Estudiante", RegistroEstudianteApp::new);
        agregarCard(grid, Modulo.AGENDAR_CLASE, "HU-WEB-002 · Coordinador",
                "Agendar Clase Práctica", AgendarClaseApp::new);
        agregarCard(grid, Modulo.SIMULACRO_EXAMEN, "HU-WEB-003 · Estudiante",
                "Simulacro de Examen", SimulacroExamenApp::new);
        agregarCard(grid, Modulo.FICHA_PEDAGOGICA, "HU-WEB-004 · Instructor",
                "Ficha Pedagógica", FichaPedagogicaApp::new);
        agregarCard(grid, Modulo.GESTION_FLOTA, "HU-WEB-005 · Enc. de Flota",
                "Gestión de Flota", GestionFlotaApp::new);
        agregarCard(grid, Modulo.REGISTRO_PAGOS, "HU-WEB-006 · Tesorera",
                "Registro de Pagos", RegistroPagosApp::new);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(UIStyle.BG_LIGHT);
        wrap.setBorder(new EmptyBorder(25, 25, 25, 25));
        wrap.add(grid, BorderLayout.NORTH); // las tarjetas no se estiran si hay pocas
        return wrap;
    }

    /** Agrega la tarjeta solo si el rol del usuario tiene permiso sobre el módulo. */
    private void agregarCard(JPanel grid, Modulo modulo, String rol, String accion,
                             Supplier<JFrame> ventana) {
        if (usuario.puedeAcceder(modulo)) {
            grid.add(buildCard(rol, accion, modulo, ventana));
        }
    }

    private JPanel buildCard(String rol, String accion, Modulo modulo, Supplier<JFrame> ventana) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.CARD_BORDER, 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel rolLbl = new JLabel(rol);
        rolLbl.setFont(UIStyle.FONT_H2);
        rolLbl.setForeground(UIStyle.TEXT_DARK);
        card.add(rolLbl);

        JLabel accionLbl = new JLabel(accion);
        accionLbl.setFont(UIStyle.FONT_SMALL);
        accionLbl.setForeground(UIStyle.TEXT_GRAY);
        accionLbl.setBorder(new EmptyBorder(4, 0, 12, 0));
        card.add(accionLbl);

        JButton abrir = UIStyle.primaryButton("Abrir");
        abrir.setAlignmentX(Component.LEFT_ALIGNMENT);
        abrir.addActionListener(e -> abrirModulo(modulo, ventana));
        card.add(abrir);

        return card;
    }

    private void abrirModulo(Modulo modulo, Supplier<JFrame> ventana) {
        if (!usuario.puedeAcceder(modulo)) { // doble verificación de permisos
            JOptionPane.showMessageDialog(this, "Tu rol no tiene acceso a esta función.",
                    "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFrame frame = ventana.get();
        ventanasAbiertas.add(frame);
        frame.setVisible(true);
    }

    private void cerrarSesion() {
        for (JFrame f : ventanasAbiertas) {
            f.dispose();
        }
        dispose();
        new LoginApp().setVisible(true);
    }

    private static String escapar(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static void main(String[] args) {
        // El panel requiere sesión: se entra siempre por el login
        LoginApp.main(args);
    }
}
