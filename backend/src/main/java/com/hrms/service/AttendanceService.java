package com.hrms.service;

import com.hrms.dto.AttendanceRequest;
import com.hrms.dto.AttendanceResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AttendanceService {

    AttendanceResponse createAttendance(AttendanceRequest request);

    AttendanceResponse getAttendanceById(Long id);

    List<AttendanceResponse> getAllAttendance();

    List<AttendanceResponse> getAttendanceByEmployee(Long employeeId);

    List<AttendanceResponse> getAttendanceByDate(LocalDate date);

    AttendanceResponse updateAttendance(
            Long id,
            AttendanceRequest request
    );

    void deleteAttendance(Long id);
}
