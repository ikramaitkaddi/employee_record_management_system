package com.erms.employee_management.controller;

import com.erms.employee_management.dto.EmployeeDTO;
import com.erms.employee_management.entity.Employee;
import com.erms.employee_management.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Get all employees", description = "Fetches a list of all employees.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched employees"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public List<EmployeeDTO> getAllEmployees(
            @Parameter(description = "User's role for permission validation", required = true) @RequestParam String userRole,
            @Parameter(description = "Optional filter by user department") @RequestParam(required = false) String userDepartment) {
        return employeeService.getAllEmployees(userRole, userDepartment)
                .stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get employee by ID", description = "Fetches details of a specific employee by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", content = @Content)
    })
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(
            @Parameter(description = "Employee ID to fetch", required = true) @PathVariable Long id,
            @Parameter(description = "User's role for permission validation", required = true) @RequestParam String userRole,
            @Parameter(description = "Optional filter by user department") @RequestParam(required = false) String userDepartment) {
        return EmployeeDTO.fromEntity(employeeService.getEmployeeById(id, userRole, userDepartment));
    }

    @Operation(summary = "Create a new employee", description = "Creates a new employee record.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", content = @Content)
    })
    @PostMapping
    public EmployeeDTO createEmployee(
            @Parameter(description = "Employee details for creation", required = true) @RequestBody EmployeeDTO employeeDTO,
            @Parameter(description = "User's role for permission validation", required = true) @RequestParam String userRole,
            @Parameter(description = "Identifier of the user performing the action", required = true) @RequestParam String performedBy) {
        return EmployeeDTO.fromEntity(
                employeeService.createEmployee(EmployeeDTO.toEntity(employeeDTO), userRole, performedBy)
        );
    }

    @Operation(summary = "Update an existing employee", description = "Updates details of an employee by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", content = @Content)
    })
    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(
            @Parameter(description = "Employee ID to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated employee details", required = true) @RequestBody EmployeeDTO employeeDTO,
            @Parameter(description = "User's role for permission validation", required = true) @RequestParam String userRole,
            @Parameter(description = "Optional filter by user department") @RequestParam(required = false) String userDepartment,
            @Parameter(description = "Identifier of the user performing the action", required = true) @RequestParam String performedBy) {
        return EmployeeDTO.fromEntity(
                employeeService.updateEmployee(id, EmployeeDTO.toEntity(employeeDTO), userRole, userDepartment, performedBy)
        );
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", content = @Content)
    })
    @DeleteMapping("/{id}")
    public String deleteEmployee(
            @Parameter(description = "Employee ID to delete", required = true) @PathVariable Long id,
            @Parameter(description = "User's role for permission validation", required = true) @RequestParam String userRole,
            @Parameter(description = "Identifier of the user performing the action", required = true) @RequestParam String performedBy) {
        employeeService.deleteEmployee(id, userRole, performedBy);
        return "Employee deleted successfully";
    }

    @Operation(summary = "Search employees with pagination", description = "Search employees by various criteria with pagination.")
    @GetMapping("/search")
    public Page<Employee> searchEmployees(
            @Parameter(description = "Filter by name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by ID") @RequestParam(required = false) Long id,
            @Parameter(description = "Filter by department") @RequestParam(required = false) String department,
            @Parameter(description = "Filter by job title") @RequestParam(required = false) String jobTitle,
            Pageable pageable,
            @Parameter(description = "User's role for permission validation", required = true) @RequestHeader("User-Role") String userRole) {
        return employeeService.searchEmployees(name, id, department, jobTitle, pageable, userRole);
    }

    @Operation(summary = "Filter employees with pagination", description = "Filter employees by various criteria with pagination.")
    @GetMapping("/filter")
    public Page<Employee> filterEmployees(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by department") @RequestParam(required = false) String department,
            @Parameter(description = "Filter by hire date") @RequestParam(required = false) LocalDate hireDate,
            Pageable pageable,
            @Parameter(description = "User's role for permission validation", required = true) @RequestHeader("User-Role") String userRole) {
        return employeeService.filterEmployees(status, department, hireDate, pageable, userRole);
    }
}
