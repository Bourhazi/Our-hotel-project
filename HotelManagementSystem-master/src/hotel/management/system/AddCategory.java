package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddCategory extends JFrame implements ActionListener{
      JButton addroom,cancel;
    JTextField tfname;
    //JComboBox combogender,comboavailable;
    AddCategory(){
        setLayout(null);
        
        JLabel heading=new JLabel("ADD CATEGORY");
        heading.setFont(new Font("Tahoma",Font.BOLD,18));
        heading.setBounds(150,20,200,20);
        add(heading);
        
        JLabel lblname=new JLabel("Name");
        lblname.setBounds(60,80,120,30);
        lblname.setFont(new Font("Tahoma",Font.PLAIN,18));
        add(lblname);
        
         tfname=new JTextField();
        tfname.setBounds(200,80,150,30);
        add(tfname);
        
        
        
        /*JLabel lblage=new JLabel("Age");
        lblage.setBounds(60,130,130,30);
        lblage.setFont(new Font("Tahoma",Font.PLAIN,18));
        add(lblage);
        
         tfage=new JTextField();
        tfage.setBounds(200,130,150,30);
        add(tfage);
        
        JLabel lblgender=new JLabel("Gender");
        lblgender.setBounds(60,180,130,30);
        lblgender.setFont(new Font("Tahoma",Font.PLAIN,18));
        add(lblgender);
        
        String bedOptions[]={"Male","Female"};
         combogender=new JComboBox(bedOptions);
        combogender.setBounds(200,180,150,30);
        combogender.setBackground(Color.WHITE);
        add(combogender);
                
     
         JLabel lblprice=new JLabel("Car Company");
        lblprice.setBounds(60,230,120,30);
        lblprice.setFont(new Font("Tahoma",Font.PLAIN,18));
        add(lblprice);
        
         tfcarcompany=new JTextField();
        tfcarcompany.setBounds(200,230,150,30);
        add(tfcarcompany);
        
        JLabel lblcarmodel=new JLabel("Car Model");
        lblcarmodel.setBounds(60,280,130,30);
        lblcarmodel.setFont(new Font("Tahoma",Font.PLAIN,18));
        add(lblcarmodel);
                
         tfcarmodel=new JTextField();
        tfcarmodel.setBounds(200,280,150,30);
        add(tfcarmodel);
        
         JLabel lblavailable=new JLabel("Available");
        lblavailable.setBounds(60,330,130,30);
        lblavailable.setFont(new Font("Tahoma",Font.PLAIN,18));
        add(lblavailable);
        
        String availableOptions[]={"Available","Busy"};
         comboavailable=new JComboBox(availableOptions);
        comboavailable.setBounds(200,330,150,30);
        comboavailable.setBackground(Color.WHITE);
        add(comboavailable);*/
        
         addroom=new JButton("Add Categorie");
        addroom.setBounds(60,400,130,30);
        addroom.setBackground(Color.BLACK);
        addroom.setForeground(Color.WHITE);
        add(addroom);
        addroom.addActionListener(this);
        
         cancel=new JButton("Cancel");
        cancel.setBounds(200,400,100,30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        add(cancel);
        cancel.addActionListener(this);
        
        ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("icons/addDrivers.jpg"));
        Image i2=i1.getImage().getScaledInstance(400,300, Image.SCALE_DEFAULT);
        ImageIcon i3=new ImageIcon(i2);
        JLabel image=new JLabel(i3);
        image.setBounds(400,80,400,300);
        add(image);
        
        
        getContentPane().setBackground(Color.WHITE);
        setBounds(250,100,850,540);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==addroom){
            String name=tfname.getText();
            /*String age=tfage.getText();
            String gender=(String) combogender.getSelectedItem();
            String carcompany=tfcarcompany.getText();
            String carmodel=tfcarmodel.getText();
            String available=(String) comboavailable.getSelectedItem();*/
            
            
            try{
                Conn c=new Conn();
                String query="insert into categorie(name) values('"+name+"')";
                c.s.executeUpdate(query);
                
                JOptionPane.showMessageDialog(null,"Categorie Succesfully added");
                setVisible(false);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(ae.getSource()==cancel){
            setVisible(false);
        }
    }
    public static void main(String args[]){
        new AddCategory();
    }
}
