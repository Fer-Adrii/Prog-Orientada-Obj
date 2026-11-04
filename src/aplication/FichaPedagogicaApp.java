import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * HU-WEB-004 · Rol: Instructor de conducción
 * "Necesito consultar la ficha pedagógica e historial de maniobras del
 * alumno asignado antes de iniciar la clase."
 *
 * Cubre: consulta de competencias (semáforo), reporte de inasistencia,
 * reporte de falla técnica y guardado de bitácora pedagógica.
 */
public class FichaPedagogicaApp extends JFrame {

    public FichaPedagogicaApp() {
        super("Luz Verde - Ficha Pedagógica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(UIStyle.buildHeader("Ficha del Alumno"), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);

        setContentPane(root);
        setSize(500, 640);
        setLocationRelativeTo(null);
    }

    private JComponent buildBody() {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(UIStyle.BG_LIGHT);
        wrap.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Carlos Ruiz · Categoría B1");
        title.setFont(UIStyle.FONT_H1);
        title.setForeground(UIStyle.GOLD_TEXT);
        wrap.add(title);
        wrap.add(Box.createVerticalStrut(15));

        String[][] competencias = {
                {"Parqueo en reversa", "Pendiente", "R"},
                {"Uso de direccionales", "Dominado", "G"},
                {"Frenado de emergencia", "En proceso", "Y"},
                {"Cambios de carril", "Dominado", "G"},
                {"Manejo en pendiente", "Pendiente", "R"}
        };
        for (String[] c : competencias) {
            wrap.add(buildCompetenciaRow(c[0], c[1], c[2]));
            wrap.add(Box.createVerticalStrut(8));
        }

        wrap.add(Box.createVerticalStrut(15));

        JButton inasistencia = UIStyle.secondaryButton("Reportar Inasistencia de Alumno");
        inasistencia.setAlignmentX(Component.LEFT_ALIGNMENT);
        inasistencia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        inasistencia.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Clase marcada como \"Cancelada por Inasistencia\". Se notificó al área administrativa.",
                "Inasistencia registrada", JOptionPane.WARNING_MESSAGE));
        wrap.add(inasistencia);
        wrap.add(Box.createVerticalStrut(8));

        JButton falla = UIStyle.secondaryButton("Reportar Falla Técnica del Vehículo");
        falla.setAlignmentX(Component.LEFT_ALIGNMENT);
        falla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        falla.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Vehículo marcado como \"Bajo Revisión\". Se notificó al encargado de flota.",
                "Falla reportada", JOptionPane.WARNING_MESSAGE));
        wrap.add(falla);
        wrap.add(Box.createVerticalStrut(15));

        JButton guardar = UIStyle.primaryButton("Guardar Evaluación de la Sesión");
        guardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        guardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        guardar.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Se actualizó el porcentaje de avance del estudiante y la hora quedó marcada como completada.",
                "Evaluación guardada", JOptionPane.INFORMATION_MESSAGE));
        wrap.add(guardar);

        return wrap;
    }

    private JPanel buildCompetenciaRow(String nombre, String estado, String semaforo) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(true);
        row.setBackground(UIStyle.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.CARD_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel nombreLbl = new JLabel(nombre);
        nombreLbl.setFont(UIStyle.FONT_BODY);
        nombreLbl.setForeground(UIStyle.TEXT_DARK);
        row.add(nombreLbl, BorderLayout.WEST);

        JLabel estadoLbl = new JLabel(estado, SwingConstants.RIGHT);
        estadoLbl.setFont(UIStyle.FONT_SMALL);
        Color color = semaforo.equals("G") ? UIStyle.GREEN_OK
                : semaforo.equals("Y") ? UIStyle.YELLOW_WARN.darker()
                : UIStyle.RED_ALERT;
        estadoLbl.setForeground(color);
        row.add(estadoLbl, BorderLayout.EAST);

        return row;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FichaPedagogicaApp().setVisible(true));
    }
}
