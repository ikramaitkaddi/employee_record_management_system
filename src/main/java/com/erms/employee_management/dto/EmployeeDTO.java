package com.erms.employee_management.dto;

import com.erms.employee_management.entity.ContactInfo;
import com.erms.employee_management.entity.Employee;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    private String fullName;
    private String jobTitle;
    private String department;
    private String hireDate; // Use String for simplified date handling
    private String employmentStatus;
    private ContactInfo contactInfo;
    private String address;

    // Map from Entity to DTO
    public static EmployeeDTO fromEntity(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFullName(employee.getFullName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setDepartment(employee.getDepartment());
        dto.setHireDate(employee.getHireDate().toString());
        dto.setEmploymentStatus(employee.getEmploymentStatus());
        dto.setContactInfo(employee.getContactInfo());
        dto.setAddress(employee.getAddress());
        return dto;
    }

    // Map from DTO to Entity
    public static Employee toEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFullName(dto.getFullName());
        employee.setJobTitle(dto.getJobTitle());
        employee.setDepartment(dto.getDepartment());
        employee.setHireDate(LocalDate.parse(dto.getHireDate())); // Assuming ISO format
        employee.setEmploymentStatus(dto.getEmploymentStatus());
        employee.setContactInfo(dto.getContactInfo());
        employee.setAddress(dto.getAddress());
        return employee;
    }
}

