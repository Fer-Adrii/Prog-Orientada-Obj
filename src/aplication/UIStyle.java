import javax.swing.*;
import java.awt.*;

public class UIStyle {
    // Colores
    public static final Color BG_LIGHT = new Color(245, 245, 247);
    public static final Color GOLD_TEXT = new Color(180, 140, 20);
    public static final Color TEXT_DARK = new Color(40, 40, 40);
    public static final Color TEXT_GRAY = new Color(120, 120, 120);
    public static final Color RED_ALERT = new Color(200, 50, 50);
    public static final Color GREEN_OK = new Color(40, 150, 60);
    public static final Color YELLOW_WARN = new Color(210, 160, 20);
    public static final Color WHITE = Color.WHITE;
    public static final Color CARD_BORDER = new Color(220, 220, 225);
    public static final Color OLIVE = new Color(85, 107, 47);

    // Fuentes
    public static final Font FONT_H1 = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FONT_H2 = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);

    // Componentes UI
    public static JPanel buildHeader(String title) {
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 30, 30));
        JLabel lbl = new JLabel(title);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(lbl);
        return header;
    }

    public static JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    public static void styleField(JComponent field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(0, 122, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SMALL);
        btn.setFocusPainted(false);
        return btn;
    }
}