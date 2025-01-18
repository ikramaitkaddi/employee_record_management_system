package com.erms.employee_management.service;

import com.erms.employee_management.entity.AuditLog;
import com.erms.employee_management.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class LogAuditService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    void logAudit(String action, String performedBy, Long employeeId, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setPerformedBy(performedBy);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setEmployeeId(employeeId);
        auditLog.setDetails(details);

        auditLogRepository.save(auditLog);
    }
}
