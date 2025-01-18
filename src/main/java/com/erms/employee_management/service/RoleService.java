package com.erms.employee_management.service;

import com.erms.employee_management.entity.Employee;
import com.erms.employee_management.entity.Role;
import com.erms.employee_management.repository.EmployeeRepository;
import com.erms.employee_management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public void assignRoleToEmployee(Long employeeId, Long roleId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Optional<Role> role = roleRepository.findById(roleId);

        if (employee.isPresent() && role.isPresent()) {
            employee.get().getRoles().add(role.get());
            employeeRepository.save(employee.get());
        } else {
            throw new IllegalArgumentException("Employee or Role not found");
        }
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role getRoleById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role updateRole(Long id, Role updatedRole) {
        Role existingRole = getRoleById(id);
        existingRole.setRoleName(updatedRole.getRoleName());
        existingRole.setPermissions(updatedRole.getPermissions());
        return roleRepository.save(existingRole);
    }

    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }
}

