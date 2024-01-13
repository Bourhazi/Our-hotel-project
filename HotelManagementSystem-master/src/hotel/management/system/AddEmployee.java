package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddEmployee extends JFrame implements ActionListener {
    JTextField namet, aget, emailt, phonet, salaryt, cinpassportt;
    JRadioButton male, female;
    JComboBox jobc;
    JButton submit, cancel;

    AddEmployee() {
        setLayout(null);

        JLabel name = new JLabel("Name:");
        name.setBounds(50, 40, 70, 30);
        name.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(name);

        namet = new JTextField();
        namet.setBounds(130, 40, 150, 30);
        add(namet);

        JLabel age = new JLabel("Age:");
        age.setBounds(50, 80, 70, 30);
        age.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(age);

        aget = new JTextField();
        aget.setBounds(130, 80, 150, 30);
        add(aget);

        JLabel gender = new JLabel("Gender:");
        gender.setBounds(50, 130, 70, 30);
        gender.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(gender);

        male = new JRadioButton("Male");
        male.setBounds(130, 130, 70, 30);
        male.setBackground(Color.WHITE);
        add(male);

        female = new JRadioButton("Female");
        female.setBounds(200, 130, 70, 30);
        female.setBackground(Color.WHITE);
        add(female);

        ButtonGroup bg = new ButtonGroup();
        bg.add(male);
        bg.add(female);

        JLabel job = new JLabel("Job:");
        job.setBounds(50, 180, 70, 30);
        job.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(job);

        String[] str = {"Front Desk Clerks", "Porters", "Housekeeping", "Kitchen Staff", "Room Service", "Chefs", "Waiters", "Manager", "Accountant"};
        jobc = new JComboBox(str);
        jobc.setBounds(130, 180, 150, 30);
        jobc.setBackground(Color.WHITE);
        add(jobc);

        JLabel salary = new JLabel("Salary:");
        salary.setBounds(50, 230, 70, 30);
        salary.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(salary);

        salaryt = new JTextField();
        salaryt.setBounds(130, 230, 150, 30);
        add(salaryt);

        JLabel phone = new JLabel("Phone:");
        phone.setBounds(50, 280, 70, 30);
        phone.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(phone);

        phonet = new JTextField();
        phonet.setBounds(130, 280, 150, 30);
        add(phonet);

        JLabel email = new JLabel("Email:");
        email.setBounds(50, 330, 70, 30);
        email.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(email);

        emailt = new JTextField();
        emailt.setBounds(130, 330, 150, 30);
        add(emailt);

        JLabel cinPassportLabel = new JLabel("CIN/Passport:");
        cinPassportLabel.setBounds(50, 380, 150, 30);
        cinPassportLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(cinPassportLabel);

        cinpassportt = new JTextField();
        cinpassportt.setBounds(160, 380, 120, 30);
        add(cinpassportt);

        submit = new JButton("SUBMIT");
        submit.setBounds(180, 440, 100, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        add(submit);
        submit.addActionListener(this);

        cancel = new JButton("CANCEL");
        cancel.setBounds(50, 440, 100, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        add(cancel);
        cancel.addActionListener(this);

        setBounds(250, 100, 850, 540);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String name = namet.getText();
            String age = aget.getText();
            String salary = salaryt.getText();
            String phone = phonet.getText();
            String email = emailt.getText();
            String cinPassport = cinpassportt.getText();
            String gender = male.isSelected() ? "Male" : "Female";
            String job = (String) jobc.getSelectedItem();
               
            if (name.isEmpty() || age.isEmpty() || salary.isEmpty() || phone.isEmpty() || email.isEmpty() || cinPassport.isEmpty() || gender.isEmpty()
                 || job.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Input should not be empty");
            }else{ 
            try {
                Conn c = new Conn();
                String query = "INSERT INTO employee(name, age, salary, phone, email, cin_passport, gender, job) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);

                pstmt.setString(1, name);
                pstmt.setInt(2, Integer.parseInt(age));
                pstmt.setFloat(3, Float.parseFloat(salary));
                pstmt.setString(4, phone);
                pstmt.setString(5, email);
                pstmt.setString(6, cinPassport);
                pstmt.setString(7, gender);
                pstmt.setString(8, job);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee Added");
                setVisible(false);
            } catch (SQLException e) {
                
                if (e.getErrorCode() == 1062) {
                    JOptionPane.showMessageDialog(null, "CIN/Passport already exists.");
                } else {
                    JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
             }else if (ae.getSource() == cancel) {
            setVisible(false);
        }
        
    }

    public static void main(String args[]) {
        new AddEmployee();
    }
}


