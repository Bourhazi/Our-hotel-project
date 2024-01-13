package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JTextField userName;
    JPasswordField passwordt;
    JButton login, cancel;

    Login() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel user = new JLabel("Username:");
        user.setBounds(40, 50, 100, 30);
        add(user);

        userName = new JTextField();
        userName.setBounds(160, 50, 150, 30);
        add(userName);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 100, 100, 30);
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(passwordLabel);

        passwordt = new JPasswordField();
        passwordt.setBounds(160, 100, 150, 30);
        add(passwordt);

        login = new JButton("Login");
        login.setBounds(40, 150, 130, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        add(login);
        login.addActionListener(this);

        cancel = new JButton("Cancel");
        cancel.setBounds(180, 150, 130, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        add(cancel);
        cancel.addActionListener(this);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/profilelogo.jpg"));
        Image i2 = i1.getImage().getScaledInstance(230, 230, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(330, 0, 230, 230);
        add(image);

        setBounds(500, 200, 600, 300);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String username = userName.getText();
            char[] passwordChars = passwordt.getPassword();
            String password = new String(passwordChars);

            try {
                Conn c = new Conn();

                String query = "select * from user where username='" + username + "'and password='" + password + "'";
                ResultSet rs = c.s.executeQuery(query);

                if (rs.next()) {
                    setVisible(false);
                    new Dashboard();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                    setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String args[]) {
        new Login();
    }
}
