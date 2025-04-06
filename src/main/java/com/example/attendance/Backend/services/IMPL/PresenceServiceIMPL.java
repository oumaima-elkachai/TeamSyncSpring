package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.services.interfaces.PresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
public class PresenceServiceIMPL implements PresenceService {

    @Autowired
    private PresenceRepository presenceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(PresenceServiceIMPL.class);

    @Override
    public List<Presence> getAllAttendances() {
        return presenceRepository.findAll();
    }

    @Override
    public Presence getAttendanceById(String id) {
        return presenceRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance not found: " + id));
    }

   public Presence saveAttendance(Presence presence) {
        logger.info("Saving presence for employee: {}", presence.getEmployeeId());

        // Conversion manuelle si nécessaire
        if (presence.getCheckInTime() != null && presence.getCheckOutTime() != null) {
            // Calcul des heures travaillées
            Duration duration = Duration.between(
                    LocalTime.parse(presence.getCheckInTime().toString()),
                    LocalTime.parse(presence.getCheckOutTime().toString())
            );
            presence.setHoursWorked(duration.toHours());
        }
        return presenceRepository.save(presence);
    }
     /*@Override
    public Presence saveAttendance(Presence presence)  {
        // Sauvegarder le payroll
        Presence savedPayroll = presenceRepository.save(presence);

        // Ajouter le payroll à l'employé
        Employee employee = employeeRepository.findById(presence.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Ajouter le payroll à la liste des payrolls de l'employé
        employee.getAttendances().add(savedPayroll);

        // Sauvegarder l'employé avec le nouveau payroll
        employeeRepository.save(employee);

        return savedPayroll;
    }*/




    @Override
    public Presence updateAttendance(String id, Presence presenceDetails) {
        Presence presence = getAttendanceById(id);
        presence.setDate(presenceDetails.getDate());
        presence.setCheckInTime(presenceDetails.getCheckInTime());
        presence.setCheckOutTime(presenceDetails.getCheckOutTime());
        //presence.setEmployeeId(presenceDetails.getEmployeeId());
        calculateWorkedHours(presence);
        return presenceRepository.save(presence);
    }

    private void calculateWorkedHours(Presence presence) {
        if (presence.getCheckInTime() != null && presence.getCheckOutTime() != null) {
            Duration duration = Duration.between(presence.getCheckInTime(), presence.getCheckOutTime());
            double hours = duration.toMinutes() / 60.0;
            presence.setHoursWorked(hours);
            presence.setOvertimeHours(Math.max(0, hours - 8));
        }
    }

    @Override
    public void deleteAttendance(String id) {
        presenceRepository.deleteById(id);
    }
}

