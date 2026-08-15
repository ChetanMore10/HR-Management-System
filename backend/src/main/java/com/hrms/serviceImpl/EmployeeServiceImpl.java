package com.hrms.serviceImpl;

import com.hrms.dto.EmployeeRequest;
import com.hrms.dto.EmployeeResponse;
import com.hrms.entity.Employee;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Employee email already exists");
        }

        Employee employee = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .dateOfJoining(request.getDateOfJoining())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        return mapToResponse(savedEmployee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + id)
                );

        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request
    ) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + id)
                );

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setDateOfJoining(request.getDateOfJoining());

        Employee updatedEmployee =
                employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + id)
                );

        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeResponse> searchEmployees(String name) {

        return employeeRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EmployeeResponse mapToResponse(Employee employee) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .department(employee.getDepartment())
                .designation(employee.getDesignation())
                .dateOfJoining(employee.getDateOfJoining())
                .build();
    }
}