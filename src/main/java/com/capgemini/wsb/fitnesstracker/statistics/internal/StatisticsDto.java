package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import jakarta.annotation.Nullable;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString

/*
record StatisticsDto(@Nullable Long id,
               User user,
               int totalTrainings,
               double totalDistance,
               int totalCaloriesBurned) {
}
*/


public class StatisticsDto implements Serializable
{
    private Long id;
    private User user;
    private int totalTrainings;
    private double totalDistance;
    private int totalCaloriesBurned;
}
