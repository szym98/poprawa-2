package com.capgemini.wsb.fitnesstracker.statistics.api;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;


/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface StatisticsService {

    Statistics createStatistics(Statistics statistics);
    void deleteStatisticsById(Long id);
    Statistics updateStatistics(Long statisticsId, Statistics statistics);
    Statistics updateStatistics(Long statisticsId, User user, Statistics statisticsUpdates);

}
