package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class HotelManagementSystem extends JFrame implements ActionListener{
    JButton Start;
    HotelManagementSystem(){
        setSize(1366,565);
         setLayout(null);
        setTitle("Welcome To Our Application For Hotel");
        ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("icons/HotelBack.jpg"));
        Image i2=i1.getImage().getScaledInstance(1366, 565, Image.SCALE_DEFAULT);
        ImageIcon i3=new ImageIcon(i2);
        JLabel image=new JLabel(i3);
        image.setBounds(0,0,1366,565);
        add(image);
        setVisible(true);
        setLocation(4,50);
        
        JLabel text=new JLabel("Hotel Desktop Application");
        text.setBounds(350,50,1000,100);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("Serif",Font.PLAIN,80));
        image.add(text);
        
        Start=new JButton("Start");
        Start.setBounds(1100,450,150,50);
        Start.setBackground(Color.WHITE);
        image.add(Start);
        Start.addActionListener(this);
        
        while(true){
            text.setVisible(false);
            try{
                Thread.sleep(500);
            }catch(Exception e){
                e.printStackTrace();
            }
            text.setVisible(true);
            try{
                Thread.sleep(500);
            }catch(Exception e){
                e.printStackTrace();
            }
            text.setVisible(false);
        }
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==Start){
            setVisible(false);
            new Login().setVisible(true);
            //new Dashboard();
        }
    }
    
    public static void main(String[] args) {
        new HotelManagementSystem();
    }
    
}