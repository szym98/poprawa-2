package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;
    private final UserServiceImpl userService;

    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings()
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public List<TrainingDto> getTrainingById(@PathVariable final Long id) {
        return trainingService.getTraining(id)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    // curl -X GET "http://localhost:8080/v1/trainings/user/1"
    @GetMapping("/user/{id}")
    public List<TrainingDto> getTrainingByUserId(@PathVariable final Long id) {
        return trainingService.getTrainingsByUserId(id)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    // curl -X GET "http://localhost:8080/v1/trainings/after/2024-01-18"
    @GetMapping("/after/{dateTrainingsAfterDate}")
    public List<TrainingDto> getTrainingAfterDate(@PathVariable final String dateTrainingsAfterDate) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = format.parse(dateTrainingsAfterDate);
        } catch (Exception e) {}

        if (date == null) {
            date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        return trainingService.getTrainingsAfterDate(date)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    // curl -X GET "http://localhost:8080/v1/trainings/activities"
    @GetMapping("/activities")
    public ActivityType[] getActivities() {
        return ActivityType.values();
    }


    // curl -X GET "http://localhost:8080/v1/trainings/activity?activity=RUNNING"
    @GetMapping("/activity")
    public List<TrainingDto> getTrainingByActivities(@RequestParam final ActivityType activity) {

        log.info("log.info -> trainingService.findTrainingByActivities");

        return trainingService.getTrainingsByActivities(activity)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }


    // Windows curl -X POST  http://localhost:8080/v1/trainings -H "Content-Type: application/json" -d "{\"id\": \"1\", \"startTime\": \"2024-05-05T12:00:00\", \"endTime\": \"2024-05-05T12:45:00\", \"activityType\": \"CYCLING\", \"distance\": \"12.3\", \"averageSpeed\": \"23.45\"}"
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Training> addTraining(@RequestBody TrainingDto trainingDto) {

        Training newTraining = trainingService.createTraining(
                trainingMapper.toEntity(trainingDto,
                        Objects.requireNonNull(userService.getUser(trainingDto.getId()).orElse(null)) ));

        URI location = URI.create("/v1/trainings" + newTraining.getId());

        return ResponseEntity.created(location).body(newTraining);
    }


    // Windows curl -X PUT  http://localhost:8080/v1/trainings/11 -H "Content-Type: application/json" -d "{\"id\": \"1\", \"startTime\": \"2024-05-06T13:00:00\", \"endTime\": \"2024-05-06T13:45:00\", \"activityType\": \"CYCLING\", \"distance\": \"23.4\", \"averageSpeed\": \"34.5\"}"
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(@PathVariable final Long id, @RequestBody TrainingDto trainingDto) {

        Training updateTraining = trainingService.updateTraining(id,
                trainingMapper.toEntity(trainingDto,
                        Objects.requireNonNull(userService.getUser(trainingDto.getId()).orElse(null)) ));

        return ResponseEntity.ok(trainingMapper.toDto(updateTraining));
    }

    // curl -X DELETE http://localhost:8080/v1/trainings/11
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  delTraining(@PathVariable final Long id) {
        trainingService.deleteTraining(id);
        return ResponseEntity.noContent().build();
    }
}
