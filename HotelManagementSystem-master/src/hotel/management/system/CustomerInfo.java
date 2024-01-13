package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import net.proteanit.sql.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomerInfo extends JFrame implements ActionListener {
    JTable table;
    JButton back, update, export;
    JScrollPane scrollPane;

    CustomerInfo() {
       getContentPane().setBackground(java.awt.Color.WHITE);
        setLayout(null);

        String[] columnNames = {"ID", "Nom", "Prénom", "CIN", "Email", "gender", "Téléphone", "Adresse", "Date de Naissance"};
        for (int i = 0; i < columnNames.length; i++) {
            JLabel lbl = new JLabel(columnNames[i]);
            lbl.setBounds(30 + i * 100, 10, 100, 20);
            add(lbl);
        }

        table = new JTable();
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 40, 900, 400);
        add(scrollPane);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from client");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        back = new JButton("Back");
        back.setBounds(50, 450, 100, 30);
        back.addActionListener(this);
        add(back);

  

        update = new JButton("Update");
        update.setBounds(290, 450, 120, 30);
        update.addActionListener(this);
        add(update);

        export = new JButton("Exporter Excel");
        export.setBounds(430, 450, 120, 30);
        export.addActionListener(this);
        add(export);

        setSize(950, 550);
        setVisible(true);
    }

 
public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == back) {
        setVisible(false);
        new Reception();
    }  else if (ae.getSource() == update) {
        updateCustomer();  // Assurez-vous que cette ligne est bien exécutée
    } else if (ae.getSource() == export) {
        exportToExcel();
    }
}


  

 private void updateCustomer() {
    int row = table.getSelectedRow();
    if (row >= 0) {
        // Assurez-vous que l'index des colonnes correspond à votre JTable
        long id = Long.parseLong(table.getModel().getValueAt(row, 1).toString()); // ID
        String nom = (String) table.getModel().getValueAt(row, 6); // Nom
        String prenom = (String) table.getModel().getValueAt(row, 7); // Prénom
        String cin = (String) table.getModel().getValueAt(row, 3); // CIN
        String email = (String) table.getModel().getValueAt(row, 4); // Email
        String gender = (String) table.getModel().getValueAt(row, 5); // gender
        String telephone = (String) table.getModel().getValueAt(row, 8); // Téléphone
        String adresse = (String) table.getModel().getValueAt(row, 2); // Adresse
        Object dateNaissanceObj = table.getModel().getValueAt(row, 0); // Récupération de l'objet date
String dateNaissance = "";
if (dateNaissanceObj instanceof LocalDateTime) {
    LocalDateTime localDateTime = (LocalDateTime) dateNaissanceObj;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    dateNaissance = localDateTime.format(formatter);
} else if (dateNaissanceObj != null) {
    dateNaissance = dateNaissanceObj.toString(); // Au cas où c'est déjà une chaîne
}
 // Date de Naissance

        // Demander la mise à jour des informations
        String newNom = JOptionPane.showInputDialog(null, "Modifier le nom:", nom);
        String newPrenom = JOptionPane.showInputDialog(null, "Modifier le prénom:", prenom);
        String newCin = JOptionPane.showInputDialog(null, "Modifier le CIN:", cin);
        String newEmail = JOptionPane.showInputDialog(null, "Modifier l'Email:", email);
        String newTelephone = JOptionPane.showInputDialog(null, "Modifier le Téléphone:", telephone);
        String newAdresse = JOptionPane.showInputDialog(null, "Modifier l'Adresse:", adresse);
        String newDateNaissance = JOptionPane.showInputDialog(null, "Modifier la Date de Naissance(YYYY-MM-DD)", dateNaissance);
    // Choix du gender
    String[] genders = {"Male", "Female"};
    String newgender = (String) JOptionPane.showInputDialog(null, "Choisir le gender:", "gender", JOptionPane.QUESTION_MESSAGE, null, genders, gender);

    // Vérification si l'une des nouvelles valeurs est nulle (annulation de mise à jour)
    if (newNom != null && newPrenom != null && newCin != null && newEmail != null && newgender != null && newTelephone != null && newAdresse != null && newDateNaissance != null) {
        try {
            Conn c = new Conn();
            String query = "UPDATE client SET nom = ?, prenom = ?, cin = ?, email = ?, gender = ?, telephone = ?, adresse = ?, date_de_naissance = ? WHERE id = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, newNom);
            pstmt.setString(2, newPrenom);
            pstmt.setString(3, newCin);
            pstmt.setString(4, newEmail);
            pstmt.setString(5, newgender);
            pstmt.setString(6, newTelephone);
            pstmt.setString(7, newAdresse);
            pstmt.setString(8, newDateNaissance);
            pstmt.setLong(9, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Mise à jour réussie");
                // Recharger les données dans la table (à implémenter)
            } else {
                JOptionPane.showMessageDialog(null, "Aucune modification effectuée");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur de mise à jour: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "Mise à jour annulée");
    }
} else {
    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un client à mettre à jour");
}
     try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from client");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
 }
  private void exportToExcel() {
        try {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Client Data");

        Conn c = new Conn();
        String query = "SELECT * FROM client"; // Requête pour obtenir toutes les données
        ResultSet rs = c.s.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 1; i <= columnCount; i++) {
            XSSFCell cell = headerRow.createCell(i - 1);
            cell.setCellValue(rsmd.getColumnName(i));
        }

        int rowIndex = 1;
        while (rs.next()) {
            XSSFRow row = sheet.createRow(rowIndex++);
            for (int i = 1; i <= columnCount; i++) {
                row.createCell(i - 1).setCellValue(rs.getString(i));
            }
        }

            // Enregistrement du fichier
         JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            JOptionPane.showMessageDialog(this, "Export Successful");
        }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Export Failed");
        }
    }


    public static void main(String args[]) {
        new CustomerInfo();
    }
}




       
