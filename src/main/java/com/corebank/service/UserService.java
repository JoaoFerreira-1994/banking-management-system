package com.corebank.service;

import com.corebank.model.Client;
import com.corebank.model.Employee;
import com.corebank.model.User;
import com.corebank.repository.ClientRepository;
import com.corebank.repository.EmployeeRepository;
import com.corebank.repository.UserRepository;
import com.corebank.util.PasswordUtil;

public class UserService {

    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private EmployeeRepository employeeRepository;

    public UserService() {
        userRepository = new UserRepository();
        clientRepository = new ClientRepository();
        employeeRepository = new EmployeeRepository();
    }


    // ------------------------------ CREATE ------------------------------

    public boolean createUser(User user) {

        if (user == null) {
            System.out.println("User data is required.");
            return false;
        }

        if (user.getUsername() == null ||
            user.getUsername().trim().isEmpty()) {

            System.out.println("Username is required.");
            return false;
        }

        if (user.getPassword() == null ||
            user.getPassword().trim().isEmpty()) {

            System.out.println("Password is required.");
            return false;
        }

        if (user.getRole() == null ||
            user.getRole().trim().isEmpty()) {

            System.out.println("Role is required.");
            return false;
        }

        User existingUser =
            userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            System.out.println("Username already exists.");
            return false;
        }

        // CLIENT
        if ("CLIENT".equalsIgnoreCase(user.getRole())) {

            if (user.getClientId() == null ||
                user.getEmployeeId() != null) {

                System.out.println("Invalid client user.");
                return false;
            }

            Client client =
                clientRepository.findClientID(user.getClientId());

            if (client == null) {
                System.out.println("Client not found.");
                return false;
            }

            user.setRole("CLIENT");
        }

        // EMPLOYEE
        else if ("EMPLOYEE".equalsIgnoreCase(user.getRole())) {

            if (user.getEmployeeId() == null ||
                user.getClientId() != null) {

                System.out.println("Invalid employee user.");
                return false;
            }

            Employee employee =
                employeeRepository.findEmployeeID(user.getEmployeeId());

            if (employee == null) {
                System.out.println("Employee not found.");
                return false;
            }

            user.setRole("EMPLOYEE");
        }

        else {
            System.out.println("Invalid role.");
            return false;
        }

        String hashedPassword =
        PasswordUtil.hashPassword(user.getPassword());

        user.setPassword(hashedPassword);
        
        userRepository.createUser(user);

        return user.getId() > 0;
    }
}