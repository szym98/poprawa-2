package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/statistics")
@RequiredArgsConstructor
class StatisticsController {

    private final StatisticsServiceImpl statisticsService;
    private final StatisticsMapper statisticsMapper;
    private final UserServiceImpl userService;

    @GetMapping
    public List<StatisticsDto> getAllStatistics() {
        return statisticsService.findAllStatistics()
                                .stream()
                                .map(statisticsMapper::toDto)
                                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public List<StatisticsDto> getStatisticsById(@PathVariable final Long id) {
        return statisticsService.getStatistics(id)
                                .stream()
                                .map(statisticsMapper::toDto)
                                .collect(Collectors.toList());
    }

    // curl -X GET "http://localhost:8080/v1/statistics/user/1"
    @GetMapping("/user/{id}")
    public List<StatisticsDto> getStatisticsByUserId(@PathVariable final Long id) {
        return statisticsService.getStatisticsByUserId(id)
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());
    }


    // curl -X GET "http://localhost:8080/v1/statistics/calories-gt?calories=10"
    @GetMapping("/calories-gt")
    public List<StatisticsDto> getStatisticsWhereCaloriesAreGreaterThan(@RequestParam final int calories) {
        return statisticsService.getStatisticsWhereCaloriesAreGreaterThan(calories)
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());
    }

    // curl -X GET "http://localhost:8080/v1/statistics/calories-gt2/10"
    @GetMapping("/calories-gt2/{calories}")
    public List<StatisticsDto> getStatisticsWhereCaloriesAreGreaterThan2(@PathVariable final int calories) {
        return statisticsService.getStatisticsWhereCaloriesAreGreaterThan(calories)
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());
    }


    // Windows curl -X POST  http://localhost:8080/v1/statistics -H "Content-Type: application/json" -d "{\"id\": \"1\", \"totalTrainings\": \"3\", \"totalDistance\": \"12.3\", \"totalCaloriesBurned\": \"2345\"}"
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addStatistics(@RequestBody StatisticsDto statisticsDto) {

        Statistics newStatistics = statisticsService.createStatistics(
                statisticsMapper.toEntity(statisticsDto,
                        Objects.requireNonNull(userService.getUser(statisticsDto.getId()).orElse(null)) ));

        URI location = URI.create("/v1/statistics" + newStatistics.getId());
        return ResponseEntity.created(location).body(newStatistics);
    }


    // Windows curl -X PUT  http://localhost:8080/v1/statistics/4 -H "Content-Type: application/json" -d "{\"id\": \"1\", \"totalTrainings\": \"5\", \"totalDistance\": \"13.4\", \"totalCaloriesBurned\": \"1234\"}"
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatistics(@PathVariable final Long id, @RequestBody StatisticsDto statisticsDto) {

        Statistics updatedStatistics = statisticsService.updateStatistics(id,
                statisticsMapper.toEntity(statisticsDto,
                        Objects.requireNonNull(userService.getUser(statisticsDto.getId()).orElse(null)) ));

        return ResponseEntity.ok(statisticsMapper.toDto(updatedStatistics));
    }

    // curl -X DELETE http://localhost:8080/v1/statistics/4
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  delStatistics(@PathVariable final Long id) {
        statisticsService.deleteStatisticsById(id);
        return ResponseEntity.noContent().build();
    }
}
