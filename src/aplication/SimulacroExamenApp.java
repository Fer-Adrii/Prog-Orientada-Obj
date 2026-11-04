import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * HU-WEB-003 · Rol: Estudiante
 * "Necesito ingresar al portal web para responder evaluaciones simuladas
 * con banco de preguntas oficial de tránsito y temporizador."
 *
 * Cubre: aprobación (>=80%), reprobación con retroalimentación y
 * agotamiento del tiempo límite.
 */
public class SimulacroExamenApp extends JFrame {

    static class Pregunta {
        String enunciado;
        String[] opciones;
        int correcta;
        Pregunta(String e, String[] o, int c) { enunciado = e; opciones = o; correcta = c; }
    }

    private final List<Pregunta> preguntas = new ArrayList<>();
    private int indice = 0;
    private int aciertos = 0;
    private ButtonGroup grupo;
    private JLabel enunciadoLabel, contadorLabel, timerLabel;
    private JPanel opcionesPanel;
    private javax.swing.Timer timer;
    private int segundosRestantes = 45 * 60; // 45 minutos según el criterio de aceptación

    public SimulacroExamenApp() {
        super("Luz Verde - Simulacro de Examen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cargarPreguntas();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG_LIGHT);
        root.add(UIStyle.buildHeader("Simulacro Teórico"), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);

        setContentPane(root);
        setSize(500, 480);
        setLocationRelativeTo(null);

        mostrarPregunta();
        iniciarTemporizador();
    }

    private void cargarPreguntas() {
        preguntas.add(new Pregunta("¿Qué indica una señal triangular de fondo amarillo?",
                new String[]{"Prohibición", "Advertencia / Peligro", "Información"}, 1));
        preguntas.add(new Pregunta("¿Cuál es la distancia mínima de seguimiento recomendada en vía urbana?",
                new String[]{"1 segundo", "2-3 segundos", "10 segundos"}, 1));
        preguntas.add(new Pregunta("Un semáforo en amarillo intermitente significa:",
                new String[]{"Detenerse totalmente", "Avanzar con precaución", "Vía preferencial total"}, 1));
        preguntas.add(new Pregunta("¿Qué documento es obligatorio portar al conducir?",
                new String[]{"Licencia de conducción vigente", "Solo la cédula", "Ninguno"}, 0));
        preguntas.add(new Pregunta("El uso del cinturón de seguridad es obligatorio para:",
                new String[]{"Solo el conductor", "Todos los ocupantes", "Solo asientos traseros"}, 1));
    }

    private JComponent buildBody() {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(UIStyle.BG_LIGHT);
        wrap.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        contadorLabel = new JLabel("Pregunta 1 de " + preguntas.size());
        contadorLabel.setFont(UIStyle.FONT_H2);
        contadorLabel.setForeground(UIStyle.GOLD_TEXT);
        topRow.add(contadorLabel, BorderLayout.WEST);

        timerLabel = new JLabel("45:00");
        timerLabel.setFont(UIStyle.FONT_H2);
        timerLabel.setForeground(UIStyle.RED_ALERT);
        topRow.add(timerLabel, BorderLayout.EAST);
        wrap.add(topRow);

        wrap.add(Box.createVerticalStrut(15));

        enunciadoLabel = new JLabel();
        enunciadoLabel.setFont(UIStyle.FONT_BODY);
        enunciadoLabel.setForeground(UIStyle.TEXT_DARK);
        enunciadoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(enunciadoLabel);

        wrap.add(Box.createVerticalStrut(15));
        opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new BoxLayout(opcionesPanel, BoxLayout.Y_AXIS));
        opcionesPanel.setOpaque(false);
        opcionesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(opcionesPanel);

        wrap.add(Box.createVerticalGlue());

        JButton siguiente = UIStyle.primaryButton("Siguiente / Enviar Examen");
        siguiente.setAlignmentX(Component.LEFT_ALIGNMENT);
        siguiente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        siguiente.addActionListener(e -> onSiguiente());
        wrap.add(siguiente);

        return wrap;
    }

    private void mostrarPregunta() {
        Pregunta p = preguntas.get(indice);
        contadorLabel.setText("Pregunta " + (indice + 1) + " de " + preguntas.size());
        enunciadoLabel.setText("<html><body style='width:380px'>" + p.enunciado + "</body></html>");

        opcionesPanel.removeAll();
        grupo = new ButtonGroup();
        for (String op : p.opciones) {
            JRadioButton rb = new JRadioButton(op);
            rb.setFont(UIStyle.FONT_BODY);
            rb.setOpaque(false);
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
            grupo.add(rb);
            opcionesPanel.add(rb);
        }
        opcionesPanel.revalidate();
        opcionesPanel.repaint();
    }

    private void onSiguiente() {
        int seleccion = -1;
        Enumeration<AbstractButton> elems = grupo.getElements();
        int i = 0;
        while (elems.hasMoreElements()) {
            if (elems.nextElement().isSelected()) seleccion = i;
            i++;
        }
        if (seleccion == preguntas.get(indice).correcta) aciertos++;

        indice++;
        if (indice < preguntas.size()) {
            mostrarPregunta();
        } else {
            finalizarExamen();
        }
    }

    private void iniciarTemporizador() {
        timer = new javax.swing.Timer(1000, e -> {
            segundosRestantes--;
            int min = segundosRestantes / 60, seg = segundosRestantes % 60;
            timerLabel.setText(String.format("%02d:%02d", min, seg));
            if (segundosRestantes <= 0) {
                timer.stop();
                finalizarExamen();
            }
        });
        timer.start();
    }

    private void finalizarExamen() {
        if (timer != null) timer.stop();
        double porcentaje = (aciertos * 100.0) / preguntas.size();
        boolean aprobado = porcentaje >= 80.0;
        String mensaje = String.format(
                "Resultado: %.0f%% de respuestas correctas.\nEstado: %s",
                porcentaje, aprobado ? "APROBADO" : "NO APROBADO");
        JOptionPane.showMessageDialog(this, mensaje,
                aprobado ? "Simulacro finalizado" : "Simulacro finalizado - Repasar temas",
                aprobado ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulacroExamenApp().setVisible(true));
    }
}
