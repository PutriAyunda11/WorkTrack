package com.example.worktrack.repository;

import com.example.worktrack.model.Employee;
import com.example.worktrack.model.EmployeeWorkSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeWorkScheduleRepository extends JpaRepository<EmployeeWorkSchedule, Integer> {
    Optional<EmployeeWorkSchedule> findByWorkDateAndEmployee(LocalDate workDate, Employee emp);

    @Query("""
            SELECT s FROM EmployeeWorkSchedule s 
            LEFT JOIN s.employee e 
            WHERE (:employeeId IS NULL OR s.employee.id = :employeeId)
            AND (:employeeName IS NULL OR LOWER(e.name) LIKE LOWER( CONCAT('%', CAST(:employeeName AS string), '%')))
            AND (:workDate IS NULL OR s.workDate = :workDate)
            AND (:startDate IS NULL OR s.workDate >= :startDate)
            AND (:endDate IS NULL OR s.workDate <= :endDate)
            """)
    Page<EmployeeWorkSchedule> searchSchedules(
            @Param("employeeId") Integer employeeId,
            @Param("employeeName") String employeeName,
            @Param("workDate") LocalDate workDate,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    List<EmployeeWorkSchedule> findByEmployee_Id(Integer employeeId);
}
