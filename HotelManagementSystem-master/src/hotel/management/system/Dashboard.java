package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener{
    JMenuItem addEmployee,addRooms,addCategory,reception;
   Dashboard(){
       setBounds(0,0,1366,700);
       
       setLayout(null);
       
       ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("icons/HotelBack.jpg"));
       Image i2=i1.getImage().getScaledInstance(1366,700,Image.SCALE_DEFAULT);
       ImageIcon i3=new ImageIcon(i2);
       JLabel image=new JLabel(i3);
       image.setBounds(0,0,1366,700);
       add(image);
       
       
       JLabel text=new JLabel("Hotel Desktop Application");
       text.setBounds(300,60,1000,50);
       text.setFont(new Font("Tahoma",Font.PLAIN,55));
       text.setForeground(Color.WHITE);
       image.add(text);
       
       JMenuBar mb=new JMenuBar();
       mb.setBounds(0,0,1366,30);
       image.add(mb);
       
       JMenu hotel=new JMenu("HOTEL MANAGEMENT");
//       hotel.setForeground(Color.RED);
       mb.add(hotel);
       
        reception=new JMenuItem("RECEPTION");
       hotel.add(reception);
       reception.addActionListener(this);
       
         JMenu admin=new JMenu("ADMIN");
//       hotel.setForeground(Color.GREEN);
       mb.add(admin);
       
       addEmployee=new JMenuItem("ADD EMPLOYEE");
       admin.add(addEmployee);
       addEmployee.addActionListener(this);
       
       addCategory=new JMenuItem("ADD CATEGORIES");
       admin.add(addCategory);
       addCategory.addActionListener(this);
       
         addRooms=new JMenuItem("ADD ROOMS");
       admin.add(addRooms);
       addRooms.addActionListener(this);
       
        
       
       setVisible(true);
   } 
   public void actionPerformed(ActionEvent ae){
       if(ae.getSource()== addEmployee){
           new AddEmployee().setVisible(true);
       }
       else if(ae.getSource()== addRooms){
           new AddRooms().setVisible(true);
       }
       else if(ae.getSource()== addCategory){
//           setVisible(false);
            //setVisible(false);
          new AddCategory().setVisible(true);
       }
       else if(ae.getSource()== reception){
//           setVisible(false);
          new Reception().setVisible(true);
       }
   }
   public static void main(String args[]){
       new Dashboard();
   }
}
