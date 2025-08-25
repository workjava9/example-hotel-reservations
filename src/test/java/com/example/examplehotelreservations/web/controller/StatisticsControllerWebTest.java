package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.service.StatisticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StatisticsControllerWebTest {

    @Autowired MockMvc mvc;

    @MockitoBean
    StatisticsService statisticsService;

    @Test
    @DisplayName("GET /api/v1/statistics/download -> returns CSV")
    void downloadCsv() throws Exception {
        // создаём реальный временный CSV-файл
        Path tmp = Files.createTempFile("stats-", ".csv");
        Files.writeString(tmp, "id,name\n1,John\n", StandardCharsets.UTF_8);
        tmp.toFile().deleteOnExit();

        // сервис вернёт путь к существующему файлу
        when(statisticsService.exportStatisticsToCsv()).thenReturn(tmp.toString());

        mvc.perform(get("/api/v1/statistics/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("filename=")))
                .andExpect(content().contentType("application/csv"))
                .andExpect(content().string(containsString("id,name")));

    }
}
