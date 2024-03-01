import java.io.File;
import java.util.Vector;

public class EmployeeManager {
    private final RandomFile application = new RandomFile();
    String generatedFileName;
    Employee currentEmployee;
    private File file;
    private long currentByteStart = 0;

    public void addEmployee(Employee employee) {
        // open file for writing
        application.openWriteFile(file.getAbsolutePath());
        // write into a file
        currentByteStart = application.addRecords(employee);
        application.closeWriteFile(); // close file for writing
    }

    // Method to delete an employee
    public void deleteEmployee(Employee employee) {
        // Code to delete an employee
    }

    // Method to search an employee by ID
    public Employee searchEmployeeById(int idToSearch) {
        Employee foundEmployee = null;

        // Logic to search for the employee by ID
        if (isSomeoneToDisplay()) {
            firstRecord();
            int firstId = currentEmployee.getEmployeeId();

            if (idToSearch == firstId) {
                foundEmployee = currentEmployee;
            } else {
                nextRecord();
                while (firstId != currentEmployee.getEmployeeId()) {
                    if (idToSearch == currentEmployee.getEmployeeId()) {
                        foundEmployee = currentEmployee;
                        break;
                    } else {
                        nextRecord();
                    }
                }
            }

            if (foundEmployee == null) {
                // Employee not found, handle accordingly (throw exception, return null, etc.)
            }
        }
        return foundEmployee;
    }

    public Employee searchEmployeesBySurname(String surnameToSearch) {
        Employee foundEmployee = null;
        if (isSomeoneToDisplay()) {
            firstRecord();
            String firstSurname = currentEmployee.getSurname().trim();

            if (surnameToSearch.equalsIgnoreCase(firstSurname)) {
                foundEmployee = currentEmployee;
            } else {
                nextRecord();
                while (!firstSurname.equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                    if (surnameToSearch.equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                        foundEmployee = currentEmployee;
                        break;
                    } else {
                        nextRecord();
                    }
                }
            }

            if (foundEmployee == null) {
                // Handle case when employee is not found
            }
        }
        return foundEmployee;
    }

    // get next free ID from Employees in the file
    public int getNextFreeId() {
        int nextFreeId = 0;
        // if file is empty or all records are empty start with ID 1 else look
        // for last active record
        if (file.length() == 0 || !isSomeoneToDisplay())
            nextFreeId++;
        else {
            lastRecord();// look for last active record
            // add 1 to last active records ID to get next ID
            nextFreeId = currentEmployee.getEmployeeId() + 1;
        }
        return nextFreeId;
    }// end getNextFreeId

    // Method to retrieve all employees
    public Vector<Object> getAllEmployees() {
        Vector<Object> allEmployees = new Vector<>();
        long byteStart = currentByteStart;
        firstRecord();
        int firstId = currentEmployee.getEmployeeId();

        do {
            Vector<Object> employeeDetails = new Vector<>();
            employeeDetails.addElement(currentEmployee.getEmployeeId());
            employeeDetails.addElement(currentEmployee.getPps());
            employeeDetails.addElement(currentEmployee.getSurname());
            employeeDetails.addElement(currentEmployee.getFirstName());
            employeeDetails.addElement(currentEmployee.getGender());
            employeeDetails.addElement(currentEmployee.getDepartment());
            employeeDetails.addElement(currentEmployee.getSalary());
            employeeDetails.addElement(currentEmployee.getFullTime());
            allEmployees.addElement(employeeDetails);
            nextRecord();
        } while (firstId != currentEmployee.getEmployeeId());

        currentByteStart = byteStart;
        return allEmployees;
    }

    // add Employee object to fail
    public void addRecord(Employee newEmployee) {
        // open file for writing
        application.openWriteFile(file.getAbsolutePath());
        // write into a file
        currentByteStart = application.addRecords(newEmployee);
        application.closeWriteFile();// close file for writing
    }// end addRecord

    // delete (make inactive - empty) record from file
    boolean deleteRecord() {
        if (isSomeoneToDisplay()) {
            application.openWriteFile(file.getAbsolutePath());
            application.deleteRecords(currentByteStart);
            application.closeWriteFile();
            return true; // Record deleted successfully
        }
        return false; // No active record to delete
    }


    private void openWriteFile() {
        application.openWriteFile(file.getAbsolutePath());
    }

    private void closeWriteFile() {
        application.closeWriteFile();
    }

    private void firstRecord() {
        // Logic to move to the first record
    }

    private void nextRecord() {
        // Logic to move to the next record
    }

    private void lastRecord() {
        // Logic to move to the last record
    }

    boolean isSomeoneToDisplay() {
        application.openReadFile(file.getAbsolutePath());
        // check if any of records in file is active - ID is not 0
        boolean someoneToDisplay = application.isSomeoneToDisplay();
        application.closeReadFile();// close file for reading
        if (!someoneToDisplay) {
            resetUI(); // Reset UI if no records found
        }
        return someoneToDisplay;
    }// end isSomeoneToDisplay

    private void resetUI() {
        currentEmployee = null;
        // Reset UI elements here
    }

    public boolean correctPps(String pps, long currentByte) {
        boolean ppsExist = false;
        // check for correct PPS format based on assignment description
        if (pps.length() == 8 || pps.length() == 9) {
            if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1))
                    && Character.isDigit(pps.charAt(2)) && Character.isDigit(pps.charAt(3))
                    && Character.isDigit(pps.charAt(4)) && Character.isDigit(pps.charAt(5))
                    && Character.isDigit(pps.charAt(6)) && Character.isLetter(pps.charAt(7))
                    && (pps.length() == 8 || Character.isLetter(pps.charAt(8)))) {
                // open file for reading
                application.openReadFile(file.getAbsolutePath());
                // look in file is PPS already in use
                ppsExist = application.isPpsExist(pps, currentByte);
                application.closeReadFile();// close file for reading
            } // end if
            else
                ppsExist = true;
        } // end if
        else
            ppsExist = true;

        return ppsExist;
    }// end correctPPS
}