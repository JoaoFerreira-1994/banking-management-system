package com.corebank.service;

import java.util.List;

import com.corebank.model.Employee;
import com.corebank.repository.EmployeeRepository;

public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService() {
        employeeRepository = new EmployeeRepository();
    }


    // ------------------------------ CREATE ------------------------------

    public boolean createEmployee(Employee employee) {

        if (employee == null) {
            System.out.println("Employee data is required.");
            return false;
        }

        if (employee.getName() == null ||
            employee.getName().trim().isEmpty()) {

            System.out.println("Employee name is required.");
            return false;
        }

        if (employee.getEmail() == null ||
            employee.getEmail().trim().isEmpty()) {

            System.out.println("Employee email is required.");
            return false;
        }

        employee.setStatus("ACTIVE");

        employeeRepository.createEmployee(employee);

        return employee.getId() > 0;
    }


    // ------------------------------ LIST ------------------------------

    public List<Employee> listEmployees() {
        return employeeRepository.listEmployees();
    }


    // ------------------------------ FIND BY ID ------------------------------

    public Employee findEmployeeID(int id) {

        if (id <= 0) {
            System.out.println("Invalid employee ID.");
            return null;
        }

        return employeeRepository.findEmployeeID(id);
    }


    // ------------------------------ UPDATE ------------------------------

    public boolean updateEmployee(int id, Employee employee) {

        if (id <= 0 || employee == null) {
            System.out.println("Invalid employee data.");
            return false;
        }

        Employee existingEmployee =
            employeeRepository.findEmployeeID(id);

        if (existingEmployee == null) {
            System.out.println("Employee not found.");
            return false;
        }

        if (employee.getName() == null ||
            employee.getName().trim().isEmpty()) {

            System.out.println("Employee name is required.");
            return false;
        }

        if (employee.getEmail() == null ||
            employee.getEmail().trim().isEmpty()) {

            System.out.println("Employee email is required.");
            return false;
        }

        employee.setId(id);

        return employeeRepository.updateEmployee(employee);
    }


    // ------------------------------ DELETE ------------------------------

    public boolean deleteEmployee(int id) {

        if (id <= 0) {
            System.out.println("Invalid employee ID.");
            return false;
        }

        Employee employee =
            employeeRepository.findEmployeeID(id);

        if (employee == null) {
            System.out.println("Employee not found.");
            return false;
        }

        return employeeRepository.deleteEmployee(id);
    }
}