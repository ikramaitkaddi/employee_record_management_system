package com.erms.employee_management.repository;

import com.erms.employee_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Search with pagination
    @Query("SELECT e FROM Employee e WHERE " +
            "(:name IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:id IS NULL OR e.id = :id) AND " +
            "(:department IS NULL OR LOWER(e.department) = LOWER(:department)) AND " +
            "(:jobTitle IS NULL OR LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :jobTitle, '%')))")
    Page<Employee> searchEmployees(@Param("name") String name,
                                   @Param("id") Long id,
                                   @Param("department") String department,
                                   @Param("jobTitle") String jobTitle,
                                   Pageable pageable);

    // Filter with pagination
    @Query("SELECT e FROM Employee e WHERE " +
            "(:status IS NULL OR LOWER(e.employmentStatus) = LOWER(:status)) AND " +
            "(:department IS NULL OR LOWER(e.department) = LOWER(:department)) AND " +
            "(:hireDate IS NULL OR e.hireDate = :hireDate)")
    Page<Employee> filterEmployees(@Param("status") String employmentStatus,
                                   @Param("department") String department,
                                   @Param("hireDate") LocalDate hireDate,
                                   Pageable pageable);
}
