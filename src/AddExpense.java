import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddExpense extends JFrame {

    private JPanel contentPane;
    private JTextField itemField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField priceField;
    private int userId;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddExpense frame = new AddExpense(1); // Pass the user ID here
                frame.setUndecorated(true);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AddExpense(int userId) {
        this.userId = userId;
        setTitle("Spend Smart || Add Expense");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 759, 618);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.window);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setBounds(5, 6, 365, 584);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblLogo = new JLabel("");
        lblLogo.setBounds(27, 116, 317, 314);
        panel.add(lblLogo);
        lblLogo.setIcon(new ImageIcon(Login.class.getResource("/images/logo.png")));

        JLabel itemLabel = new JLabel("Item");
        itemLabel.setBounds(391, 180, 131, 16);
        contentPane.add(itemLabel);

        itemField = new JTextField();
        itemField.setBounds(382, 210, 334, 41);
        contentPane.add(itemField);
        itemField.setColumns(10);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setBounds(391, 278, 131, 16);
        contentPane.add(categoryLabel);

        categoryField = new JTextField();
        categoryField.setBounds(382, 308, 334, 41);
        contentPane.add(categoryField);

        JLabel quantityLabel = new JLabel("Quantity");
        quantityLabel.setBounds(391, 370, 131, 16);
        contentPane.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(382, 400, 334, 41);
        contentPane.add(quantityField);

        JLabel priceLabel = new JLabel("Price Per Unit");
        priceLabel.setBounds(391, 460, 131, 16);
        contentPane.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(382, 490, 334, 41);
        contentPane.add(priceField);

        JButton addExpenseBtn = new JButton("Add Expense");
        addExpenseBtn.setBounds(494, 550, 150, 29);
        contentPane.add(addExpenseBtn);

        addExpenseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        JLabel heading = new JLabel("ADD EXPENSE");
        heading.setFont(new Font("Waseem", Font.BOLD | Font.ITALIC, 23));
        heading.setBounds(480, 97, 177, 29);
        contentPane.add(heading);

        JLabel close = new JLabel("X");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        close.setFont(new Font("Lucida Grande", Font.PLAIN, 23));
        close.setForeground(Color.RED);
        close.setBounds(727, 6, 15, 21);
        contentPane.add(close);
    }

    private void addExpense() {
        String item = itemField.getText();
        String category = categoryField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();

        if (item.isEmpty() || category.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity = Integer.parseInt(quantityText);
        double price = Double.parseDouble(priceText);
        double totalPrice = quantity * price;

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO expenses (user_id, item_name, category, quantity, price,total_price) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, item);
            stmt.setString(3, category);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, price);
            stmt.setDouble(6, totalPrice);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Expense added successfully!");
            dispose();
            Home homeFrame = new Home(userId);
            homeFrame.setUndecorated(true);
            homeFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding expense!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
