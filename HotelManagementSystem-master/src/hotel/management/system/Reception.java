package hotel.management.system;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Reception extends JFrame implements ActionListener {
    JButton rooms, allEmployee, customers, reservationInfo, logout;

    Reception() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        rooms = new JButton("Rooms");
        rooms.setBounds(10, 70, 200, 30);
        rooms.setBackground(Color.BLACK);
        rooms.setForeground(Color.WHITE);
        rooms.addActionListener(this);
        add(rooms);

        allEmployee = new JButton("All Employees");
        allEmployee.setBounds(10, 110, 200, 30);
        allEmployee.setBackground(Color.BLACK);
        allEmployee.setForeground(Color.WHITE);
        allEmployee.addActionListener(this);
        add(allEmployee);

        customers = new JButton("Customers");
        customers.setBounds(10, 150, 200, 30);
        customers.setBackground(Color.BLACK);
        customers.setForeground(Color.WHITE);
        customers.addActionListener(this);
        add(customers);

        reservationInfo = new JButton("Reservation Info");
        reservationInfo.setBounds(10, 190, 200, 30);
        reservationInfo.setBackground(Color.BLACK);
        reservationInfo.setForeground(Color.WHITE);
        reservationInfo.addActionListener(this);
        add(reservationInfo);

        logout = new JButton("Logout");
        logout.setBounds(10, 230, 200, 30);
        logout.setBackground(Color.BLACK);
        logout.setForeground(Color.WHITE);
        logout.addActionListener(this);
        add(logout);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/reception.jpg"));
        Image i2 = i1.getImage().getScaledInstance(600, 500, Image.SCALE_DEFAULT);
ImageIcon i3 = new ImageIcon(i2);
JLabel image = new JLabel(i3);
image.setBounds(250, 30, 600, 500);
add(image);
    setBounds(350, 200, 900, 600);
    setVisible(true);
}

public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == rooms) {
        setVisible(false);
        new Room();
    } else if (ae.getSource() == allEmployee) {
        setVisible(false);
        new EmployeeInfo();
    } else if (ae.getSource() == customers) {
        setVisible(false);
        new CustomerInfo();
    } else if (ae.getSource() == reservationInfo) {
        setVisible(false);
        new ReservationInfo(); // Assurez-vous que cette classe existe
    } else if (ae.getSource() == logout) {
        setVisible(false);
        new Dashboard();
        new Login();
        //System.exit(0);
    }
}

public static void main(String[] args) {
    new Reception();
}
}
