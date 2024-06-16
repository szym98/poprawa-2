package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
class StatisticsMapper {


    StatisticsDto toDto(Statistics statistics) {
        return new StatisticsDto(
                statistics.getId(),
                statistics.getUser(),
                statistics.getTotalTrainings(),
                statistics.getTotalDistance(),
                statistics.getTotalCaloriesBurned());
    }

    Statistics toEntity(StatisticsDto statisticsDto, User user) {
        return new Statistics(
                user,
                statisticsDto.getTotalTrainings(),
                statisticsDto.getTotalDistance(),
                statisticsDto.getTotalCaloriesBurned());
    }

}
