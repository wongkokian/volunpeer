package com.project.volunpeer_be.connection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionUpcomingQuestShift {
    private String startDateTime;
    private String endDateTime;
    private Integer availableSlots;
    private List<String> attendingConnections;
}