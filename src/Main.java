import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class BarPanel extends JPanel {
    private int height_bars;
    private int width_bars = 10;

    public BarPanel(int height) {
        this.height_bars = height;
        setPreferredSize(new Dimension(width_bars, height));
    }

    public int getBarHeight() {
        return height_bars;
    }

    public int getBarWidth() {
        return width_bars;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, getHeight() - height_bars, getWidth(), height_bars);
    }
}

public class Main {
    private JFrame frame;
    private JPanel barContainer;
    private List<BarPanel> bars;
    private int number_of_bars = 66;

    public Main() {
        // Erstelle das Hauptfenster
        frame = new JFrame("Sorting Algorithms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Erstelle das Panel zur Anzeige der Balken
        barContainer = new JPanel();
        barContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));

        // Erstelle die Balken
        bars = new ArrayList<>();
        for (int i = 0; i < number_of_bars; i++) { // TODO number_of_bars an Breite des Fensters anpassen
            int height = (int) (Math.random() * 400) + 50;
            BarPanel bar = new BarPanel(height);
            bars.add(bar);
            barContainer.add(bar);
        }
        // Füge das Balken-Panel zum Hauptfenster hinzu
        frame.add(new JScrollPane(barContainer), BorderLayout.CENTER);

        // Erstelle ein Panel für die Buttons
        JPanel buttonPanel = new JPanel();

        // Erstelle den Sortier-Button
        // TODO hier die verschiedenen Sortieralgorithmen einfuegen
        JButton sortButton = new JButton("Sortieren");
        sortButton.addActionListener(e -> sortBars());
        buttonPanel.add(sortButton);

        // Erstelle den Button zum Mischen
        JButton shuffleButton = new JButton("Mischen");
        shuffleButton.addActionListener(e -> shuffleBars());
        buttonPanel.add(shuffleButton);

        // Füge das Button-Panel zum Hauptfenster hinzu
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Zeige das Fenster an
        frame.setVisible(true);
    }

    private void sortBars() {
        // Sortiere die Balken nach ihrer Höhe
        Collections.sort(bars, Comparator.comparingInt(BarPanel::getHeight));

        // Aktualisiere die Anzeige
        barContainer.removeAll();
        for (BarPanel bar : bars) {
            barContainer.add(bar);
        }
        barContainer.revalidate();
        barContainer.repaint();
    }

    private void shuffleBars() {
        // Mische die Balken zufällig
        Collections.shuffle(bars);

        // Aktualisiere die Anzeige
        barContainer.removeAll();
        for (BarPanel bar : bars) {
            barContainer.add(bar);
        }
        barContainer.revalidate();
        barContainer.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}