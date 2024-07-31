import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

class BarPanel extends JPanel {
    private int height;
    private final int width_bars = 10;

    public BarPanel(int height) {
        this.height = height;
        setPreferredSize(new Dimension(width_bars, height));
    }

    public int getBarHeight() {
        return height;
    }

    public void setBarHeight(int height) {
        this.height = height;
        setPreferredSize(new Dimension(width_bars, height));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, getHeight() - height, getWidth(), height);
    }
}

public class Main {
    private final JPanel barContainer;
    private final List<BarPanel> bars;
    private final int number_of_bars = 55;
    private final JComboBox<String> algorithmComboBox;

    private boolean isSorting = false;
    private final long how_long_to_sleep = 150;

    private Thread sortingThread;

    public Main() {
        // Erstelle das Hauptfenster
        JFrame frame = new JFrame("Sorting Algorithms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        // Erstelle das Panel zur Anzeige der Balken
        barContainer = new JPanel();
        barContainer.setLayout(new BoxLayout(barContainer, BoxLayout.X_AXIS));
        barContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Erstelle die Balken
        bars = new ArrayList<>();
        for (int i = 0; i < number_of_bars; i++) {
            int height = (int) (Math.random() * 400) + 50;
            BarPanel bar = new BarPanel(height);
            bars.add(bar);
            barContainer.add(bar);
            barContainer.add(Box.createRigidArea(new Dimension(3, 0)));
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
                isSorting = true;
                sortingThread = new Thread(this::animateSortBars);
                sortingThread.start();
            }
        });
        buttonPanel.add(sortButton);

        // Erstelle den Button zum Mischen
        JButton shuffleButton = new JButton("Mischen");
        shuffleButton.addActionListener(e -> {
            if (isSorting) {
                sortingThread.interrupt();
                isSorting = false;
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
        //isSorting = true;
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        try {
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
                    mergeSort(0, bars.size() - 1);
                    break;
                case "Heap Sort":
                    heapSort();
                    break;
                case null:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + selectedAlgorithm);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        isSorting = false;
    }

    /* Quadratic Sorting */

    // O(n²)
    private void bubbleSort() throws InterruptedException {
        for (int i = 0; i < bars.size() - 1; i++) {
            for (int j = 0; j <bars.size() - 1 - i; j++) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                if (bars.get(j).getBarHeight() > bars.get(j + 1).getBarHeight()) {
                    Collections.swap(bars, j, j + 1);
                    updateBars();
                }
            }
        }
    }
    // O(n²)
    private void selectionSort() throws InterruptedException {
        int maxIndex = bars.size();
        int insertIndex = 0;
        while (insertIndex < maxIndex) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
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
    private void insertionSort() throws InterruptedException {
        int i = 1;
        while (i < bars.size()) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
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
    private void quickSort(int left, int right) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        if (left < right) {
            int divider = quickSortPartition(left, right);
            quickSort(left, divider - 1);
            quickSort(divider + 1, right);
        }
    }
    private int quickSortPartition(int left, int right) throws InterruptedException { // Hilfsfunktion für quickSort()
        // daten[] = bars
        // Starte mit j links vom Pivotelement
        int i = left, j = right - 1;
        int pivot = bars.get(right).getBarHeight();

        while (i < j) { // Solange i an j nicht vorbeigelaufen ist
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
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
    // O(n * log(n))
    private void mergeSort(int left, int right) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(left, middle);
            mergeSort(middle + 1, right);
            merge(left, middle, right);
        }
    }
    private void merge(int left, int middle, int right) throws InterruptedException {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        List<Integer> leftHeights = new ArrayList<>(n1);
        List<Integer> rightHeights = new ArrayList<>(n2);

        for (int i = 0; i < n1; i++) {
            leftHeights.add(bars.get(left + i).getBarHeight());
        }
        for (int j = 0; j < n2; j++) {
            rightHeights.add(bars.get(middle + 1 + j).getBarHeight());
        }

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (leftHeights.get(i) <= rightHeights.get(j)) {
                bars.get(k).setBarHeight(leftHeights.get(i));
                i++;
            } else {
                bars.get(k).setBarHeight(rightHeights.get(j));
                j++;
            }
            k++;
            updateBars();
        }

        while (i < n1) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            bars.get(k).setBarHeight(leftHeights.get(i));
            i++; k++;
            updateBars();
        }

        while (j < n2) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            bars.get(k).setBarHeight(rightHeights.get(j));
            j++; k++;
            updateBars();
        }
    }


    private void heapSort() throws InterruptedException {

    }

    private void updateBars() throws InterruptedException {
        try {
            SwingUtilities.invokeAndWait(() -> {
                barContainer.removeAll();
                for (BarPanel bar : bars) {
                    barContainer.add(bar);
                    barContainer.add(Box.createRigidArea(new Dimension(3, 0)));
                }
                barContainer.revalidate();
                barContainer.repaint();
            });
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(how_long_to_sleep);
    }

    private void shuffleBars() {
        // Mische die Balken zufällig
        Collections.shuffle(bars);

        // Aktualisiere die Anzeige
        barContainer.removeAll();
        for (BarPanel bar : bars) {
            barContainer.add(bar);
            barContainer.add(Box.createRigidArea(new Dimension(3, 0)));
        }
        barContainer.revalidate();
        barContainer.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}