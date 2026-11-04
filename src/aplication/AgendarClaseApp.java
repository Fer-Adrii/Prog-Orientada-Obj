import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * HU-WEB-002 · Rol: Coordinador de Clases
 * "Necesito agendar clases prácticas en la matriz del calendario
 * seleccionando fecha, hora, alumno, instructor y vehículo/moto."
 *
 * Cubre: agendamiento exitoso, conflicto de vehículo y vehículo
 * inhabilitado por taller/documentación.
 */
public class AgendarClaseApp extends JFrame {

    private JComboBox<String> alumnoCombo, instructorCombo, vehiculoCombo, horaCombo;
    private JSpinner fechaSpinner;

    // Reservas ya existentes, simuladas para validar cruces de horario (demo)
    private static final List<String> RESERVAS = new ArrayList<>();
    static {
        RESERVAS.add("Carlos Ruiz|08:00|Moto ABC-123");
        RESERVAS.add("Ana Torres|09:00|Auto XYZ-789");
    }

    private static final String VEHICULO_INHABILITADO = "Moto DEF-456 (Inhabilitado por Taller)";

    public AgendarClaseApp() {
        super("Luz Verde - Agendar Clase Práctica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(UIStyle.buildHeader("Agenda de Clases"), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.CENTER);

        setContentPane(root);
        setSize(480, 560);
        setLocationRelativeTo(null);
    }

    private JComponent buildForm() {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(UIStyle.BG_LIGHT);
        wrap.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Nueva Clase Práctica");
        title.setFont(UIStyle.FONT_H1);
        title.setForeground(UIStyle.GOLD_TEXT);
        wrap.add(title);

        wrap.add(UIStyle.fieldLabel("Alumno"));
        alumnoCombo = new JComboBox<>(new String[]{"Carlos Ruiz", "Ana Torres", "Luis Peña", "María Gómez"});
        UIStyle.styleField(alumnoCombo);
        wrap.add(alumnoCombo);

        wrap.add(UIStyle.fieldLabel("Instructor"));
        instructorCombo = new JComboBox<>(new String[]{"Pedro Sánchez", "Diana Vélez"});
        UIStyle.styleField(instructorCombo);
        wrap.add(instructorCombo);

        wrap.add(UIStyle.fieldLabel("Vehículo / Moto"));
        vehiculoCombo = new JComboBox<>(new String[]{"Moto ABC-123", "Auto XYZ-789", VEHICULO_INHABILITADO});
        UIStyle.styleField(vehiculoCombo);
        wrap.add(vehiculoCombo);

        wrap.add(UIStyle.fieldLabel("Fecha"));
        fechaSpinner = new JSpinner(new SpinnerDateModel());
        fechaSpinner.setEditor(new JSpinner.DateEditor(fechaSpinner, "dd/MM/yyyy"));
        UIStyle.styleField(fechaSpinner);
        wrap.add(fechaSpinner);

        wrap.add(UIStyle.fieldLabel("Hora"));
        horaCombo = new JComboBox<>(new String[]{"08:00", "09:00", "10:00", "11:00", "14:00", "15:00"});
        UIStyle.styleField(horaCombo);
        wrap.add(horaCombo);

        wrap.add(Box.createVerticalStrut(25));
        JButton confirmar = UIStyle.primaryButton("Confirmar Cita");
        confirmar.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmar.addActionListener(e -> onConfirmar());
        wrap.add(confirmar);

        return wrap;
    }

    private void onConfirmar() {
        String vehiculo = (String) vehiculoCombo.getSelectedItem();
        String hora = (String) horaCombo.getSelectedItem();
        String instructor = (String) instructorCombo.getSelectedItem();
        String alumno = (String) alumnoCombo.getSelectedItem();

        if (vehiculo.equals(VEHICULO_INHABILITADO)) {
            JOptionPane.showMessageDialog(this,
                    "El vehículo/moto seleccionado no está disponible: inhabilitado por taller.",
                    "Vehículo no disponible", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (String reserva : RESERVAS) {
            String[] partes = reserva.split("\\|");
            if (partes[1].equals(hora) && partes[2].equals(vehiculo)) {
                JOptionPane.showMessageDialog(this,
                        "El vehículo/moto seleccionado no está disponible en este horario.",
                        "Conflicto de vehículo", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        RESERVAS.add(alumno + "|" + hora + "|" + vehiculo);
        JOptionPane.showMessageDialog(this,
                "Clase registrada. Se notificó al instructor " + instructor + " y al estudiante " + alumno + ".",
                "Agendamiento exitoso", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AgendarClaseApp().setVisible(true));
    }
}
