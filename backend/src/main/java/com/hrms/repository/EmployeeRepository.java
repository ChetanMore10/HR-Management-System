package com.hrms.repository;

import com.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);
    List<Employee> findByNameContainingIgnoreCase(String name);
}