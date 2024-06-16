package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.*;
import java.util.stream.Collectors;

interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    default List<Statistics> findStatisticsByUserId(Long Id) {
        return findAll().stream()
                .filter(statistics -> Objects.equals(statistics.getUser().getId(), Id))
                .collect(Collectors.toList());
    }

    default List<Statistics> findStatisticsWhereCaloriesAreGreaterThan(int calories) {
        return findAll().stream()
                .filter(statistics -> statistics.getTotalCaloriesBurned() > calories)
                .collect(Collectors.toList());
    }

}
