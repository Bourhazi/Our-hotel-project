package hotel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.proteanit.sql.*;
import java.sql.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.io.File;

public class EmployeeInfo extends JFrame implements ActionListener {
    JTable table;
    JButton back, delete, update, searchButton, next, previous,export;
    JTextField searchField;
    int page = 1;
    int totalPages = 0;
    int recordsPerPage = 5;

    EmployeeInfo() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Labels
        
      /*  JLabel heading = new JLabel("EMPLOYEE DETAILS");
        heading.setFont(new Font("Tahoma", Font.BOLD, 18));
        heading.setBounds(20, 10, 200, 20);
        add(heading);
        
        JLabel l1 = new JLabel("Name");
        l1.setBounds(40, 10, 100, 20);
        add(l1);

        JLabel l2 = new JLabel("Age");
        l2.setBounds(170, 10, 100, 20);
        add(l2);

        JLabel l3 = new JLabel("Salary");
        l3.setBounds(290, 10, 100, 20);
        add(l3);

        JLabel l4 = new JLabel("Phone");
        l4.setBounds(400, 10, 100, 20);
        add(l4);

        JLabel l5 = new JLabel("Email");
        l5.setBounds(540, 10, 100, 20);
        add(l5);

        JLabel l6 = new JLabel("CIN/Passport");
        l6.setBounds(670, 10, 100, 20);
        add(l6);

        JLabel l7 = new JLabel("Gender");
        l7.setBounds(790, 10, 100, 20);
        add(l7);

        JLabel l8 = new JLabel("Job");
        l8.setBounds(910, 10, 100, 20);
        add(l8);

 */

    
        // Table
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 40, 800, 400);
        add(scrollPane);
        
        // Boutons de pagination
        previous = new JButton("<");
        previous.setBounds(20, 450, 50, 30);
        previous.addActionListener(this);
        add(previous);

        next = new JButton(">");
        next.setBounds(80, 450, 50, 30);
        next.addActionListener(this);
        add(next);


        // Delete Button
        delete = new JButton("Delete");
        delete.setBackground(Color.BLACK);
        delete.setForeground(Color.WHITE);
        delete.setBounds(130, 450, 100, 30);
        delete.addActionListener(this);
        add(delete);

        // Update Button
        update = new JButton("Update");
        update.setBackground(Color.BLACK);
        update.setForeground(Color.WHITE);
        update.setBounds(240, 450, 100, 30);
        update.addActionListener(this);
        add(update);

        // Search Field
        searchField = new JTextField();
        searchField.setBounds(350, 450, 150, 30);
        add(searchField);

        // Search Button
        searchButton = new JButton("Search");
        searchButton.setBackground(Color.BLACK);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBounds(510, 450, 100, 30);
        searchButton.addActionListener(this);
        add(searchButton);

        // Bouton Back
        back = new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setBounds(720, 450, 100, 30);
        back.addActionListener(this);
        add(back);
        
        // Bouton d'exportation
        export = new JButton("Export to Excel");
        export.setBackground(Color.BLACK);
        export.setForeground(Color.WHITE);
        export.setBounds(620, 500, 150, 30);
        export.addActionListener(this);
        add(export);
        
        
        calculateTotalPages();
        fetchPage(page);

        setBounds(150, 100, 850, 550);
        setVisible(true);
    }
    
        private void fetchPage(int page) {
        try {
            Conn c = new Conn();
            int start = (page - 1) * recordsPerPage;
            String query = "SELECT * FROM employee LIMIT " + start + ", " + recordsPerPage;
            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateTotalPages() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT COUNT(*) FROM employee");
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
        String query = "SELECT * FROM employee WHERE name LIKE ? OR cin_passport LIKE ? LIMIT " + start + ", " + recordsPerPage;
        PreparedStatement pstmt = c.c.prepareStatement(query);
        pstmt.setString(1, "%" + searchText + "%");
        pstmt.setString(2, "%" + searchText + "%");

        ResultSet rs = pstmt.executeQuery();
        table.setModel(DbUtils.resultSetToTableModel(rs));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    private void calculateTotalPagesSearch(String searchText) {
    try {
        Conn c = new Conn();
        String query = "SELECT COUNT(*) FROM employee WHERE name LIKE ? OR cin_passport LIKE ?";
        PreparedStatement pstmt = c.c.prepareStatement(query);
        pstmt.setString(1, "%" + searchText + "%");
        pstmt.setString(2, "%" + searchText + "%");

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int totalRecords = rs.getInt(1);
            totalPages = (int) Math.ceil(totalRecords / (double) recordsPerPage);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    private void exportToExcel() {
        try {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Employee Data");

        Conn c = new Conn();
        String query = "SELECT * FROM employee"; // Requête pour obtenir toutes les données
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
    
    

    public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == export) {
            exportToExcel();
        }
    if (ae.getSource() == next) {
        if (page < totalPages) {
            page++;
            fetchPage(page);
        }
    }else if (ae.getSource() == previous) {
        if (page > 1) {
            page--;
            fetchPage(page);
        }
    } else if (ae.getSource() == back) {
        setVisible(false);
        // Autre logique pour le bouton Back si nécessaire
    } else if (ae.getSource() == delete) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee to delete.");
            return;

        }

        String cinPassport = (String) table.getModel().getValueAt(row, 5);
        try {
            Conn c = new Conn();
            String query = "DELETE FROM employee WHERE cin_passport = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, cinPassport);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Employee Deleted Successfully");
                fetchPage(page); // Rafraîchir les données de la table
            } else {
                JOptionPane.showMessageDialog(null, "Error in Deleting Employee");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else if (ae.getSource() == update) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee to update.");
            return;
        }

            // Récupération et mise à jour des informations de l'employé
            String name = (String) table.getModel().getValueAt(row, 0);
            String age = table.getModel().getValueAt(row, 1).toString();
            String salary = table.getModel().getValueAt(row, 2).toString();
            String phone = (String) table.getModel().getValueAt(row, 3);
            String email = (String) table.getModel().getValueAt(row, 4);
            String cinPassport = (String) table.getModel().getValueAt(row, 5);
            String job = (String) table.getModel().getValueAt(row, 7);

            name = JOptionPane.showInputDialog("Enter Name:", name);
            age = JOptionPane.showInputDialog("Enter Age:", age);
            salary = JOptionPane.showInputDialog("Enter Salary:", salary);
            phone = JOptionPane.showInputDialog("Enter Phone:", phone);
            email = JOptionPane.showInputDialog("Enter Email:", email);
            
            // Choix du travail
            String[] jobOptions = {"Front Desk Clerks", "Porters", "Housekeeping", "Kitchen Staff", "Room Service", "Chefs", "Waiters", "Manager", "Accountant"};
            JComboBox jobBox = new JComboBox(jobOptions);
            jobBox.setSelectedItem(job);
            JOptionPane.showMessageDialog(null, jobBox, "Select Job", JOptionPane.QUESTION_MESSAGE);
            job = (String) jobBox.getSelectedItem();

            // Choix du genre
            String[] genderOptions = {"Male", "Female"};
            JComboBox genderBox = new JComboBox(genderOptions);
            genderBox.setSelectedItem(table.getModel().getValueAt(row, 6));
            JOptionPane.showMessageDialog(null, genderBox, "Select Gender", JOptionPane.QUESTION_MESSAGE);
            String gender = (String) genderBox.getSelectedItem();
            

            // Mise à jour dans la base de données
            try {
                Conn c = new Conn();
                String query = "UPDATE employee SET name = ?, age = ?, salary = ?, phone = ?, email = ?, gender = ?, job = ? WHERE cin_passport = ?";
                PreparedStatement pstmt = c.c.prepareStatement(query);

                pstmt.setString(1, name);
                pstmt.setInt(2, Integer.parseInt(age));
                pstmt.setFloat(3, Float.parseFloat(salary));
                pstmt.setString(4, phone);
                pstmt.setString(5, email);
                pstmt.setString(6, gender);
                pstmt.setString(7, job);
                pstmt.setString(8, cinPassport);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Employee Updated Successfully");
                    // Rafraîchir le tableau
                    ResultSet rs = c.s.executeQuery("select * from employee");
                    table.setModel(DbUtils.resultSetToTableModel(rs));
                } else {
                    JOptionPane.showMessageDialog(null, "Error in Updating Employee");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                 else if (ae.getSource() == searchButton) {
            String searchText = searchField.getText();
            calculateTotalPagesSearch(searchText);
            fetchPageSearch(1, searchText);
        }
    }

    public static void main(String args[]) {
        new EmployeeInfo();
    }
}


