package com.capgemini.wsb.fitnesstracker.notification.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsProvider;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@EnableScheduling
@Service
@Slf4j
public class ReportToEmail {
    private static final String TITLE = "Training report";

    private  final JavaMailSender javaMailSender;
    private  final UserProvider userProvider;
    private  final TrainingProvider trainingProvider;
    private  final StatisticsProvider statisticsProvider;

    //    second, minute, hour, day, month, weekday
    //
    //    0 0 * * * * = the top of every hour of every day.
    //    */10 * * * * * = every ten seconds.
    //    0 0 8-10 * * * = 8, 9 and 10 o'clock of every day.
    //    0 0 6,19 * * * = 6:00 AM and 7:00 PM every day.
    //    0 0/30 8-10 * * * = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
    //    0 0 9-17 * * MON-FRI = on the hour nine-to-five weekdays
    //    0 0 0 25 12 ? = every Christmas Day at midnight
    //    https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
    // co minute
    //@Scheduled(cron = "0 * * * * *")
    // co godzine
    //@Scheduled(cron = "0 0 * * * *")
    // raz na tydzien
    @Scheduled(cron = "0 0 0 * * 0")
    public void generateReport() {
        log.info("Starting generation of training reports");
        userProvider.findAllUsers().forEach(user -> {
            sendReport(user);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Generation of training reports finished");
    }

    private void sendReport(User user) {
        log.info("Sending email to {}", user.getEmail());
        javaMailSender.send(createEmail(user));
    }

    private SimpleMailMessage createEmail(User user) {
        List<Training> trainings = trainingProvider.getTrainingsByUserId(user.getId());
        List<Statistics> statistics = statisticsProvider.getStatisticsByUserId(user.getId());

        log.info("Creating email for {}", user.getEmail());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(TITLE);
        email.setFrom("system@trainings.com");
        email.setTo(user.getEmail());
        email.setText(createEmailText(trainings, statistics));
        //email.setText(createEmailText2(trainings));
        //email.setText(createEmailText3(statistics));

        // total number of training sessions
        log.info("Email created");
        log.info("Email created");
        log.info("\n{}", email.getText() );
        log.info("Email created");
        log.info("Email created");

        return email;
    }

    private String createEmailText(List<Training> trainings, List<Statistics> statistics ) {
        int sum = statistics.stream()
                .mapToInt(Statistics::getTotalTrainings)
                .sum();

        return String.format("""
                        ________________________________________________________________
                        Report generated from the "Training" table.
                        Number of training sessions %s.
                        ________________________________________________________________
                        Report generated from the "Statistics" table.
                        Total number of trainings %s.
                        ________________________________________________________________
                        """,
                trainings.size(),
                sum
        );
    }

    private String createEmailText2(List<Training> trainings) {
        return "Number of training sessions " + trainings.size() + ".";
    }

    private String createEmailText3(List<Statistics> statistics) {
        int sum = statistics.stream()
                .mapToInt(Statistics::getTotalTrainings)
                .sum();
        return "Total number of trainings " + sum + ".";
    }

}
