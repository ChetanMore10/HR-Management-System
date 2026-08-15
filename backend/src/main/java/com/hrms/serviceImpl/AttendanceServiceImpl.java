package com.hrms.serviceImpl;

import com.hrms.dto.AttendanceRequest;
import com.hrms.dto.AttendanceResponse;
import com.hrms.entity.Attendance;
import com.hrms.entity.AttendanceStatus;
import com.hrms.entity.Employee;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.AttendanceService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository) {

        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    private AttendanceResponse mapToResponse(
            Attendance attendance) {

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(attendance.getEmployee().getName())
                .attendanceDate(attendance.getAttendanceDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .status(attendance.getStatus().name())
                .build();
    }

    private AttendanceStatus getAttendanceStatus(String status) {

        try {
            return AttendanceStatus.valueOf(status.trim().toUpperCase());

        } catch (IllegalArgumentException e) {

            throw new RuntimeException("Invalid attendance status: "
                    + status + ". Allowed values: PRESENT, ABSENT, HALF_DAY, LEAVE");
        }
    }

    @Override
    public AttendanceResponse createAttendance(
            AttendanceRequest request) {

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + request.getEmployeeId()));

        AttendanceStatus status = getAttendanceStatus(request.getStatus());

        Attendance attendance = Attendance.builder()
                .employee(employee).attendanceDate(request.getAttendanceDate())
                .checkInTime(request.getCheckInTime())
                .checkOutTime(request.getCheckOutTime())
                .status(status)
                .build();

        Attendance savedAttendance = attendanceRepository.save(attendance);

        return mapToResponse(savedAttendance);
    }

    @Override
    public AttendanceResponse getAttendanceById(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attendance not found with id: " + id));

        return mapToResponse(attendance);
    }

    @Override
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByEmployee(
            Long employeeId) {

        return attendanceRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByDate(
            LocalDate date) {

        return attendanceRepository
                .findByAttendanceDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AttendanceResponse updateAttendance(
            Long id,
            AttendanceRequest request) {

        Attendance attendance = attendanceRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attendance not found with id: " + id));

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + request.getEmployeeId()));

        AttendanceStatus status = getAttendanceStatus(request.getStatus());

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());
        attendance.setStatus(status);

        Attendance updatedAttendance = attendanceRepository.save(attendance);

        return mapToResponse(updatedAttendance);
    }

    @Override
    public void deleteAttendance(Long id) {

        Attendance attendance = attendanceRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attendance not found with id: " + id));

        attendanceRepository.delete(attendance);
    }
}