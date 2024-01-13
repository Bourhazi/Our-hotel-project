package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.*;

public class ReservationInfo extends JFrame implements ActionListener {
    JTable table;
    JButton delete, refresh;

    ReservationInfo() {
        setTitle("Informations sur les Réservations");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel l1 = new JLabel("Informations sur les Réservations");
        l1.setBounds(200, 10, 400, 30);
        l1.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(l1);

        table = new JTable();
        table.setBounds(0, 50, 1000, 400);
        add(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 50, 1000, 400);
        add(scrollPane);

        delete = new JButton("Supprimer");
        delete.setBounds(100, 460, 150, 30);
        delete.addActionListener(this);
        add(delete);

        refresh = new JButton("Actualiser");
        refresh.setBounds(300, 460, 150, 30);
        refresh.addActionListener(this);
        add(refresh);

        loadTableData();
        setBounds(300, 200, 1020, 550);
        setVisible(true);
    }

    private void loadTableData() {
    try {
        Conn c = new Conn();
        String query = "SELECT * FROM reservation";
        ResultSet rs = c.s.executeQuery(query);
        table.setModel(DbUtils.resultSetToTableModel(rs));
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors du chargement des données");
    }
}

public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == delete) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = Integer.parseInt(table.getModel().getValueAt(row, 4).toString()); // Assumant que l'ID est à l'indice 4
            try {
                Conn c = new Conn();
                String query = "DELETE FROM reservation WHERE id = ?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Réservation supprimée avec succès");
                    loadTableData(); // Recharge les données
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la réservation");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une réservation à supprimer");
        }
    } else if (ae.getSource() == refresh) {
        loadTableData(); // Recharge les données
    }
}

public static void main(String[] args) {
    new ReservationInfo();
}
}