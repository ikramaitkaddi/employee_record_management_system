package com.erms.employee_management.service;

import com.erms.employee_management.entity.Employee;
import com.erms.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LogAuditService logAuditService;

    // Helper methods to check roles and permissions
    private boolean hasRole(String userRole, String requiredRole) {
        return userRole.equalsIgnoreCase(requiredRole);
    }

    private boolean hasPermission(String userRole, String permissionName) {
        switch (userRole.toUpperCase()) {
            case "ADMIN":
                return true;
            case "HR":
                return permissionName.startsWith("employee:");
            case "MANAGER":
                return permissionName.matches("employee:(read|update)");
            default:
                return false;
        }
    }

    public List<Employee> getAllEmployees(String userRole, String userDepartment) {
        if (hasRole(userRole, "MANAGER") && userDepartment != null) {
            // Filter employees by the manager's department
            return employeeRepository.findAll()
                    .stream()
                    .filter(employee -> userDepartment.equals(employee.getDepartment()))
                    .collect(Collectors.toList());
        } else if (hasRole(userRole, "ADMIN") || hasRole(userRole, "HR")) {
            // Admin or HR can view all employees
            return employeeRepository.findAll();
        }

        throw new SecurityException("Access Denied");
    }


    public Employee getEmployeeById(Long id, String userRole, String userDepartment) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            if (hasPermission(userRole, "employee:read") &&
                    (hasRole(userRole, "ADMIN") || hasRole(userRole, "HR") ||
                            (hasRole(userRole, "MANAGER") && userDepartment.equals(employee.getDepartment())))) {
                return employee;
            }
        }

        throw new SecurityException("Access Denied");
    }


    public Employee createEmployee(Employee employee, String userRole, String performedBy) {
        if (hasPermission(userRole, "employee:create")) {
            Employee createdEmployee = employeeRepository.save(employee);
            logAuditService.logAudit("CREATE", performedBy, createdEmployee.getId(), "Created new employee");
            return createdEmployee;
        }

        throw new SecurityException("Access Denied");
    }


    public Employee updateEmployee(Long id, Employee updates, String userRole, String userDepartment, String performedBy) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            if (hasPermission(userRole, "employee:update") &&
                    (hasRole(userRole, "ADMIN") || hasRole(userRole, "HR") ||
                            (hasRole(userRole, "MANAGER") && userDepartment.equals(employee.getDepartment())))) {

                String details = "Updated fields: ";
                if (hasRole(userRole, "MANAGER")) {
                    // Managers can only update limited fields
                    employee.setJobTitle(updates.getJobTitle());
                    employee.setContactInfo(updates.getContactInfo());
                    details += "jobTitle, contactInfo";
                } else {
                    // Admins and HR can update all fields
                    employee.setFullName(updates.getFullName());
                    employee.setJobTitle(updates.getJobTitle());
                    employee.setDepartment(updates.getDepartment());
                    employee.setHireDate(updates.getHireDate());
                    employee.setEmploymentStatus(updates.getEmploymentStatus());
                    employee.setContactInfo(updates.getContactInfo());
                    employee.setAddress(updates.getAddress());
                    details += "fullName, jobTitle, department, hireDate, employmentStatus, contactInfo, address";
                }

                Employee updatedEmployee = employeeRepository.save(employee);
                logAuditService.logAudit("UPDATE", performedBy, updatedEmployee.getId(), details);
                return updatedEmployee;
            }
        }

        throw new SecurityException("Access Denied");
    }


    public void deleteEmployee(Long id, String userRole, String performedBy) {
        if (hasPermission(userRole, "employee:delete")) {
            employeeRepository.deleteById(id);
            logAuditService.logAudit("DELETE", performedBy, id, "Deleted employee");
        } else {
            throw new SecurityException("Access Denied");
        }
    }

    // Search employees with pagination
    public Page<Employee> searchEmployees(String name, Long id, String department, String jobTitle, Pageable pageable, String userRole) {
        if (hasPermission(userRole, "employee:read")) {
            return employeeRepository.searchEmployees(name, id, department, jobTitle, pageable);
        }

        throw new SecurityException("Access Denied");
    }

    // Filter employees with pagination
    public Page<Employee> filterEmployees(String status, String department, LocalDate hireDate, Pageable pageable, String userRole) {
        if (hasPermission(userRole, "employee:read")) {
            return employeeRepository.filterEmployees(status, department, hireDate, pageable);
        }

        throw new SecurityException("Access Denied");
    }
}



