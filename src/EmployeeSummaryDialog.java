/*
 *
 * This is the summary dialog for displaying all Employee details
 *
 * */

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Vector;

public class EmployeeSummaryDialog extends JDialog implements ActionListener {
    // vector with all Employees details
    Vector<Object> allEmployees;
    JButton back;

    public EmployeeSummaryDialog(Vector<Object> allEmployees) {
        this.allEmployees = allEmployees;
        initUI();
    }

    private static Vector<Object> getObjects(Employee allEmployee) {
        Vector<Object> rowsVector = new Vector<>();
        rowsVector.addElement(allEmployee.getEmployeeId());
        rowsVector.addElement(allEmployee.getPps());
        rowsVector.addElement(allEmployee.getSurname());
        rowsVector.addElement(allEmployee.getFirstName());
        rowsVector.addElement(allEmployee.getGender());
        rowsVector.addElement(allEmployee.getDepartment());
        rowsVector.addElement(allEmployee.getSalary());
        rowsVector.addElement(allEmployee.getFullTime());
        return rowsVector;
    }

    public void initUI() {
        setTitle("Employee Summary");
        setModal(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(summaryPane());
        setContentPane(scrollPane);
        setSize(850, 500);
        setLocation(350, 250);
        setVisible(true);
    }

    public Container summaryPane() {
        JPanel summaryDialog = new JPanel(new MigLayout());
        JPanel buttonPanel = createButtonPanel();
        JTable employeeTable = createEmployeeTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        summaryDialog.add(buttonPanel, "growx, pushx, wrap");
        summaryDialog.add(scrollPane, "growx, pushx, wrap");
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        return summaryDialog;
    }

    private JTable createEmployeeTable() {
        // column center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        // column left alignment
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();

        Vector<Object> header = new Vector<>();
        Vector<Object> rowsVector;
        Vector<Vector<Object>> dataVector = new Vector<>();
        for (Object allEmployee : allEmployees) {
            rowsVector = getObjects((Employee) allEmployee);
            dataVector.addElement(rowsVector);
        }

        // header names

        String[] headerName = {"ID", "PPS Number", "Surname", "First Name", "Gender", "Department", "Salary", "Full Time"};
        int[] colWidth = {15, 100, 120, 120, 50, 120, 80, 80};
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        for (String name : headerName) {
            header.addElement(name);
        }

        DefaultTableModel tableModel = new DefaultTableModel(dataVector, header) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 4 -> Character.class;
                    case 6 -> Double.class;
                    case 7 -> Boolean.class;
                    default -> String.class;
                };
            }
        };
        JTable employeeTable = new JTable(tableModel);
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumn(headerName[i]).setMinWidth(colWidth[i]);
        }
        employeeTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        employeeTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        employeeTable.getColumnModel().getColumn(6).setCellRenderer(new DecimalFormatRenderer());

        employeeTable.setEnabled(false);
        employeeTable.setPreferredScrollableViewportSize(new Dimension(800, (15 * employeeTable.getRowCount() + 15)));
        employeeTable.setAutoCreateRowSorter(true);
        return employeeTable;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        back = new JButton("Back");
        back.addActionListener(this);
        back.setToolTipText("Return to main screen");
        buttonPanel.add(back);
        return buttonPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            dispose();
        }

    }

    // format for salary column
    static class DecimalFormatRenderer extends DefaultTableCellRenderer {
        private static final DecimalFormat format = new DecimalFormat(
                "\u20ac ###,###,##0.00");

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel label = (JLabel) c;
            label.setHorizontalAlignment(JLabel.RIGHT);
            // format salary column
            value = format.format(value);
            return c;
        }// end getTableCellRendererComponent
    }// DefaultTableCellRenderer
}// end class EmployeeSummaryDialog
