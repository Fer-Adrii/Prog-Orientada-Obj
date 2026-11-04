import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * HU-WEB-006 · Rol: Tesorera / Secretaria
 * "Necesito registrar los abonos y pagos de cuotas realizados por los
 * alumnos y expedir su comprobante de caja."
 *
 * Cubre: registro de abono con liberación de cupo y visualización de
 * mora / saldo pendiente del alumno.
 */
public class RegistroPagosApp extends JFrame {

    private JComboBox<String> alumnoCombo, metodoCombo;
    private JTextField montoField;
    private JLabel saldoLabel;

    public RegistroPagosApp() {
        super("Luz Verde - Registro de Pagos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(UIStyle.buildHeader("Tesorería"), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.CENTER);

        setContentPane(root);
        setSize(460, 520);
        setLocationRelativeTo(null);
    }

    private JComponent buildForm() {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(UIStyle.BG_LIGHT);
        wrap.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Registrar Pago");
        title.setFont(UIStyle.FONT_H1);
        title.setForeground(UIStyle.GOLD_TEXT);
        wrap.add(title);

        wrap.add(UIStyle.fieldLabel("Alumno"));
        alumnoCombo = new JComboBox<>(new String[]{"Carlos Ruiz (Mora: $150.000)", "Ana Torres (Al día)"});
        UIStyle.styleField(alumnoCombo);
        wrap.add(alumnoCombo);

        saldoLabel = new JLabel("Saldo pendiente: $150.000");
        saldoLabel.setFont(UIStyle.FONT_SMALL);
        saldoLabel.setForeground(UIStyle.RED_ALERT);
        saldoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        saldoLabel.setBorder(new EmptyBorder(4, 0, 10, 0));
        wrap.add(saldoLabel);

        alumnoCombo.addActionListener(e -> {
            boolean enMora = ((String) alumnoCombo.getSelectedItem()).contains("Mora");
            saldoLabel.setText(enMora ? "Saldo pendiente: $150.000" : "Saldo pendiente: $0 (Al día)");
            saldoLabel.setForeground(enMora ? UIStyle.RED_ALERT : UIStyle.GREEN_OK);
        });

        wrap.add(UIStyle.fieldLabel("Monto a registrar"));
        montoField = new JTextField();
        UIStyle.styleField(montoField);
        wrap.add(montoField);

        wrap.add(UIStyle.fieldLabel("Método de pago"));
        metodoCombo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
        UIStyle.styleField(metodoCombo);
        wrap.add(metodoCombo);

        wrap.add(Box.createVerticalStrut(10));
        JButton adjuntar = UIStyle.secondaryButton("Adjuntar soporte de pago");
        adjuntar.setAlignmentX(Component.LEFT_ALIGNMENT);
        adjuntar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        wrap.add(adjuntar);

        wrap.add(Box.createVerticalStrut(20));
        JButton registrar = UIStyle.primaryButton("Registrar Pago");
        registrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        registrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        registrar.addActionListener(e -> onRegistrar());
        wrap.add(registrar);

        return wrap;
    }

    private void onRegistrar() {
        String monto = montoField.getText().trim();
        if (monto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el monto del pago.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Double.parseDouble(monto.replace(",", "").replace("$", ""));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un valor numérico.",
                    "Monto inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Pago registrado. Se habilitó el agendamiento de horas prácticas.",
                "Pago exitoso", JOptionPane.INFORMATION_MESSAGE);
        saldoLabel.setText("Saldo pendiente: $0 (Al día)");
        saldoLabel.setForeground(UIStyle.GREEN_OK);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroPagosApp().setVisible(true));
    }
}
