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

    private boolean isSorting = false;
    private boolean stopSorting = false;
    private long how_long_to_sleep = 150;

    private JComboBox<String> algorithmComboBox;

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

        // Erstelle die Dropdown-Liste für Sortieralgorithmen
        String[] algorithms = { "Bubble Sort", "Selection Sort", "Insertion Sort",
                "Quick Sort", "Merge Sort", "Heap Sort" };
        algorithmComboBox = new JComboBox<>(algorithms);
        buttonPanel.add(algorithmComboBox);

        // Erstelle den Sortier-Button
        JButton sortButton = new JButton("Sortieren");
        sortButton.addActionListener(e -> {
            if (!isSorting) {
                stopSorting = false;
                new Thread(this::animateSortBars).start();
            }
        });
        buttonPanel.add(sortButton);

        // Erstelle den Button zum Mischen
        JButton shuffleButton = new JButton("Mischen");
        shuffleButton.addActionListener(e -> {
            if (isSorting) {
                stopSorting = true;
            }
            shuffleBars();
        });
        buttonPanel.add(shuffleButton);

        // Füge das Button-Panel zum Hauptfenster hinzu
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Zeige das Fenster an
        frame.setVisible(true);
    }
    // Bubble Sort
    private void animateSortBars() {
        isSorting = true;
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        switch (selectedAlgorithm) {
            case "Bubble Sort":
                bubbleSort();
                break;
            case "Selection Sort":
                selectionSort();
                break;
            case "Insertion Sort":
                insertionSort();
                break;
            case "Quick Sort":
                quickSort(0, bars.size() - 1);
                break;
            case "Merge Sort":
                mergeSort();
                break;
            case "Heap Sort":
                heapSort();
                break;
        }
        isSorting = false;
    }
    /* Quadratic Sorting */

    // O(n²)
    private void bubbleSort() {
        for (int i = 0; i < bars.size() - 1; i++) {
            for (int j = 0; j <bars.size() - 1 - i; j++) {
                if (stopSorting) return;
                if (bars.get(j).getBarHeight() > bars.get(j + 1).getBarHeight()) {
                    Collections.swap(bars, j, j + 1);
                    updateBars();
                }
            }
        }
    }
    // O(n²)
    private void selectionSort() {
        int maxIndex = bars.size();
        int insertIndex = 0;
        while (insertIndex < maxIndex) {
            if (stopSorting) return;
            int minPosition = insertIndex;
            for (int i = insertIndex + 1; i < maxIndex; i++) {
                if (bars.get(i).getBarHeight() < bars.get(minPosition).getBarHeight()) {
                    minPosition = i;
                }
            }
            Collections.swap(bars, minPosition, insertIndex);
            insertIndex++;
            updateBars();
        }
    }
    // Average: O(n²)
    private void insertionSort() {
        int i = 1;
        while (i < bars.size()) {
            if (stopSorting) return;
            int j = i;
            while (j > 0 && bars.get(j).getBarHeight() < bars.get(j - 1).getBarHeight()) {
                Collections.swap(bars, j, j - 1);
                j--;
            }
            i++;
            updateBars();
        }
    }

    /* Logarithmic Sorting */

    // Average: O(n * log(n))
    private void quickSort(int left, int right) {
        if (left < right && !stopSorting) {
            int divider = quickSortPartition(left, right);
            quickSort(left, divider - 1);
            quickSort(divider + 1, right);
        }
    }
    private int quickSortPartition(int left, int right) { // Hilfsfunktion für quickSort()
        // daten[] = bars
        // Starte mit j links vom Pivotelement
        int i = left, j = right - 1;
        int pivot = bars.get(right).getBarHeight();

        while (i < j) { // Solange i an j nicht vorbeigelaufen ist
            if (stopSorting) return -1;

            // Suche von links ein Element, welches größer als Pivotelement ist
            while (i < j && bars.get(i).getBarHeight() <= pivot) {
                i++;
            }

            // Suche von rechts ein Element, welches kleiner oder gleich dem Pivotelement ist
            while (j > i && bars.get(j).getBarHeight() > pivot) {
                j--;
            }
            if (bars.get(i).getBarHeight() > bars.get(j).getBarHeight()) {
                Collections.swap(bars, i, j);
                updateBars();
            }
        }
        // Tausche Pivotelement (daten[rechts]) mit neuer endgültiger Position (daten[i])
        // und gib die neue Position des Pivotelements zurück, beende Durchlauf
        if (bars.get(i).getBarHeight() > pivot) {
            Collections.swap(bars, i, right);
            updateBars();
        } else {
            i = right;
        }
        return i;
    }

    private void mergeSort() {}

    private void heapSort() {}

    private void updateBars() {
        barContainer.removeAll();
        for (BarPanel bar : bars) {
            barContainer.add(bar);
        }
        barContainer.revalidate();
        barContainer.repaint();
        try {
            Thread.sleep(how_long_to_sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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