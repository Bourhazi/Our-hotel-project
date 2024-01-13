package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddRooms extends JFrame implements ActionListener {
    JButton addroom, cancel;
    JTextField tfroom, tfprice;
    Choice ccategory;  // Composant Choice pour les catégories
    JComboBox<String> comboavailable, comboacleaning, combobed;
    JComboBox<Integer> roomNumberComboBox;

    public AddRooms() {
        setLayout(null);
        
        JLabel heading=new JLabel("ADD Room");
        heading.setFont(new Font("Tahoma",Font.BOLD,18));
        heading.setBounds(150,20,200,20);
        add(heading);
        
        // Ajout du composant Choice pour les catégories
        JLabel lblCategory = new JLabel("Category");
        lblCategory.setBounds(60, 160, 120, 30);
        add(lblCategory);

        ccategory = new Choice();
        ccategory.setBounds(200, 160, 150, 30);
        add(ccategory);
        loadCategories(); // Charger les catégories

        // Ajout du JTextField pour le numéro de chambre
        JLabel lblRoom = new JLabel("Room Number");
        lblRoom.setBounds(60, 200, 120, 30);
        add(lblRoom);

         roomNumberComboBox = new JComboBox<>();
        roomNumberComboBox.setBounds(200, 200, 150, 30);
        loadAvailableRoomNumbers(); // Charger les numéros de chambre disponibles
        add(roomNumberComboBox);

            tfroom = new JTextField(); // Initialisation de tfroom
    tfroom.setBounds(200, 200, 150, 30);
    add(tfroom);
        
        // Ajout du JTextField pour le prix
        JLabel lblPrice = new JLabel("Price");
        lblPrice.setBounds(60, 240, 120, 30);
        add(lblPrice);

        tfprice = new JTextField();
        tfprice.setBounds(200, 240, 150, 30);
        add(tfprice);


        // Bouton pour ajouter une chambre
        addroom = new JButton("Add Room");
        addroom.setBounds(60, 300, 130, 30);
        addroom.addActionListener(this);
        add(addroom);

        // Bouton pour annuler
        cancel = new JButton("Cancel");
        cancel.setBounds(220, 300, 130, 30);
        cancel.addActionListener(this);
        add(cancel);
        setSize(400, 450);
        
       setSize(400, 450);
        setVisible(true);
        setLocation(500, 200);
    }

    // Méthode pour charger les catégories depuis la base de données
    private void loadCategories() {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelmanager", "root", "");
        stmt = conn.createStatement();

        String sql = "SELECT name FROM categorie";
        rs = stmt.executeQuery(sql);

        while (rs.next()) {
            ccategory.add(rs.getString("name"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    // Méthode pour obtenir le prochain numéro de chambre
    private void loadAvailableRoomNumbers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelmanager", "root", "");
            for (int i = 1; i <= 2000; i++) {
                String checkRoom = "SELECT COUNT(*) FROM chambre WHERE id = ?";
                pstmt = conn.prepareStatement(checkRoom);
                pstmt.setInt(1, i);
                rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    roomNumberComboBox.addItem(i); // Ajoutez l'ID non utilisé à la liste déroulante
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
  if (ae.getSource() == addroom) {
        String selectedCategoryName = ccategory.getSelectedItem();
        Integer roomNumber = (Integer) roomNumberComboBox.getSelectedItem(); // Récupération du numéro de chambre sélectionné
        String price = tfprice.getText();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelmanager", "root", "");

            // Requête pour trouver l'ID de la catégorie en fonction du nom
            String queryCategory = "SELECT id FROM categorie WHERE name = ?";
            pstmt = conn.prepareStatement(queryCategory);
            pstmt.setString(1, selectedCategoryName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int categoryId = rs.getInt("id");

                // Insertion de la nouvelle chambre avec l'ID de la catégorie
                String insertRoom = "INSERT INTO chambre (prix, category_id, id) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(insertRoom);
                pstmt.setString(1, price);
                pstmt.setInt(2, categoryId);
                pstmt.setInt(3, roomNumber); // Utilisation de roomNumber récupéré

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Room Added Successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Room Not Added");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }this.dispose();
        new AddRooms();
    } else if (ae.getSource() == cancel) {
        this.setVisible(false); // Fermer la fenêtre
    }

}
    public static void main(String[] args) {
        new AddRooms();
    }
}