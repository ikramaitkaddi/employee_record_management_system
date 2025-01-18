package com.erms.employee_management.service;

import com.erms.employee_management.entity.Permission;
import com.erms.employee_management.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission getPermissionById(Long id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        return permissionOptional.orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    public Permission updatePermission(Long id, Permission updatedPermission) {
        Permission existingPermission = getPermissionById(id);
        existingPermission.setPermissionName(updatedPermission.getPermissionName());
        return permissionRepository.save(existingPermission);
    }

    public void deletePermission(Long id) {
        Permission permission = getPermissionById(id);
        permissionRepository.delete(permission);
    }
}