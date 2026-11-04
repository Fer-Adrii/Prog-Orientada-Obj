import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * HU-WEB-005 · Rol: Encargado de Flota
 * "Necesito registrar la información técnica y fechas de expiración de
 * SOAT y revisión técnico-mecánica de la flota."
 *
 * Cubre: inhabilitación por documentación vencida, inhabilitación por
 * mantenimiento de taller y renovación/habilitación de un vehículo.
 */
public class GestionFlotaApp extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public GestionFlotaApp() {
        super("Luz Verde - Gestión de Flota");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(UIStyle.buildHeader("Gestión de Flota"), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);

        setContentPane(root);
        setSize(640, 480);
        setLocationRelativeTo(null);
    }

    private JComponent buildBody() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(UIStyle.BG_LIGHT);
        wrap.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Vehículos y Motos Registrados");
        title.setFont(UIStyle.FONT_H1);
        title.setForeground(UIStyle.GOLD_TEXT);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        wrap.add(title, BorderLayout.NORTH);

        String[] columnas = {"Placa", "Tipo", "SOAT vence", "Rev. Técnica vence", "Estado"};
        Object[][] datos = {
                {"ABC-123", "Moto", "10/09/2026", "15/01/2027", "Activo / Operativo"},
                {"XYZ-789", "Auto", "01/08/2026", "20/08/2026", "Activo / Operativo"},
                {"DEF-456", "Moto", "05/06/2026", "05/06/2026", "Inhabilitado por Documentación"},
                {"GHI-321", "Auto", "12/12/2026", "12/12/2026", "Inhabilitado por Taller"}
        };
        modelo = new DefaultTableModel(datos, columnas) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFont(UIStyle.FONT_BODY);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(UIStyle.FONT_H2);
        tabla.getTableHeader().setBackground(UIStyle.OLIVE);
        tabla.getTableHeader().setForeground(UIStyle.WHITE);
        tabla.getColumnModel().getColumn(4).setCellRenderer(new EstadoRenderer());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(UIStyle.CARD_BORDER, 1));
        wrap.add(scroll, BorderLayout.CENTER);

        JButton actualizar = UIStyle.primaryButton("Actualizar Documento y Habilitar");
        actualizar.setBorder(new EmptyBorder(10, 15, 10, 15));
        actualizar.addActionListener(e -> onActualizar());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setOpaque(false);
        south.setBorder(new EmptyBorder(12, 0, 0, 0));
        south.add(actualizar);
        wrap.add(south, BorderLayout.SOUTH);

        return wrap;
    }

    private void onActualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un vehículo de la tabla.",
                    "Ningún vehículo seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String estadoActual = (String) modelo.getValueAt(fila, 4);
        if (estadoActual.equals("Inhabilitado por Taller")) {
            JOptionPane.showMessageDialog(this,
                    "Este vehículo tiene clases agendadas próximas. Reasigna manualmente los alumnos antes de continuar.",
                    "Clases afectadas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modelo.setValueAt("Activo / Operativo", fila, 4);
        JOptionPane.showMessageDialog(this,
                "El vehículo fue reincorporado a la lista de disponibles para agendamiento.",
                "Vehículo habilitado", JOptionPane.INFORMATION_MESSAGE);
    }

    static class EstadoRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            String estado = String.valueOf(v);
            if (estado.startsWith("Activo")) l.setForeground(UIStyle.GREEN_OK);
            else if (estado.contains("Taller")) l.setForeground(UIStyle.YELLOW_WARN.darker());
            else l.setForeground(UIStyle.RED_ALERT);
            l.setFont(UIStyle.FONT_BODY);
            return l;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionFlotaApp().setVisible(true));
    }
}
