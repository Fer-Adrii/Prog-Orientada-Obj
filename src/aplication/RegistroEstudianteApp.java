import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * HU-WEB-001 · Rol: Secretaria / Administrador
 * "Necesito registrar a un nuevo estudiante seleccionando categoría y
 * adjuntando cédula y examen médico."
 *
 * Cubre los escenarios: registro exitoso, bloqueo por falta de examen
 * médico, archivo con formato no permitido y documento duplicado.
 */
public class RegistroEstudianteApp extends JFrame {

    private JTextField nombreField, documentoField;
    private JComboBox<String> categoriaCombo;
    private JLabel cedulaLabel, examenLabel;
    private boolean examenAdjunto = false;

    // Simula la base de datos de documentos ya registrados (escenario 4)
    private static final Set<String> DOCUMENTOS_EXISTENTES = new HashSet<>();
    static { DOCUMENTOS_EXISTENTES.add("123456789"); }

    public RegistroEstudianteApp() {
        super("Luz Verde - Registrar Estudiante");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(UIStyle.buildHeader("Registro de Estudiantes"), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.CENTER);

        setContentPane(root);
        setSize(480, 640);
        setLocationRelativeTo(null);
    }

    private JComponent buildForm() {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(UIStyle.BG_LIGHT);
        wrap.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Nuevo Estudiante");
        title.setFont(UIStyle.FONT_H1);
        title.setForeground(UIStyle.GOLD_TEXT);
        wrap.add(title);

        wrap.add(UIStyle.fieldLabel("Nombre completo"));
        nombreField = new JTextField();
        UIStyle.styleField(nombreField);
        wrap.add(nombreField);

        wrap.add(UIStyle.fieldLabel("N° de documento"));
        documentoField = new JTextField();
        UIStyle.styleField(documentoField);
        wrap.add(documentoField);

        wrap.add(UIStyle.fieldLabel("Categoría"));
        categoriaCombo = new JComboBox<>(new String[]{"Auto B1", "Auto C1", "Moto A1", "Moto A2", "Combinado"});
        UIStyle.styleField(categoriaCombo);
        categoriaCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        wrap.add(categoriaCombo);

        wrap.add(Box.createVerticalStrut(15));
        wrap.add(buildAttachRow("Cédula de ciudadanía", true));
        wrap.add(Box.createVerticalStrut(8));
        wrap.add(buildAttachRow("Examen médico de aptitud", false));

        wrap.add(Box.createVerticalStrut(25));
        JButton guardar = UIStyle.primaryButton("Guardar y Matricular");
        guardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        guardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        guardar.addActionListener(e -> onGuardar());
        wrap.add(guardar);

        return wrap;
    }

    private JPanel buildAttachRow(String label, boolean esCedula) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIStyle.FONT_SMALL);
        lbl.setForeground(UIStyle.TEXT_DARK);
        row.add(lbl, BorderLayout.WEST);

        JLabel status = new JLabel("Sin adjuntar");
        status.setFont(UIStyle.FONT_SMALL);
        status.setForeground(UIStyle.RED_ALERT);
        row.add(status, BorderLayout.CENTER);
        if (esCedula) cedulaLabel = status; else examenLabel = status;

        JButton sel = UIStyle.secondaryButton("Adjuntar");
        sel.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                String fname = chooser.getSelectedFile().getName();
                String lower = fname.toLowerCase();
                boolean formatoValido = lower.endsWith(".pdf") || lower.endsWith(".jpg") || lower.endsWith(".png");
                if (!formatoValido) {
                    JOptionPane.showMessageDialog(this,
                            "Formato no soportado. Suba archivos únicamente en PDF, JPG o PNG menores a 10 MB.",
                            "Archivo inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                status.setText(fname);
                status.setForeground(UIStyle.GREEN_OK);
                if (!esCedula) examenAdjunto = true;
            }
        });
        row.add(sel, BorderLayout.EAST);
        return row;
    }

    private void onGuardar() {
        String documento = documentoField.getText().trim();

        if (documento.isEmpty() || nombreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa nombre y número de documento.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (DOCUMENTOS_EXISTENTES.contains(documento)) {
            JOptionPane.showMessageDialog(this,
                    "El documento " + documento + " ya pertenece a un estudiante activo o registrado.",
                    "Documento duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!examenAdjunto) {
            examenLabel.setForeground(UIStyle.RED_ALERT);
            JOptionPane.showMessageDialog(this,
                    "Es obligatorio adjuntar el examen médico de aptitud vigente.",
                    "Registro bloqueado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DOCUMENTOS_EXISTENTES.add(documento);
        JOptionPane.showMessageDialog(this, "Estudiante matriculado con éxito.",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroEstudianteApp().setVisible(true));
    }
}
