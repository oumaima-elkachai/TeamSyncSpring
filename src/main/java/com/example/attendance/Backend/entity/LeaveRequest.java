package com.example.attendance.Backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    // Getter et Setter pour id
    @Getter
    @Id
    private String id;
    private String employeeId;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String justification;

    public void setId(String id) {
        this.id = id;
    }

}
