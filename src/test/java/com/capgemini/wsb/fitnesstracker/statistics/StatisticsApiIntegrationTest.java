package com.capgemini.wsb.fitnesstracker.statistics;

import com.capgemini.wsb.fitnesstracker.IntegrationTest;
import com.capgemini.wsb.fitnesstracker.IntegrationTestBase;
import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest

@IntegrationTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class StatisticsApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllStatistics_whenGettingAllStatistics() throws Exception {

        User user1 = existingUser(generateClient());
        Statistics statistics1 = persistStatistics(generateStatistics(user1));

        mockMvc.perform(get("/v1/statistics").contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].user.id").value(user1.getId()))
                .andExpect(jsonPath("$[0].user.firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$[0].user.lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$[0].user.email").value(user1.getEmail()))
                .andExpect(jsonPath("$[0].totalTrainings").value((statistics1.getTotalTrainings())))
                .andExpect(jsonPath("$[0].totalDistance").value((statistics1.getTotalDistance())))
                .andExpect(jsonPath("$[0].totalCaloriesBurned").value(statistics1.getTotalCaloriesBurned()))

                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    @Transactional
    void shouldReturnAllStatisticsForDedicatedUser_whenGettingAllStatisticsForDedicatedUser() throws Exception {

        User user1 = existingUser(generateClient());
        Statistics statistics1 = persistStatistics(generateStatistics(user1));

        mockMvc.perform(get("/v1/statistics/{userId}", user1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].user.id").value(user1.getId()))
                .andExpect(jsonPath("$[0].user.firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$[0].user.lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$[0].user.email").value(user1.getEmail()))
                .andExpect(jsonPath("$[0].totalTrainings").value((statistics1.getTotalTrainings())))
                .andExpect(jsonPath("$[0].totalDistance").value((statistics1.getTotalDistance())))
                .andExpect(jsonPath("$[0].totalCaloriesBurned").value(statistics1.getTotalCaloriesBurned()))

                .andExpect(jsonPath("$[1]").doesNotExist());
    }


    @Test
    void shouldPersistStatistics_whenCreatingNewStatistics() throws Exception {

        User user1 = existingUser(generateClient());

        String requestBody = """
                {
                    "id": "%s",
                    "totalTrainings": 1,
                    "totalDistance": 2.3,
                    "totalCaloriesBurned": 4
                }
                """.formatted(user1.getId());
        mockMvc.perform(post("/v1/statistics").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id").value(user1.getId()))
                .andExpect(jsonPath("$.user.firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$.user.lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.totalTrainings").value(1))
                .andExpect(jsonPath("$.totalDistance").value(2.3))
                .andExpect(jsonPath("$.totalCaloriesBurned").value(4));
    }

    @Test
    void shouldUpdateStatistics_whenUpdatingStatistics() throws Exception {

        User user1 = existingUser(generateClient());
        Statistics statistics1 = persistStatistics(generateStatisticsWithActivityType(user1));
        String requestBody = """
                {
                "id": "%s",
                "totalTrainings": 0,
                "totalDistance": 0.0,
                "totalCaloriesBurned": 0
                }
                """.formatted(user1.getId());
        mockMvc.perform(put("/v1/statistics/{statisticsId}", statistics1.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(user1.getId()))
                .andExpect(jsonPath("$.user.firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$.user.lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.totalTrainings").value(0))
                .andExpect(jsonPath("$.totalTrainings").value(0.0))
                .andExpect(jsonPath("$.totalCaloriesBurned").value(0));
    }

    private static User generateClient() {
        return new User(randomUUID().toString(), randomUUID().toString(), now(), randomUUID().toString());
    }

    private static Statistics generateStatistics(User user) throws ParseException {
        return new Statistics(
                user,
                1,
                2.3,
                4
                );
    }

    private static Statistics generateStatisticsWithActivityType(User user) throws ParseException {
        return new Statistics(
                user,
                0, 0.0, 0);
    }

}
