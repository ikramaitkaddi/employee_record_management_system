package com.erms.employee_management.controller;

import com.erms.employee_management.dto.EmployeeDTO;
import com.erms.employee_management.entity.Employee;
import com.erms.employee_management.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Test
    public void getAllEmployeesTest() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFullName("John Doe");
        employee.setHireDate(LocalDate.from(LocalDateTime.now()));

        when(employeeService.getAllEmployees("Admin", "IT")).thenReturn(Collections.singletonList(employee));

        List<EmployeeDTO> responseEntity = employeeController.getAllEmployees("Admin", "IT");
        assertEquals(1, responseEntity.size());
        assertEquals("John Doe", responseEntity.get(0).getFullName());

        verify(employeeService, times(1)).getAllEmployees("Admin", "IT");
    }

    @Test
    public void getEmployeeByIdTest() {
        Employee employee = new Employee();
        employee.setId(3L);
        employee.setFullName("John Doe");
        employee.setHireDate(LocalDate.from(LocalDateTime.now()));

        when(employeeService.getEmployeeById(3L, "Admin", "hr")).thenReturn(employee);

        EmployeeDTO responseEntity = employeeController.getEmployeeById(3L, "Admin", "hr");

        assertEquals("John Doe", responseEntity.getFullName());

        //verify(employeeService, times(1)).getEmployeeById(4L, "Admin", "hr");
    }

    @Test
    public void createEmployeeTest() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFullName("John Doe");
        employee.setHireDate(LocalDate.from(LocalDateTime.now()));

        when(employeeService.createEmployee(any(Employee.class), anyString(), anyString())).thenReturn(employee);

        EmployeeDTO responseEntity = employeeController.createEmployee(EmployeeDTO.fromEntity(employee), "Admin", "AdminUser");

        assertEquals("John Doe", responseEntity.getFullName());

        verify(employeeService, times(1)).createEmployee(any(Employee.class), eq("Admin"), eq("AdminUser"));
    }
    @Test
    public void deleteEmployee_Success() {
        Long employeeId = 5L;
        String userRole = "Admin";
        String performedBy = "AdminUser";

        String responseEntity = employeeController.deleteEmployee(employeeId, userRole, performedBy);

        assertEquals("Employee deleted successfully", responseEntity);

        verify(employeeService, times(1)).deleteEmployee(employeeId, userRole, performedBy);
    }
    @Test
    public void searchEmployees_Success() {
        String name = "John";
        Long id = 1L;
        String department = "IT";
        String jobTitle = "Software Engineer";
        String userRole = "Admin";

        Pageable pageable = PageRequest.of(0, 10);

        Page<Employee> mockPage = new PageImpl<>(Collections.singletonList(new Employee()));
        when(employeeService.searchEmployees(name, id, department, jobTitle, pageable, userRole)).thenReturn(mockPage);

       Page<Employee> responseEntity = employeeController.searchEmployees(name, id, department, jobTitle, pageable, userRole);

        assertEquals(1, responseEntity.getContent().size());
    }

    @Test
    public void searchEmployees_NotFound() {
        String name = "John";
        Long id = 1L;
        String department = "IT";
        String jobTitle = "Software Engineer";
        String userRole = "Admin";

        Pageable pageable = PageRequest.of(0, 10);
        when(employeeService.searchEmployees(name, id, department, jobTitle, pageable, userRole)).thenReturn(Page.empty());

        Page<Employee> responseEntity = employeeController.searchEmployees(name, id, department, jobTitle, pageable, userRole);
        assertEquals(0, responseEntity.getContent().size());
    }
    @Test
    public void filterEmployees_Success() {
        String status = "Active";
        String department = "IT";
        LocalDate hireDate = LocalDate.of(2021, 6, 1);
        String userRole = "Admin";

        Pageable pageable = PageRequest.of(0, 10);

        Page<Employee> mockPage = new PageImpl<>(Collections.singletonList(new Employee()));
        when(employeeService.filterEmployees(status, department, hireDate, pageable, userRole)).thenReturn(mockPage);

        Page<Employee> responseEntity = employeeController.filterEmployees(status, department, hireDate, pageable, userRole);

        assertEquals(1, responseEntity.getContent().size());
    }

    @Test
    public void filterEmployees_NotFound() {
        String status = "Inactive";
        String department = "HR";
        LocalDate hireDate = LocalDate.of(2020, 1, 1);
        String userRole = "Admin";

        Pageable pageable = PageRequest.of(0, 10);
        when(employeeService.filterEmployees(status, department, hireDate, pageable, userRole)).thenReturn(Page.empty());

        Page<Employee> responseEntity = employeeController.filterEmployees(status, department, hireDate, pageable, userRole);

        assertEquals(0, responseEntity.getContent().size());
    }
}
