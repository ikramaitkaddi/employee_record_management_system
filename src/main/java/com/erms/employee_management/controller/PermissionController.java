package com.erms.employee_management.controller;

import com.erms.employee_management.entity.Permission;
import com.erms.employee_management.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/permissions")
@Tag(name = "Permission Management", description = "APIs for managing permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Operation(summary = "Create a new permission", description = "Creates a new permission record in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Permission created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public Permission createPermission(
            @Parameter(description = "Permission details to be created", required = true)
            @RequestBody Permission permission) {
        return permissionService.createPermission(permission);
    }

    @Operation(summary = "Get permission by ID", description = "Fetches a permission record by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission found"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Permission getPermissionById(
            @Parameter(description = "ID of the permission to fetch", required = true)
            @PathVariable Long id) {
        return permissionService.getPermissionById(id);
    }

    @Operation(summary = "Update a permission", description = "Updates an existing permission record by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission updated successfully"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public Permission updatePermission(
            @Parameter(description = "ID of the permission to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated permission details", required = true)
            @RequestBody Permission updatedPermission) {
        return permissionService.updatePermission(id, updatedPermission);
    }

    @Operation(summary = "Delete a permission", description = "Deletes a permission record by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public void deletePermission(
            @Parameter(description = "ID of the permission to delete", required = true)
            @PathVariable Long id) {
        permissionService.deletePermission(id);
    }
}
