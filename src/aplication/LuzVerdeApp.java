import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * LuzVerdeApp
 * Recreación visual (solo maquetación/estilo, sin lógica de negocio) de la
 * landing page del Centro de Enseñanza Automovilística "Luz Verde".
 *
 * Pensado para importarse en un proyecto NetBeans: crea un nuevo proyecto
 * Java (Ant o Maven), coloca este archivo en el paquete por defecto (o
 * ajusta el "package" si usas uno) y ejecútalo (Main Class = LuzVerdeApp).
 */
public class LuzVerdeApp extends JFrame {

    // ---------- Paleta de colores basada en la referencia ----------
    static final Color OLIVE          = new Color(0xA7, 0x9A, 0x5C); // banner/footer
    static final Color OLIVE_DARK     = new Color(0x8E, 0x82, 0x4B);
    static final Color GOLD_TEXT      = new Color(0x9C, 0x8A, 0x3A);
    static final Color BG_LIGHT       = new Color(0xF1, 0xF0, 0xEC);
    static final Color WHITE          = Color.WHITE;
    static final Color CARD_BORDER    = new Color(0xE3, 0xC6, 0x4A);
    static final Color TEXT_DARK      = new Color(0x33, 0x33, 0x33);
    static final Color TEXT_GRAY      = new Color(0x7A, 0x7A, 0x7A);
    static final Color GREEN_WA       = new Color(0x25, 0xA6, 0x4E);
    static final Color STAR_GOLD      = new Color(0xF5, 0xB3, 0x01);
    static final Color RED_LIGHT      = new Color(0xE5, 0x39, 0x35);
    static final Color YELLOW_LIGHT   = new Color(0xFD, 0xD8, 0x35);
    static final Color GREEN_LIGHT    = new Color(0x43, 0xA0, 0x47);
    static final Color MAP_GREEN      = new Color(0xDD, 0xEC, 0xD6);

    static final Font FONT_LOGO   = new Font("Serif", Font.BOLD, 22);
    static final Font FONT_NAV    = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FONT_H1     = new Font("SansSerif", Font.BOLD, 20);
    static final Font FONT_H2     = new Font("SansSerif", Font.BOLD, 16);
    static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 11);

    public LuzVerdeApp() {
        super("Luz Verde - Centro de Enseñanza Automovilística");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_LIGHT);

        content.add(buildHeader());
        content.add(buildHeroBanner());
        content.add(buildCoursesSection());
        content.add(buildLocationSection());
        content.add(buildReviewsSection());
        content.add(buildFooter());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        setContentPane(scroll);

        setSize(760, 950);
        setLocationRelativeTo(null);
    }

    // ================= HEADER =================
    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, CARD_BORDER));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        header.setPreferredSize(new Dimension(760, 60));

        // Logo (semáforo + texto)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        logoPanel.setOpaque(false);
        logoPanel.add(new TrafficLightIcon(18, 40));
        JLabel logoText = new JLabel("Luz Verde");
        logoText.setFont(FONT_LOGO);
        logoText.setForeground(TEXT_DARK);
        logoPanel.add(logoText);
        header.add(logoPanel, BorderLayout.WEST);

        // Nav
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        nav.setOpaque(false);
        String[] items = {"Inicio", "Cursos", "CALE", "Sedes", "Flota", "Nosotros"};
        for (int i = 0; i < items.length; i++) {
            JLabel l = new JLabel(items[i]);
            l.setFont(FONT_NAV);
            l.setForeground(i == 0 ? TEXT_DARK : TEXT_GRAY);
            if (i == 0) {
                l.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GOLD_TEXT));
            }
            nav.add(l);
        }
        header.add(nav, BorderLayout.CENTER);

        // Redes sociales
        JPanel social = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        social.setOpaque(false);
        social.setBorder(new EmptyBorder(0, 0, 0, 15));
        for (String s : new String[]{"f", "\u25CE", "\u266A"}) { // fb, ig, tiktok (símbolos aprox.)
            JLabel icon = new JLabel(s);
            icon.setFont(new Font("SansSerif", Font.BOLD, 14));
            icon.setForeground(TEXT_DARK);
            social.add(icon);
        }
        header.add(social, BorderLayout.EAST);

        return header;
    }

    /** Icono de semáforo (rojo, amarillo, verde) dibujado a mano */
    static class TrafficLightIcon extends JComponent {
        int w, h;
        TrafficLightIcon(int w, int h) {
            this.w = w; this.h = h;
            setPreferredSize(new Dimension(w, h));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(TEXT_DARK);
            g2.fillRoundRect(0, 0, w, h, 4, 4);
            int d = w - 6;
            g2.setColor(RED_LIGHT);
            g2.fillOval(3, 3, d, d);
            g2.setColor(YELLOW_LIGHT);
            g2.fillOval(3, h / 2 - d / 2, d, d);
            g2.setColor(GREEN_LIGHT);
            g2.fillOval(3, h - d - 3, d, d);
        }
    }

    // ================= HERO BANNER =================
    private JComponent buildHeroBanner() {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setBackground(BG_LIGHT);
        wrap.setBorder(new EmptyBorder(20, 0, 10, 0));

        JPanel banner = new JPanel(new BorderLayout(15, 0));
        banner.setBackground(OLIVE);
        banner.setPreferredSize(new Dimension(680, 150));
        banner.setBorder(new EmptyBorder(15, 15, 15, 15));

        JComponent img = new GradientImagePlaceholder(220, 120, new Color(0x2b2b3a), new Color(0xe08a3c));
        banner.add(img, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("A2 \u2022 B1 \u2022 C1 \u2022 RC1");
        title.setFont(FONT_H2);
        title.setForeground(WHITE);
        JTextArea desc = new JTextArea(
            "\u00a1Pasa de pasajero a conductor! Tu licencia est\u00e1 a un solo paso. "
          + "Aprende a tu ritmo en la categor\u00eda que necesites con instructores "
          + "certificados listos para garantizar tu \u00e9xito.");
        desc.setFont(FONT_BODY);
        desc.setForeground(WHITE);
        desc.setOpaque(false);
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setFocusable(false);
        desc.setMaximumSize(new Dimension(420, 90));

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(desc);
        banner.add(textPanel, BorderLayout.CENTER);

        wrap.add(banner);
        return wrap;
    }

    /** Placeholder simple con degradado para simular una fotografía */
    static class GradientImagePlaceholder extends JComponent {
        Color c1, c2;
        GradientImagePlaceholder(int w, int h, Color c1, Color c2) {
            this.c1 = c1; this.c2 = c2;
            setPreferredSize(new Dimension(w, h));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        }
    }

    // ================= CURSOS =================
    private JComponent buildCoursesSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BG_LIGHT);
        section.setBorder(new EmptyBorder(10, 0, 30, 0));

        JLabel title = new JLabel("Formaci\u00f3n para todas las licencias", SwingConstants.CENTER);
        title.setFont(FONT_H1);
        title.setForeground(GOLD_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(title);
        section.add(Box.createVerticalStrut(15));

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        cards.setOpaque(false);
        String[][] data = {
            {"REC C1", "5h teoria", "0hrs taller", "15h practica"},
            {"A2",     "20h teoria", "5h taller", "10h practica"},
            {"B1",     "20h teoria", "5h taller", "20h practica"},
            {"C1",     "20h teoria", "5h taller", "20h practica"}
        };
        for (String[] d : data) {
            cards.add(new CourseCard(d[0], d[1], d[2], d[3]));
        }
        section.add(cards);
        return section;
    }

    static class CourseCard extends JPanel {
        CourseCard(String name, String s1, String s2, String s3) {
            setLayout(new BorderLayout());
            setBackground(WHITE);
            setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
            setPreferredSize(new Dimension(140, 190));

            JPanel top = new JPanel();
            top.setOpaque(false);
            top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
            top.setBorder(new EmptyBorder(15, 10, 5, 10));

            JLabel title = new JLabel(name, SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 22));
            title.setForeground(TEXT_DARK);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            top.add(title);
            top.add(Box.createVerticalStrut(10));

            // icono simple según el tipo de curso
            JComponent icon = "REC C1".equals(name)
                    ? new SimpleIcon(SimpleIcon.Type.CLOCK)
                    : "A2".equals(name)
                        ? new SimpleIcon(SimpleIcon.Type.MOTO)
                        : new SimpleIcon(SimpleIcon.Type.CAR);
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);
            top.add(icon);
            top.add(Box.createVerticalStrut(8));

            JPanel stats = new JPanel(new GridLayout(1, 3, 4, 0));
            stats.setOpaque(false);
            for (String s : new String[]{s1, s2, s3}) {
                JLabel l = new JLabel("<html><center>" + s.replace(" ", "<br>") + "</center></html>", SwingConstants.CENTER);
                l.setFont(FONT_SMALL);
                l.setForeground(TEXT_GRAY);
                l.setOpaque(true);
                l.setBackground(new Color(0xF0, 0xF0, 0xF0));
                stats.add(l);
            }
            top.add(stats);
            add(top, BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.setOpaque(false);
            bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
            bottom.setBorder(new EmptyBorder(5, 10, 12, 10));

            JButton btn = new JButton("Inscr\u00edbete");
            btn.setFont(FONT_SMALL);
            btn.setForeground(GOLD_TEXT);
            btn.setBackground(WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(110, 26));

            JLabel wa = new JLabel("\u25CF WhatsApp", SwingConstants.CENTER);
            wa.setFont(FONT_SMALL);
            wa.setForeground(GREEN_WA);
            wa.setAlignmentX(Component.CENTER_ALIGNMENT);
            wa.setBorder(new EmptyBorder(6, 0, 0, 0));

            bottom.add(btn);
            bottom.add(wa);
            add(bottom, BorderLayout.SOUTH);
        }
    }

    /** Iconos simples (reloj, moto, carro) dibujados con formas básicas */
    static class SimpleIcon extends JComponent {
        enum Type { CLOCK, MOTO, CAR }
        Type type;
        SimpleIcon(Type t) {
            this.type = t;
            setPreferredSize(new Dimension(40, 30));
            setMaximumSize(new Dimension(40, 30));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(GOLD_TEXT);
            g2.setStroke(new BasicStroke(2f));
            switch (type) {
                case CLOCK:
                    g2.drawOval(10, 3, 20, 20);
                    g2.drawLine(20, 13, 20, 6);
                    g2.drawLine(20, 13, 26, 15);
                    break;
                case MOTO:
                    g2.drawOval(4, 15, 12, 12);
                    g2.drawOval(24, 15, 12, 12);
                    g2.drawLine(10, 21, 24, 12);
                    g2.drawLine(24, 12, 30, 21);
                    break;
                case CAR:
                    g2.drawRoundRect(5, 8, 30, 12, 6, 6);
                    g2.drawOval(8, 18, 8, 8);
                    g2.drawOval(24, 18, 8, 8);
                    break;
            }
        }
    }

    // ================= UBICACIÓN =================
    private JComponent buildLocationSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BG_LIGHT);
        section.setBorder(new EmptyBorder(10, 0, 30, 0));

        JLabel title = new JLabel("Ubicaci\u00f3n Sede Principal", SwingConstants.CENTER);
        title.setFont(FONT_H1);
        title.setForeground(GOLD_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(title);
        section.add(Box.createVerticalStrut(15));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        row.setOpaque(false);

        JPanel mapCol = new JPanel();
        mapCol.setOpaque(false);
        mapCol.setLayout(new BoxLayout(mapCol, BoxLayout.Y_AXIS));
        MapPlaceholder map = new MapPlaceholder(260, 160);
        map.setAlignmentX(Component.CENTER_ALIGNMENT);
        mapCol.add(map);
        mapCol.add(Box.createVerticalStrut(8));

        String[] infoLines = {
            "Sede Principal Cr ##",
            "Horario:",
            "L-V:10:00AM - 6:00PM",
            "S: 9:00AM - 5:00PM",
            "Ll\u00e1manos o escr\u00edbenos:",
            "313 266 8411"
        };
        for (String line : infoLines) {
            JLabel l = new JLabel(line, SwingConstants.CENTER);
            l.setFont(new Font("SansSerif", Font.BOLD, 11));
            l.setForeground(GOLD_TEXT);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            mapCol.add(l);
        }
        row.add(mapCol);

        JComponent building = new GradientImagePlaceholder(260, 190, new Color(0x9fd0e8), new Color(0x3a5f7d));
        row.add(building);

        section.add(row);
        return section;
    }

    static class MapPlaceholder extends JComponent {
        MapPlaceholder(int w, int h) {
            setPreferredSize(new Dimension(w, h));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(MAP_GREEN);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(WHITE);
            g2.setStroke(new BasicStroke(3f));
            g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2 - 20);
            g2.drawLine(getWidth() / 3, 0, getWidth() / 2, getHeight());
            // pin
            int cx = getWidth() / 2, cy = getHeight() / 2 - 10;
            g2.setColor(RED_LIGHT);
            g2.fillOval(cx - 10, cy - 10, 20, 20);
            int[] xs = {cx - 8, cx + 8, cx};
            int[] ys = {cy + 6, cy + 6, cy + 26};
            g2.fillPolygon(xs, ys, 3);
            g2.setColor(WHITE);
            g2.fillOval(cx - 4, cy - 4, 8, 8);
        }
    }

    // ================= RESEÑAS =================
    private JComponent buildReviewsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BG_LIGHT);
        section.setBorder(new EmptyBorder(10, 0, 20, 0));

        JLabel excelente = new JLabel("EXCELENTE", SwingConstants.CENTER);
        excelente.setFont(new Font("SansSerif", Font.BOLD, 16));
        excelente.setForeground(TEXT_DARK);
        excelente.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(excelente);

        StarRow stars = new StarRow(5);
        stars.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(stars);

        JLabel basedOn = new JLabel("A base de 443 rese\u00f1as", SwingConstants.CENTER);
        basedOn.setFont(FONT_SMALL);
        basedOn.setForeground(TEXT_GRAY);
        basedOn.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(basedOn);

        JLabel google = new JLabel("Google", SwingConstants.CENTER);
        google.setFont(new Font("Serif", Font.BOLD, 14));
        google.setForeground(new Color(0x42, 0x85, 0xF4));
        google.setAlignmentX(Component.CENTER_ALIGNMENT);
        google.setBorder(new EmptyBorder(2, 0, 15, 0));
        section.add(google);

        JPanel reviews = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        reviews.setOpaque(false);
        String[][] data = {
            {"Sergio Luis", "Excelente asesor\u00eda, las clases te\u00f3ricas son super claras y las pr\u00e1cticas eran casi que a diario..."},
            {"Luis Pi\u00f1a Ardila", "Excelente metodolog\u00eda de ense\u00f1anza tanto como en la etapa te\u00f3rica y pr\u00e1ctica, siempre pendiente de las m\u00e1s..."},
            {"Dylan Smith", "Una de las mejores escuelas de Barranquilla, excelente servicio, los mejores en calidad - precio, con instructores altamente..."},
            {"Camilo Steven Per...", "Muy buena experiencia, hacen en proceso lo m\u00e1s r\u00e1pido posible y a tu disposici\u00f3n"}
        };
        for (String[] d : data) {
            reviews.add(new ReviewCard(d[0], d[1]));
        }
        section.add(reviews);

        return section;
    }

    static class StarRow extends JComponent {
        int count;
        StarRow(int count) {
            this.count = count;
            setPreferredSize(new Dimension(count * 22, 20));
            setMaximumSize(new Dimension(count * 22, 20));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(STAR_GOLD);
            for (int i = 0; i < count; i++) {
                drawStar(g2, i * 22 + 10, 10, 9);
            }
        }
        void drawStar(Graphics2D g2, int cx, int cy, int r) {
            int points = 5;
            double[] xs = new double[points * 2];
            double[] ys = new double[points * 2];
            for (int i = 0; i < points * 2; i++) {
                double radius = (i % 2 == 0) ? r : r / 2.3;
                double angle = Math.PI / points * i - Math.PI / 2;
                xs[i] = cx + radius * Math.cos(angle);
                ys[i] = cy + radius * Math.sin(angle);
            }
            Path2D path = new Path2D.Double();
            path.moveTo(xs[0], ys[0]);
            for (int i = 1; i < xs.length; i++) path.lineTo(xs[i], ys[i]);
            path.closePath();
            g2.fill(path);
        }
    }

    static class ReviewCard extends JPanel {
        ReviewCard(String name, String text) {
            setLayout(new BorderLayout());
            setBackground(WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(0xE0, 0xE0, 0xE0), 1));
            setPreferredSize(new Dimension(160, 150));

            JPanel top = new JPanel(new BorderLayout(6, 0));
            top.setOpaque(false);
            top.setBorder(new EmptyBorder(8, 8, 4, 8));
            JLabel avatar = new JLabel();
            avatar.setOpaque(true);
            avatar.setBackground(new Color(0xC9, 0xC9, 0xC9));
            avatar.setPreferredSize(new Dimension(24, 24));
            top.add(avatar, BorderLayout.WEST);

            JPanel namePanel = new JPanel();
            namePanel.setOpaque(false);
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
            JLabel nameLbl = new JLabel(name);
            nameLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
            JLabel timeLbl = new JLabel("hace 7 meses");
            timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 9));
            timeLbl.setForeground(TEXT_GRAY);
            namePanel.add(nameLbl);
            namePanel.add(timeLbl);
            top.add(namePanel, BorderLayout.CENTER);
            add(top, BorderLayout.NORTH);

            JPanel mid = new JPanel();
            mid.setOpaque(false);
            mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
            mid.setBorder(new EmptyBorder(0, 8, 4, 8));
            StarRow stars = new StarRow(5);
            stars.setAlignmentX(Component.LEFT_ALIGNMENT);
            mid.add(stars);
            mid.add(Box.createVerticalStrut(4));

            JTextArea body = new JTextArea(text);
            body.setFont(FONT_SMALL);
            body.setForeground(TEXT_DARK);
            body.setLineWrap(true);
            body.setWrapStyleWord(true);
            body.setOpaque(false);
            body.setEditable(false);
            body.setFocusable(false);
            mid.add(body);
            add(mid, BorderLayout.CENTER);

            JLabel leerMas = new JLabel("Leer m\u00e1s");
            leerMas.setFont(FONT_SMALL);
            leerMas.setForeground(TEXT_GRAY);
            leerMas.setBorder(new EmptyBorder(0, 8, 8, 0));
            add(leerMas, BorderLayout.SOUTH);
        }
    }

    // ================= FOOTER =================
    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(OLIVE);
        footer.setPreferredSize(new Dimension(760, 90));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        logoPanel.setOpaque(false);
        logoPanel.add(new TrafficLightIcon(16, 34));
        JLabel logoText = new JLabel("Luz Verde");
        logoText.setFont(new Font("Serif", Font.BOLD, 18));
        logoText.setForeground(WHITE);
        logoPanel.add(logoText);
        footer.add(logoPanel, BorderLayout.WEST);

        JPanel contact = new JPanel();
        contact.setOpaque(false);
        contact.setLayout(new BoxLayout(contact, BoxLayout.Y_AXIS));
        String[] lines = {"\u00bfDudas o inquietudes?", "###@gmail.com", "(numero telef\u00f3nico)", "Bogot\u00e1, Colombia"};
        for (String l : lines) {
            JLabel lbl = new JLabel(l);
            lbl.setFont(FONT_SMALL);
            lbl.setForeground(WHITE);
            contact.add(lbl);
        }
        footer.add(contact, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(OLIVE_DARK);
        wrapper.setPreferredSize(new Dimension(760, 115));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        wrapper.add(footer, BorderLayout.NORTH);

        JLabel copy = new JLabel("\u00a9 Centro de Ense\u00f1anza Automovil\u00edstica Luz Verde S.A.S - 2026", SwingConstants.CENTER);
        copy.setFont(FONT_SMALL);
        copy.setForeground(WHITE);
        copy.setOpaque(true);
        copy.setBackground(new Color(0x6f, 0x66, 0x3c));
        copy.setBorder(new EmptyBorder(8, 0, 8, 0));
        wrapper.add(copy, BorderLayout.SOUTH);

        return wrapper;
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            LuzVerdeApp app = new LuzVerdeApp();
            app.setVisible(true);
        });
    }
}