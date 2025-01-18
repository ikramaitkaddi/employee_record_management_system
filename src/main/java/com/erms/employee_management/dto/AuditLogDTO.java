package com.erms.employee_management.dto;

import com.erms.employee_management.entity.AuditLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private String action;
    private String performedBy;
    private String timestamp; // Use String for consistent format
    private Long employeeId;
    private String details;

    // Map from Entity to DTO
    public static AuditLogDTO fromEntity(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setAction(auditLog.getAction());
        dto.setPerformedBy(auditLog.getPerformedBy());
        dto.setTimestamp(auditLog.getTimestamp().toString());
        dto.setEmployeeId(auditLog.getEmployeeId());
        dto.setDetails(auditLog.getDetails());
        return dto;
    }

    // Map from DTO to Entity (if needed)
    public static AuditLog toEntity(AuditLogDTO dto) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(dto.getAction());
        auditLog.setPerformedBy(dto.getPerformedBy());
        auditLog.setTimestamp(LocalDateTime.parse(dto.getTimestamp()));
        auditLog.setEmployeeId(dto.getEmployeeId());
        auditLog.setDetails(dto.getDetails());
        return auditLog;
    }
}

