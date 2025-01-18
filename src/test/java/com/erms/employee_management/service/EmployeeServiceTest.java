package com.erms.employee_management.service;

import com.erms.employee_management.entity.ContactInfo;
import com.erms.employee_management.entity.Employee;
import com.erms.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LogAuditService logAuditService;

    @Test
    public void testGetAllEmployees_AdminOrHR_ReturnsAllEmployees() {

        String userRole = "ADMIN";
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(new Employee(), new Employee()));


        List<Employee> employees = employeeService.getAllEmployees(userRole, null);


        assertEquals(2, employees.size());
    }

    @Test
    public void testGetAllEmployees_ManagerWithDepartment_ReturnsFilteredEmployees() {

        String userRole = "MANAGER";
        String userDepartment = "Sales";
        Employee emp1 = new Employee();
        emp1.setDepartment("Sales");
        Employee emp2 = new Employee();
        emp2.setDepartment("HR");
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2));


        List<Employee> employees = employeeService.getAllEmployees(userRole, userDepartment);


        assertEquals(1, employees.size());
        assertEquals("Sales", employees.get(0).getDepartment());
    }

    @Test
    public void testGetAllEmployees_AccessDenied_ThrowsSecurityException() {

        String userRole = "EMPLOYEE";


        assertThrows(SecurityException.class, () -> employeeService.getAllEmployees(userRole, null));
    }

    @Test
    public void testGetEmployeeById_AccessAllowed() {

        Long id = 1L;
        String userRole = "MANAGER";
        String userDepartment = "Sales";
        Employee employee = new Employee();
        employee.setDepartment("Sales");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));


        Employee result = employeeService.getEmployeeById(id, userRole, userDepartment);


        assertNotNull(result);
        assertEquals("Sales", result.getDepartment());
    }

    @Test
    public void testGetEmployeeById_AccessDenied() {

        Long id = 1L;
        String userRole = "EMPLOYEE";


        assertThrows(SecurityException.class, () -> employeeService.getEmployeeById(id, userRole, null));
    }

    @Test
    public void testCreateEmployee_AccessAllowed() {

        String userRole = "ADMIN";
        Employee newEmployee = new Employee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);


        Employee createdEmployee = employeeService.createEmployee(newEmployee, userRole, "performedBy");


        assertNotNull(createdEmployee);
    }

    @Test
    public void testCreateEmployee_AccessDenied() {

        String userRole = "EMPLOYEE";
        Employee newEmployee = new Employee();


        assertThrows(SecurityException.class, () -> employeeService.createEmployee(newEmployee, userRole, "performedBy"));
    }

    @Test
    public void testUpdateEmployee_AccessAllowed() {

        Long id = 1L;
        String userRole = "MANAGER";
        String userDepartment = "Sales";
        Employee updates = new Employee(); updates.setJobTitle("New Title");
        Employee existingEmployee = new Employee(); existingEmployee.setDepartment("Sales");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);


        Employee updatedEmployee = employeeService.updateEmployee(id, updates, userRole, userDepartment, "performedBy");


        assertNotNull(updatedEmployee);
        assertEquals("New Title", updatedEmployee.getJobTitle());
    }

    @Test
    public void testUpdateEmployee_AccessDenied() {

        Long id = 1L;
        String userRole = "EMPLOYEE";


        assertThrows(SecurityException.class, () -> employeeService.updateEmployee(id, new Employee(), userRole, null, "performedBy"));
    }
    @Test
    public void testUpdateEmployee_AllFieldsUpdated() {

        Long id = 1L;
        String userRole = "ADMIN";
        Employee updates = new Employee();
        updates.setFullName("John Doe");
        updates.setJobTitle("Software Engineer");
        updates.setDepartment("IT");
        updates.setHireDate(LocalDate.now());
        updates.setEmploymentStatus("Active");
        updates.setContactInfo(new ContactInfo());
        updates.setAddress("123 Street, City");
        Employee existingEmployee = new Employee();
        existingEmployee.setFullName("Jane Doe");
        existingEmployee.setJobTitle("Manager");
        existingEmployee.setDepartment("HR");
        existingEmployee.setHireDate(LocalDate.now().minusDays(100));
        existingEmployee.setEmploymentStatus("Inactive");
        existingEmployee.setContactInfo(new ContactInfo());
        existingEmployee.setAddress("456 Avenue, City");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);


        Employee updatedEmployee = employeeService.updateEmployee(id, updates, userRole, null, "performedBy");


        assertNotNull(updatedEmployee);
        assertEquals("John Doe", updatedEmployee.getFullName());
        assertEquals("Software Engineer", updatedEmployee.getJobTitle());
        assertEquals("IT", updatedEmployee.getDepartment());
        assertEquals(LocalDate.now(), updatedEmployee.getHireDate());
        assertEquals("Active", updatedEmployee.getEmploymentStatus());
        assertEquals("123 Street, City", updatedEmployee.getAddress());
    }
    @Test
    public void testDeleteEmployee_AccessAllowed() {

        Long id = 1L;
        String userRole = "ADMIN";


        employeeService.deleteEmployee(id, userRole, "performedBy");


        verify(employeeRepository).deleteById(id);
    }

    @Test
    public void testDeleteEmployee_AccessDenied() {

        Long id = 1L;
        String userRole = "EMPLOYEE";


        assertThrows(SecurityException.class, () -> employeeService.deleteEmployee(id, userRole, "performedBy"));
    }
    @Test
    public void testSearchEmployees_WithPermission() {

        String userRole = "HR";
        String name = "John";
        Long id = 1L;
        String department = "IT";
        String jobTitle = "Engineer";
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee();
        employee.setFullName("John Doe");
        Page<Employee> expectedPage = new PageImpl<>(Arrays.asList(employee));

        when(employeeRepository.searchEmployees(name, id, department, jobTitle, pageable)).thenReturn(expectedPage);


        Page<Employee> result = employeeService.searchEmployees(name, id, department, jobTitle, pageable, userRole);


        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getFullName());
        verify(employeeRepository, times(1)).searchEmployees(name, id, department, jobTitle, pageable);
    }

    @Test
    public void testSearchEmployees_WithoutPermission() {

        String userRole = "INTERN";
        String name = "John";
        Long id = 1L;
        String department = "IT";
        String jobTitle = "Engineer";
        Pageable pageable = PageRequest.of(0, 10);


        assertThrows(SecurityException.class, () -> employeeService.searchEmployees(name, id, department, jobTitle, pageable, userRole));
        verify(employeeRepository, never()).searchEmployees(anyString(), anyLong(), anyString(), anyString(), any(Pageable.class));
    }
    @Test
    public void testFilterEmployees_WithPermission() {

        String userRole = "ADMIN";
        String status = "Active";
        String department = "IT";
        LocalDate hireDate = LocalDate.now().minusYears(1);
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee();
        employee.setFullName("Jane Doe");
        Page<Employee> expectedPage = new PageImpl<>(Arrays.asList(employee));

        when(employeeRepository.filterEmployees(status, department, hireDate, pageable)).thenReturn(expectedPage);


        Page<Employee> result = employeeService.filterEmployees(status, department, hireDate, pageable, userRole);


        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Jane Doe", result.getContent().get(0).getFullName());
        verify(employeeRepository, times(1)).filterEmployees(status, department, hireDate, pageable);
    }

    @Test
    public void testFilterEmployees_WithoutPermission() {

        String userRole = "INTERN";
        String status = "Active";
        String department = "IT";
        LocalDate hireDate = LocalDate.now().minusYears(1);
        Pageable pageable = PageRequest.of(0, 10);


        assertThrows(SecurityException.class, () -> employeeService.filterEmployees(status, department, hireDate, pageable, userRole));
        verify(employeeRepository, never()).filterEmployees(anyString(), anyString(), any(LocalDate.class), any(Pageable.class));
    }
}

