package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.proteanit.sql.*;
import java.sql.*;
import java.util.ArrayList;

public class Room extends JFrame implements ActionListener {
    JTable table;
    JButton add, update, back, search, delete, previous, next;
    JTextField searchField;
    int page = 1;
    int totalPages = 0;
    int recordsPerPage = 5;

    Room() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("ROOM DETAILS");
        heading.setFont(new Font("Tahoma", Font.BOLD, 18));
        heading.setBounds(20, 10, 200, 20);
        add(heading);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 40, 800, 400);
        add(scrollPane);

        previous = new JButton("<");
        previous.setBounds(20, 500, 50, 30);
        previous.addActionListener(this);
        add(previous);

        next = new JButton(">");
        next.setBounds(80, 500, 50, 30);
        next.addActionListener(this);
        add(next);


        update = new JButton("Update");
        update.setBackground(Color.BLACK);
        update.setForeground(Color.WHITE);
        update.setBounds(250, 500, 100, 30);
        update.addActionListener(this);
        add(update);

        delete = new JButton("Delete");
        delete.setBackground(Color.BLACK);
        delete.setForeground(Color.WHITE);
        delete.setBounds(360, 500, 100, 30);
        delete.addActionListener(this);
        add(delete);

        back = new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setBounds(470, 500, 100, 30);
        back.addActionListener(this);
        add(back);

        searchField = new JTextField();
        searchField.setBounds(580, 500, 150, 30);
        add(searchField);

        search = new JButton("Search");
        search.setBackground(Color.BLACK);
        search.setForeground(Color.WHITE);
        search.setBounds(740, 500, 100, 30);
        search.addActionListener(this);
        add(search);

        calculateTotalPages();
        fetchPage(page);

        setBounds(150, 100, 850, 600);
        setVisible(true);
    }
    
    private String[] getCategoryOptions() {
    ArrayList<String> categories = new ArrayList<>();
    try {
        Conn c = new Conn();
        String query = "SELECT name FROM categorie";
        ResultSet rs = c.s.executeQuery(query);

        while (rs.next()) {
            categories.add(rs.getString("name"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Convertir la liste en tableau
    String[] categoryArray = new String[categories.size()];
    categoryArray = categories.toArray(categoryArray);
    return categoryArray;
}

    
    private void fetchPage(int page) {
        try {
            Conn c = new Conn();
            int start = (page - 1) * recordsPerPage;
            String query = "SELECT chambre.id, chambre.prix, categorie.name FROM chambre INNER JOIN categorie ON chambre.category_id = categorie.id LIMIT " + start + ", " + recordsPerPage;
            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateTotalPages() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT COUNT(*) FROM chambre");
            if (rs.next()) {
                int totalRecords = rs.getInt(1);
                totalPages = (int) Math.ceil(totalRecords / (double) recordsPerPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void fetchPageSearch(int page, String searchText) {
    try {
        Conn c = new Conn();
        int start = (page - 1) * recordsPerPage;
        String query = "SELECT chambre.id, chambre.prix, categorie.name " +
                       "FROM chambre " +
                       "INNER JOIN categorie ON chambre.category_id = categorie.id " +
                       "WHERE chambre.id LIKE ? OR chambre.prix LIKE ? OR categorie.name LIKE ? " +
                       "LIMIT " + start + ", " + recordsPerPage;
        PreparedStatement pstmt = c.c.prepareStatement(query);
        pstmt.setString(1, "%" + searchText + "%");
        pstmt.setString(2, "%" + searchText + "%");
        pstmt.setString(3, "%" + searchText + "%");

        ResultSet rs = pstmt.executeQuery();
        table.setModel(DbUtils.resultSetToTableModel(rs));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    private void calculateTotalPagesSearch(String searchText) {
    try {
        Conn c = new Conn();
        String query = "SELECT COUNT(*) FROM chambre " +
                       "INNER JOIN categorie ON chambre.category_id = categorie.id " +
                       "WHERE chambre.id LIKE ? OR chambre.prix LIKE ? OR categorie.name LIKE ?";
        PreparedStatement pstmt = c.c.prepareStatement(query);
        pstmt.setString(1, "%" + searchText + "%");
        pstmt.setString(2, "%" + searchText + "%");
        pstmt.setString(3, "%" + searchText + "%");

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int totalRecords = rs.getInt(1);
            totalPages = (int) Math.ceil(totalRecords / (double) recordsPerPage);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == next) {
            if (page < totalPages) {
                page++;
                fetchPage(page);
            }
        } else if (ae.getSource() == previous) {
            if (page > 1) {
                page--;
                fetchPage(page);
            }
        
        } else if (ae.getSource() == update) {
            // Logique pour mettre à jour une chambre
            int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(null, "Please select a room to update.");
        return;
    }

    
    
    // Récupération des informations de la chambre
    String roomId = table.getModel().getValueAt(row, 0).toString();
    String price = table.getModel().getValueAt(row, 1).toString();
    String categoryName = table.getModel().getValueAt(row, 2).toString();

    // Demande à l'utilisateur de mettre à jour les informations
    price = JOptionPane.showInputDialog("Enter Price:", price);
    
    // Récupération de la liste des catégories
    String[] categoryOptions = getCategoryOptions(); // Vous devez implémenter cette méthode
    JComboBox<String> categoryBox = new JComboBox<>(categoryOptions);
    categoryBox.setSelectedItem(categoryName);
    JOptionPane.showMessageDialog(null, categoryBox, "Select Category", JOptionPane.QUESTION_MESSAGE);
    categoryName = (String) categoryBox.getSelectedItem();

    // Mise à jour dans la base de données
    try {
        Conn c = new Conn();
        String query = "UPDATE chambre SET prix = ?, category_id = (SELECT id FROM categorie WHERE name = ?) WHERE id = ?";
        PreparedStatement pstmt = c.c.prepareStatement(query);

        pstmt.setFloat(1, Float.parseFloat(price));
        pstmt.setString(2, categoryName);
        pstmt.setString(3, roomId);

        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(null, "Room Updated Successfully");
            fetchPage(page); // Rafraîchir les données de la table
        } else {
            JOptionPane.showMessageDialog(null, "Error in Updating Room");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
            
        } else if (ae.getSource() == delete) {
            // Logique pour supprimer une chambre
            int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select a room to delete.");
            return;
        }

        // Supposons que l'ID de la chambre soit dans la première colonne de la table
        String roomId = table.getModel().getValueAt(row, 0).toString();
        try {
            Conn c = new Conn();
            String query = "DELETE FROM chambre WHERE id = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, roomId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Room Deleted Successfully");
                fetchPage(page); // Rafraîchir les données de la table
            } else {
                JOptionPane.showMessageDialog(null, "Error in Deleting Room");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            
            
        } else if (ae.getSource() == back) {
            setVisible(false); // Ferme la fenêtre actuelle
        } else if (ae.getSource() == search) {
        String searchText = searchField.getText();
        calculateTotalPagesSearch(searchText);
        fetchPageSearch(1, searchText);
    }
    }

    public static void main(String[] args) {
        new Room();
    }
}
