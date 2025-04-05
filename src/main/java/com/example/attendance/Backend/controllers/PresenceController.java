// CONTROLLER
package com.example.attendance.Backend.controllers;

import com.example.attendance.Backend.dto.PresenceDTO;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.services.interfaces.EmployeeService;
import com.example.attendance.Backend.services.interfaces.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/attendances")
public class PresenceController {

    @Autowired
    private PresenceService presenceService;
    @Autowired  // Ajoutez cette annotation
    private EmployeeService employeeService;



    @GetMapping("/all")
    public ResponseEntity<?> getAllAttendances() {
        try {
            List<Presence> presences = presenceService.getAllAttendances();
            List<PresenceDTO> dtos = presences.stream()
                    .map(p -> {
                        PresenceDTO dto = new PresenceDTO(p);
                        // Gestion des employeeId null
                        if (p.getEmployeeId() != null) {
                            dto.setEmployeeName(employeeService.getEmployeeName(p.getEmployeeId()));
                        } else {
                            dto.setEmployeeName("Employé non spécifié");
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching attendances: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Presence> getAttendanceById(@PathVariable String id) {
        Presence presence = presenceService.getAttendanceById(id);
        return ResponseEntity.ok(presence);
    }

    @PostMapping("/saveAttendance")
    public ResponseEntity<?> saveAttendance(@RequestBody Presence presence) {
        // Debug avant sauvegarde


        Presence saved = presenceService.saveAttendance(presence);

        // Debug après sauvegarde
        System.out.println("EmployeeId sauvegardé: " + saved.getEmployeeId());

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Presence> updateAttendance(@PathVariable String id, @RequestBody Presence presenceDetails) {
        Presence updated = presenceService.updateAttendance(id, presenceDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteAttendance(@PathVariable String id) {
        presenceService.deleteAttendance(id);
    }


}
