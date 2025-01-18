package com.erms.employee_management.controller;

import com.erms.employee_management.entity.Role;
import com.erms.employee_management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@Tag(name = "Role Management", description = "APIs for managing roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Assign role to an employee", description = "Assigns a specific role to an employee by their IDs.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Employee or role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/assign")
    public String assignRole(
            @Parameter(description = "ID of the employee to assign the role to", required = true) @RequestParam Long employeeId,
            @Parameter(description = "ID of the role to assign", required = true) @RequestParam Long roleId) {
        roleService.assignRoleToEmployee(employeeId, roleId);
        return "Role assigned successfully";
    }

    @Operation(summary = "Create a new role", description = "Creates a new role in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public Role createRole(
            @Parameter(description = "Role details to be created", required = true) @RequestBody Role role) {
        return roleService.createRole(role);
    }

    @Operation(summary = "Get role by ID", description = "Fetches details of a specific role by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role found"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Role getRoleById(
            @Parameter(description = "ID of the role to fetch", required = true) @PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @Operation(summary = "Update an existing role", description = "Updates a role by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public Role updateRole(
            @Parameter(description = "ID of the role to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated role details", required = true) @RequestBody Role updatedRole) {
        return roleService.updateRole(id, updatedRole);
    }

    @Operation(summary = "Delete a role", description = "Deletes a role by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public void deleteRole(
            @Parameter(description = "ID of the role to delete", required = true) @PathVariable Long id) {
        roleService.deleteRole(id);
    }
}
