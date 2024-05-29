import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Book {
    String isbn;
    double price;
    String author;
    String title;

    public Book(String isbn, double price, String author, String title) {
        this.isbn = isbn;
        this.price = price;
        this.author = author;
        this.title = title;
    }
}

class GUI extends JFrame {

    private JTable table;
    private ArrayList<Book> books = new ArrayList<>();

    public GUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Knihovna");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        JPanel topPanel = new JPanel();
        JButton addButton = new JButton("Přidat knihu");
        JButton deleteButton = new JButton("Smazat knihu");
        JButton exportButton = new JButton("Exportovat do textového souboru");
        JButton calculateButton = new JButton("Spočítat průměrnou cenu");
        topPanel.add(addButton);
        topPanel.add(deleteButton);
        topPanel.add(exportButton);
        topPanel.add(calculateButton);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ISBN", "Cena", "Autor", "Název"};
        Object[][] data = new Object[books.size()][4];

        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).isbn;
            data[i][1] = books.get(i).price;
            data[i][2] = books.get(i).author;
            data[i][3] = books.get(i).title;
        }

        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = JOptionPane.showInputDialog("Zadejte ISBN:");
                double price = Double.parseDouble(JOptionPane.showInputDialog("Zadejte cenu:"));
                String author = JOptionPane.showInputDialog("Zadejte autora:");
                String title = JOptionPane.showInputDialog("Zadejte název:");
                books.add(new Book(isbn, price, author, title));
                updateTable();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow!= -1) {
                    books.remove(selectedRow);
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Nebyl vybrán žádný řádek.");
                }
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToFile();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAveragePrice();
            }
        });

        setVisible(true);
    }

    private void updateTable() {
        String[] columnNames = {"ISBN", "Cena", "Autor", "Název"};
        Object[][] data = new Object[books.size()][4];

        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).isbn;
            data[i][1] = books.get(i).price;
            data[i][2] = books.get(i).author;
            data[i][3] = books.get(i).title;
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void exportToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book book : books) {
                writer.write(book.isbn + "," + book.price + "," + book.author + "," + book.title + "\n");
            }
            JOptionPane.showMessageDialog(null, "Údaje byly úspěšně exportovány do souboru books.txt.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Chyba při exportu údajů: " + e.getMessage());
        }
    }

    private void calculateAveragePrice() {
        double totalPrice = 0;

        for (Book book : books) {
            totalPrice += book.price;
        }
        double averagePrice = totalPrice / books.size();

        JOptionPane.showMessageDialog(null, "Průměrná cena všech knih je: " + averagePrice + "\n" +
                "Celková cena knih je: " + totalPrice);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}