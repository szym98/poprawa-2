package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsProvider;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsService;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class StatisticsServiceImpl implements StatisticsService, StatisticsProvider {

    private final StatisticsRepository statisticsRepository;

    @Override
    public Statistics createStatistics(final Statistics statistics) {
        log.info("Creating Statistics {}", statistics);
        if (statistics.getId() != null) {
            throw new IllegalArgumentException("Statistics has already DB ID, update is not permitted!");
        }

        return statisticsRepository.save(statistics);
    }

    @Override
    public Optional<Statistics> getStatistics(final Long statisticsId) {
        return statisticsRepository.findById(statisticsId);
    }

    @Override
    public List<Statistics> getStatisticsByUserId(final Long userId) {
        return statisticsRepository.findStatisticsByUserId(userId);
    }

    @Override
    public List<Statistics> getStatisticsWhereCaloriesAreGreaterThan(final int calories) {
        return statisticsRepository.findStatisticsWhereCaloriesAreGreaterThan(calories);
    }


    @Override
    public void deleteStatisticsById(Long id){
        log.info("log.info -> statisticsService.deleteStatisticsById {}", id);
        statisticsRepository.deleteById(id);
    };


    @Override
    public Statistics updateStatistics(Long  statisticsId, Statistics statisticsUpdates) {

        log.info("Updating Statistics (2) {} ", statisticsUpdates);

        statisticsUpdates.setId(statisticsId);
        return statisticsRepository.save(statisticsUpdates);
    };

    @Override
    public Statistics updateStatistics(Long  statisticsId, User user, Statistics statisticsUpdates) {

        log.info("Updating Statistics (3) {} ", statisticsUpdates);

        statisticsUpdates.setId(statisticsId);
        statisticsUpdates.setUser(user);
        return statisticsRepository.save(statisticsUpdates);
    };


    //@Override
    public List<Statistics> findAllStatistics() {
        return statisticsRepository.findAll();
    }

}