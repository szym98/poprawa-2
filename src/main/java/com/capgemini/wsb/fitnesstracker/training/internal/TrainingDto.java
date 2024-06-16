package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import jakarta.annotation.Nullable;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@ToString

/*
record TrainingDto(@Nullable Long id,
                User user,
                //@JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date startTime,
                //@JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date endTime,
                Date startTime,
                Date endTime,
                ActivityType activityType,
                double distance,
                double averageSpeed) {
}
*/

public class TrainingDto implements Serializable
{
    private Long id;
    private User user;
    private Date startTime;
    private Date endTime;
    private ActivityType activityType;
    private double distance;
    private double averageSpeed;
}
