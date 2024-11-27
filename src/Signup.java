import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class Signup extends JFrame {

    private JPanel contentPane;
    private JTextField username;
    private JTextField income;
    private JTextField budget;
    private JPasswordField pass;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Signup frame = new Signup();
                    frame.setUndecorated(true);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Signup() {
        setTitle("Spend Smart || SignUp");
        setBackground(SystemColor.activeCaption);
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

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(27, 116, 317, 314);
        panel.add(lblNewLabel);
        lblNewLabel.setIcon(new ImageIcon(Signup.class.getResource("/images/logo.png")));

        JLabel usernamelbl = new JLabel("UserName");
        usernamelbl.setBounds(391, 97, 131, 16);
        contentPane.add(usernamelbl);

        username = new JTextField();
        username.setBounds(382, 125, 334, 41);
        contentPane.add(username);
        username.setColumns(10);

        JLabel incomelbl = new JLabel("Monthly Income");
        incomelbl.setBounds(391, 180, 131, 16);
        contentPane.add(incomelbl);

        income = new JTextField();
        income.setColumns(10);
        income.setBounds(382, 210, 334, 41);
        contentPane.add(income);

        JLabel budgetlbl = new JLabel("Total Budget");
        budgetlbl.setBounds(391, 278, 131, 16);
        contentPane.add(budgetlbl);

        budget = new JTextField();
        budget.setColumns(10);
        budget.setBounds(382, 308, 334, 41);
        contentPane.add(budget);

        JLabel passlbl = new JLabel("Password");
        passlbl.setBounds(391, 381, 131, 16);
        contentPane.add(passlbl);
        
        pass = new JPasswordField();
        pass.setBounds(382, 416, 334, 41);
        contentPane.add(pass);

        JButton signupbtn = new JButton("SignUp");
        signupbtn.setBounds(494, 494, 117, 29);
        contentPane.add(signupbtn);

        signupbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validate fields
                if (username.getText().isEmpty() || income.getText().isEmpty() || budget.getText().isEmpty() || pass.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String userName = username.getText();
                    String userIncome = income.getText();
                    String userBudget = budget.getText();
                    String userPass = new String(pass.getPassword());

                    // Insert the user data into the database
                    try (Connection conn = DBConnection.getConnection()) {
                        String query = "INSERT INTO users (username, password, income, budget) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, userName);
                        stmt.setString(2, userPass);
                        stmt.setDouble(3, Double.parseDouble(userIncome));
                        stmt.setDouble(4, Double.parseDouble(userBudget));

                        int rowsInserted = stmt.executeUpdate();
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(null, "Sign up successful!");
                            // Close the current Signup form
                            dispose();
                            // Open the Login form
                            Login loginFrame = new Login();
                            loginFrame.setUndecorated(true);
                            loginFrame.setVisible(true);
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error saving data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JLabel Heading = new JLabel("SIGN UP");
        Heading.setFont(new Font("Waseem", Font.BOLD | Font.ITALIC, 23));
        Heading.setBounds(501, 33, 177, 29);
        contentPane.add(Heading);

        JLabel loginlink = new JLabel("Already Have Account? Login");
        loginlink.setForeground(Color.BLUE);
        loginlink.setBounds(461, 535, 202, 16);
        contentPane.add(loginlink);

        loginlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Close the current Signup form
                dispose();
                // Open the Login form
                Login loginFrame = new Login();
                loginFrame.setUndecorated(true);
                loginFrame.setVisible(true);
            }
        });

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
}