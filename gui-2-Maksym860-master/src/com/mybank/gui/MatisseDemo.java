package com.mybank.gui;

import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;

/**
 * Головна форма графічного інтерфейсу банківської системи MyBank.
 * Створена у стилі Matisse (Netbeans Swing GUI Builder):
 * компонування виконується через GroupLayout, як генерує Matisse.
 *
 * Функціонал:
 *  - вибір клієнта зі списку (JComboBox)
 *  - кнопка Show — показує всі рахунки клієнта
 *  - кнопка Report — загальний звіт по всіх клієнтах
 *  - кнопка About — діалогове вікно з інформацією про програму
 *
 * @author Maksym
 */
public class MatisseDemo extends JFrame {

    // -----------------------------------------------------------------------
    // Компоненти форми (оголошені так само, як генерує Matisse)
    // -----------------------------------------------------------------------

    /** Заголовок над списком клієнтів */
    private JLabel lblClients;

    /** Випадаючий список з іменами клієнтів банку */
    private JComboBox<String> cmbClients;

    /** Кнопка для відображення інформації про обраного клієнта */
    private JButton btnShow;

    /** Кнопка для виведення загального звіту за всіма клієнтами */
    private JButton btnReport;

    /** Кнопка для відображення діалогу "Про програму" */
    private JButton btnAbout;

    /** Текстова панель (HTML) для виводу інформації */
    private JEditorPane editorPane;

    /** Прокручуваний контейнер для текстової панелі */
    private JScrollPane scrollPane;

    // -----------------------------------------------------------------------
    // Конструктор
    // -----------------------------------------------------------------------

    /**
     * Конструктор форми: ініціалізує всі компоненти та налаштовує макет.
     */
    public MatisseDemo() {
        // Ініціалізація компонентів (аналог initComponents() у Matisse)
        initComponents();
        // Заповнення випадаючого списку клієнтами з банку
        populateClients();
    }

    // -----------------------------------------------------------------------
    // initComponents — аналог автозгенерованого методу Matisse
    // -----------------------------------------------------------------------

    /**
     * Ініціалізація та компонування всіх елементів форми.
     * Метод відповідає структурі, яку генерує Netbeans Matisse:
     * використовується GroupLayout для панелі та BorderLayout для фрейму.
     *
     * УВАГА: цей метод не слід редагувати вручну у реальному Matisse-проєкті —
     * він повністю керується GUI Builder.
     */
    private void initComponents() {

        // --- Ініціалізація компонентів ---
        lblClients  = new JLabel("Клієнт:");
        cmbClients  = new JComboBox<>();
        btnShow     = new JButton("Show");
        btnReport   = new JButton("Report");
        btnAbout    = new JButton("About");
        editorPane  = new JEditorPane("text/html", "");
        scrollPane  = new JScrollPane(editorPane);

        // Налаштування текстової панелі
        editorPane.setEditable(false); // Лише для читання
        editorPane.setBackground(new Color(255, 255, 240)); // Світло-жовтий фон
        scrollPane.setPreferredSize(new Dimension(480, 220));

        // Шрифт для мітки
        lblClients.setFont(new Font("SansSerif", Font.BOLD, 13));

        // --- Панель керування (верхня частина вікна) ---
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        // Компонування за допомогою GroupLayout (стиль Matisse)
        GroupLayout layout = new GroupLayout(controlPanel);
        controlPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Горизонтальне компонування: мітка | список | Show | Report | About
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addComponent(lblClients)
                .addComponent(cmbClients, GroupLayout.PREFERRED_SIZE,
                        200, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShow)
                .addComponent(btnReport)
                .addComponent(btnAbout)
        );

        // Вертикальне компонування: всі елементи по центру рядка
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblClients)
                .addComponent(cmbClients)
                .addComponent(btnShow)
                .addComponent(btnReport)
                .addComponent(btnAbout)
        );

        // --- Компонування головного фрейму ---
        setTitle("MyBank — Matisse GUI Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Заборона зміни розміру вікна (вимога завдання)
        getContentPane().setLayout(new BorderLayout(0, 0));
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // --- Обробники подій ---
        btnShow.addActionListener(e -> onShowClicked());
        btnReport.addActionListener(e -> onReportClicked());
        btnAbout.addActionListener(e -> onAboutClicked());

        pack();
        setLocationRelativeTo(null); // Центрування на екрані
    }

    // -----------------------------------------------------------------------
    // Заповнення списку клієнтів
    // -----------------------------------------------------------------------

    /**
     * Заповнює випадаючий список іменами клієнтів, завантажених до банку.
     */
    private void populateClients() {
        cmbClients.removeAllItems();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer c = Bank.getCustomer(i);
            // Формат: "Прізвище, Ім'я"
            cmbClients.addItem(c.getLastName() + ", " + c.getFirstName());
        }
    }

    // -----------------------------------------------------------------------
    // Обробники подій кнопок
    // -----------------------------------------------------------------------

    /**
     * Обробник кнопки "Show".
     * Відображає ім'я обраного клієнта та інформацію про всі його рахунки.
     */
    private void onShowClicked() {
        int idx = cmbClients.getSelectedIndex();
        if (idx < 0) return; // Якщо список порожній — нічого не робити

        Customer c = Bank.getCustomer(idx);

        // Формуємо HTML-вміст з даними клієнта
        StringBuilder sb = new StringBuilder();
        sb.append("<br>&nbsp;<b><span style='font-size:1.6em;'>")
          .append(c.getLastName()).append(", ").append(c.getFirstName())
          .append("</span></b><br><hr>");

        // Перебираємо всі рахунки клієнта
        for (int j = 0; j < c.getNumberOfAccounts(); j++) {
            // Визначаємо тип рахунку: чековий або ощадний
            String type = c.getAccount(j) instanceof CheckingAccount
                    ? "Checking" : "Savings";
            sb.append("&nbsp;&nbsp;<b>Рахунок #").append(j + 1)
              .append("</b> — тип: <i>").append(type).append("</i>")
              .append(" &nbsp;|&nbsp; Баланс: ")
              .append("<b><span style='color:darkred;'>$")
              .append(String.format("%.2f", c.getAccount(j).getBalance()))
              .append("</span></b><br>");
        }

        editorPane.setText(sb.toString());
    }

    /**
     * Обробник кнопки "Report".
     * Виводить у нижній частині вікна зведений звіт за всіма клієнтами
     * у форматі, аналогічному CustomerReport з роботи №8.
     */
    private void onReportClicked() {
        StringBuilder sb = new StringBuilder();
        sb.append("<br>&nbsp;<b><span style='font-size:1.4em;'>")
          .append("ЗВІТ ЗА КЛІЄНТАМИ БАНКУ")
          .append("</span></b><br><hr>");

        // Загальні лічильники для підсумкового рядка
        double totalBalance = 0;

        // Перебираємо всіх клієнтів банку
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer c = Bank.getCustomer(i);
            sb.append("<br>&nbsp;<b>")
              .append(c.getLastName()).append(", ").append(c.getFirstName())
              .append("</b><br>");

            // Перебираємо всі рахунки клієнта
            for (int j = 0; j < c.getNumberOfAccounts(); j++) {
                String type = c.getAccount(j) instanceof CheckingAccount
                        ? "Checking" : "Savings";
                double bal = c.getAccount(j).getBalance();
                totalBalance += bal;

                sb.append("&nbsp;&nbsp;&nbsp;[").append(type).append("]")
                  .append(" Баланс: <span style='color:darkred;'>$")
                  .append(String.format("%.2f", bal))
                  .append("</span><br>");
            }
        }

        // Підсумковий рядок — загальний баланс по всіх рахунках
        sb.append("<hr>&nbsp;<b>Загальний баланс всіх рахунків: ")
          .append("<span style='color:darkblue;'>$")
          .append(String.format("%.2f", totalBalance))
          .append("</span></b>");

        editorPane.setText(sb.toString());
    }

    /**
     * Обробник кнопки "About".
     * Відображає діалогове вікно JOptionPane з інформацією про програму
     * та її розробника.
     */
    private void onAboutClicked() {
        // Використовуємо JOptionPane для діалогу "Про програму"
        JOptionPane.showMessageDialog(
                this,
                "MyBank GUI — Лабораторна робота №4\n\n"
                + "Автор: Maksym\n"
                + "Технологія: Java Swing (Matisse / GroupLayout)\n"
                + "Дані: завантажуються з файлу data/test.dat\n\n"
                + "Курс: Об'єктно-орієнтоване програмування (Java)",
                "Про програму",                          // Заголовок діалогу
                JOptionPane.INFORMATION_MESSAGE           // Іконка інформації
        );
    }

    // -----------------------------------------------------------------------
    // Завантаження даних з файлу
    // -----------------------------------------------------------------------

    /**
     * Зчитує дані клієнтів банку з текстового файлу test.dat.
     *
     * Формат файлу:
     *   перший рядок — кількість клієнтів (ціле число)
     *   далі блоки для кожного клієнта:
     *     Ім'я  Прізвище  КількістьРахунків
     *     тип(S/C)  баланс  додатковий_параметр
     *     ...
     *
     * @param filename шлях до файлу з даними
     */
    private static void loadData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            // Перший рядок — кількість клієнтів
            int numCustomers = Integer.parseInt(br.readLine().trim());

            for (int i = 0; i < numCustomers; i++) {

                // Пропускаємо порожні рядки між блоками
                String line = br.readLine();
                while (line != null && line.trim().isEmpty()) {
                    line = br.readLine();
                }
                if (line == null) break;

                // Розбираємо рядок клієнта: ім'я, прізвище, кількість рахунків
                String[] parts = line.trim().split("\\s+");
                String firstName   = parts[0];
                String lastName    = parts[1];
                int    numAccounts = Integer.parseInt(parts[2]);

                // Додаємо клієнта до банку
                Bank.addCustomer(firstName, lastName);
                int custIdx = Bank.getNumberOfCustomers() - 1;

                // Читаємо рахунки клієнта
                for (int j = 0; j < numAccounts; j++) {
                    String accLine = br.readLine();
                    while (accLine != null && accLine.trim().isEmpty()) {
                        accLine = br.readLine();
                    }
                    if (accLine == null) break;

                    String[] ap      = accLine.trim().split("\\s+");
                    String   accType = ap[0];                      // S або C
                    double   balance = Double.parseDouble(ap[1]);
                    double   extra   = Double.parseDouble(ap[2]);  // ставка або ліміт

                    if ("S".equals(accType)) {
                        // Ощадний рахунок: баланс + відсоткова ставка
                        Bank.getCustomer(custIdx).addAccount(
                                new SavingsAccount(balance, extra));
                    } else if ("C".equals(accType)) {
                        // Чековий рахунок: баланс + ліміт овердрафту
                        Bank.getCustomer(custIdx).addAccount(
                                new CheckingAccount(balance, extra));
                    }
                }
            }

        } catch (IOException ex) {
            // Повідомлення про помилку читання файлу
            System.err.println("Помилка читання файлу: " + ex.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Точка входу
    // -----------------------------------------------------------------------

    /**
     * Головний метод програми.
     * Завантажує дані з файлу та запускає графічний інтерфейс
     * у потоці подій Swing (Event Dispatch Thread).
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        // Завантажуємо дані клієнтів з файлу test.dat
        loadData("./data/test.dat");

        // Запускаємо GUI у EDT — обов'язкова вимога для Swing-додатків
        SwingUtilities.invokeLater(() -> {
            new MatisseDemo().setVisible(true);
        });
    }
}
