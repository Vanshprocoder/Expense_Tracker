import java.awt.Component;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import javax.swing.JOptionPane;

public class Home extends JFrame {

    private JPanel contentPane;
    private JTextField totalbudget;
    private JTextField budgetleft;
    private JTable expenseTable;
    private JTable borrowingTable;
    private int userId;

    public Home(int userId) {
        this.userId = userId;
        initialize();
        fetchData();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                int exampleUserId = 1; // Replace with actual user ID for testing
                Home frame = new Home(exampleUserId);
                frame.setUndecorated(true);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 893, 617);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel navbar = new JPanel();
        navbar.setBackground(SystemColor.textHighlight);
        navbar.setBounds(6, 6, 881, 41);
        contentPane.add(navbar);
        navbar.setLayout(null);

        JButton homebtn = new JButton("Home");
        homebtn.setBounds(322, 6, 117, 29);
        homebtn.addActionListener(e -> {
            Home homeFrame = new Home(userId);
            homeFrame.setVisible(true);
            dispose();
        });
        navbar.add(homebtn);

        JButton expensebtn = new JButton("Add Expenses");
        expensebtn.setBounds(456, 6, 117, 29);
        expensebtn.addActionListener(e -> {
            AddExpense addExpenseFrame = new AddExpense(userId);
            addExpenseFrame.setVisible(true);
            dispose();
        });
        navbar.add(expensebtn);

        JButton borrowingsbtn = new JButton("Add Borrowings");
        borrowingsbtn.setBounds(584, 6, 159, 29);
        borrowingsbtn.addActionListener(e -> {
            AddBorrowing addBorrowingFrame = new AddBorrowing(userId);
            addBorrowingFrame.setVisible(true);
            dispose();
        });
        navbar.add(borrowingsbtn);

        JButton Logoutbtn = new JButton("Logout");
        Logoutbtn.setBounds(746, 6, 117, 29);
        Logoutbtn.addActionListener(e -> {
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
            dispose();
        });
        navbar.add(Logoutbtn);

        JLabel logo = new JLabel("Spend Smart");
        logo.setFont(new Font("Monaco", Font.BOLD, 20));
        logo.setBounds(24, 11, 171, 24);
        navbar.add(logo);

        JLabel budgetlbl = new JLabel("Total Budget:");
        budgetlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 19));
        budgetlbl.setBounds(37, 79, 130, 30);
        contentPane.add(budgetlbl);

        totalbudget = new JTextField();
        totalbudget.setBounds(172, 77, 141, 41);
        contentPane.add(totalbudget);
        totalbudget.setColumns(10);

        JLabel lblBudgetLeft = new JLabel("Budget Left:");
        lblBudgetLeft.setFont(new Font("Lucida Grande", Font.PLAIN, 19));
        lblBudgetLeft.setBounds(392, 79, 130, 30);
        contentPane.add(lblBudgetLeft);

        budgetleft = new JTextField();
        budgetleft.setColumns(10);
        budgetleft.setBounds(537, 77, 141, 41);
        contentPane.add(budgetleft);

        // Expense Table
        String[] expenseColumns = {"ID", "Item", "Category", "Quantity", "Price", "Total Price"};
        DefaultTableModel expenseModel = new DefaultTableModel(expenseColumns, 0);
        expenseTable = new JTable(expenseModel);
        JScrollPane expenseScrollPane = new JScrollPane(expenseTable);
        expenseScrollPane.setBounds(37, 145, 700, 150);
        contentPane.add(expenseScrollPane);

        // Borrowing Table with "Mark as Paid" button
        String[] borrowingColumns = {"ID", "Lender", "Amount", "Action"};
        DefaultTableModel borrowingModel = new DefaultTableModel(borrowingColumns, 0);
        borrowingTable = new JTable(borrowingModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only the "Action" column is editable
            }
        };

        TableColumn actionColumn = borrowingTable.getColumnModel().getColumn(3);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(this)); // Pass the Home frame reference here

        JScrollPane borrowingScrollPane = new JScrollPane(borrowingTable);
        borrowingScrollPane.setBounds(37, 320, 700, 150);
        contentPane.add(borrowingScrollPane);
    }

    private void fetchData() {
        double totalBudget = getTotalBudget(userId);
        totalbudget.setText(String.valueOf(totalBudget));

        double totalExpenses = getTotalExpenses(userId);
        double budgetLeft = totalBudget - totalExpenses;
        budgetleft.setText(String.valueOf(budgetLeft));

        fetchExpenses(userId);
        fetchBorrowings(userId);
    }

    private double getTotalBudget(int userId) {
        double totalBudget = 0.0;
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT budget FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalBudget = rs.getDouble("budget");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalBudget;
    }

    private double getTotalExpenses(int userId) {
        double totalExpenses = 0.0;
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT SUM(total_price) AS total FROM expenses WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalExpenses = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalExpenses;
    }

    private void fetchExpenses(int userId) {
        DefaultTableModel model = (DefaultTableModel) expenseTable.getModel();
        model.setRowCount(0); // Clear existing rows
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM expenses WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String item = rs.getString("item_name");
                String category = rs.getString("category");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double totalPrice = rs.getDouble("total_price");
                model.addRow(new Object[]{id, item, category, quantity, price, totalPrice});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchBorrowings(int userId) {
        DefaultTableModel model = (DefaultTableModel) borrowingTable.getModel();
        model.setRowCount(0); // Clear existing rows
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM borrowings WHERE usr_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String lender = rs.getString("lender");
                double amount = rs.getDouble("amount");
                model.addRow(new Object[]{id, lender, amount, "Mark as Paid"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markAsPaid(int borrowingId) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to mark this borrowing as paid?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String query = "DELETE FROM borrowings WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, borrowingId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Borrowing marked as paid.");
                fetchBorrowings(userId); // Refresh the table after deletion
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

// Button Renderer Class
class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setText("Mark as Paid");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

// Button Editor Class
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean clicked;
    private int borrowingId; // Store the borrowing ID
    private Home homeFrame;  // Reference to the Home frame to call `markAsPaid`

    public ButtonEditor(Home homeFrame) {
        super(new JTextField());
        this.button = new JButton("Mark as Paid");
        this.homeFrame = homeFrame;

        // Action Listener for the button
        this.button.addActionListener(e -> {
            if (clicked) {
                homeFrame.markAsPaid(borrowingId); // Mark borrowing as paid when clicked
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        borrowingId = (int) table.getValueAt(row, 0); // Get the borrowing ID from the first column
        clicked = true; // Track if the button was clicked
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return clicked;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false; // Reset the clicked state
        return super.stopCellEditing();
    }
}
