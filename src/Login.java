import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField username;
    private JPasswordField pass;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setUndecorated(true);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Login() {
        setTitle("Spend Smart || Login");
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
        lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/images/logo.png")));

        JLabel usernamelbl = new JLabel("UserName");
        usernamelbl.setBounds(391, 180, 131, 16);
        contentPane.add(usernamelbl);

        username = new JTextField();
        username.setBounds(382, 210, 334, 41);
        contentPane.add(username);
        username.setColumns(10);

        JLabel passlbl = new JLabel("Password");
        passlbl.setBounds(391, 278, 131, 16);
        contentPane.add(passlbl);

        pass = new JPasswordField();
        pass.setBounds(382, 308, 334, 41);
        contentPane.add(pass);

        JButton loginbtn = new JButton("Login");
        loginbtn.setBounds(494, 394, 117, 29);
        contentPane.add(loginbtn);

        loginbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (username.getText().isEmpty() || pass.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(null, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String userName = username.getText();
                    String userPass = new String(pass.getPassword());

                    try (Connection conn = DBConnection.getConnection()) {
                        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, userName);
                        stmt.setString(2, userPass);

                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "Login successful!");
                            dispose();
                            int userId = rs.getInt("id");
                            Home homeFrame = new Home(userId);
                            homeFrame.setUndecorated(true);
                            homeFrame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error accessing the database!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JLabel Heading = new JLabel("LOGIN");
        Heading.setFont(new Font("Waseem", Font.BOLD | Font.ITALIC, 23));
        Heading.setBounds(501, 97, 177, 29);
        contentPane.add(Heading);

        JLabel signupLink = new JLabel("Create a new Account");
        signupLink.setForeground(Color.BLUE);
        signupLink.setBounds(480, 460, 202, 16);
        contentPane.add(signupLink);

        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                Signup signupFrame = new Signup();
                signupFrame.setUndecorated(true);
                signupFrame.setVisible(true);
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
