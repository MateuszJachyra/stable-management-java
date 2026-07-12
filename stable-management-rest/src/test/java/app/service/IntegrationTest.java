package app.service;


import app.repository.HorseRepository;
import app.repository.RatingRepository;
import app.repository.StableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest()
@Testcontainers
@AutoConfigureMockMvc
public class IntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");
    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private StableRepository stableRepository;

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        horseRepository.deleteAll();
        stableRepository.deleteAll();
    }

    @Test
    void givenValidPOST_whenAddStable_thenSuccess() throws Exception {
        String json = """
                {
                    "name": "Test stable",
                    "capacity": 10
                }
                """;

        mockMvc.perform(post("/api/stables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test stable"))
                .andExpect(jsonPath("$.capacity").value(10));
    }


}
