import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddBorrowing extends JFrame {

    private JPanel contentPane;
    private JTextField lenderField;
    private JTextField amountField;
    private int userId;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddBorrowing frame = new AddBorrowing(1); // Pass the user ID here
                frame.setUndecorated(true);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AddBorrowing(int userId) {
        this.userId = userId;
        setTitle("Spend Smart || Add Borrowing");
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

        JLabel lenderLabel = new JLabel("Lender");
        lenderLabel.setBounds(391, 180, 131, 16);
        contentPane.add(lenderLabel);

        lenderField = new JTextField();
        lenderField.setBounds(382, 210, 334, 41);
        contentPane.add(lenderField);
        lenderField.setColumns(10);

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setBounds(391, 278, 131, 16);
        contentPane.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(382, 308, 334, 41);
        contentPane.add(amountField);

        JButton addBorrowingBtn = new JButton("Add Borrowing");
        addBorrowingBtn.setBounds(494, 394, 150, 29);
        contentPane.add(addBorrowingBtn);

        addBorrowingBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBorrowing();
            }
        });

        JLabel heading = new JLabel("ADD BORROWING");
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

    private void addBorrowing() {
        String lender = lenderField.getText();
        String amountText = amountField.getText();

        if (lender.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount = Double.parseDouble(amountText);

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO borrowings (usr_id, lender, amount) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, lender);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Borrowing added successfully!");
            dispose();
            Home homeFrame = new Home(userId);
            homeFrame.setUndecorated(true);
            homeFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding borrowing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
