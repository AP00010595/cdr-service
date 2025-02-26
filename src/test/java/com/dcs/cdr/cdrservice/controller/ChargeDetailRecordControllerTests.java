package com.dcs.cdr.cdrservice.controller;

import com.dcs.cdr.cdrservice.model.ChargeDetailRecord;
import com.dcs.cdr.cdrservice.model.ChargeDetailRecordResponse;
import com.dcs.cdr.cdrservice.service.ChargeDetailRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ChargeDetailRecordController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
                org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class
        })
public class ChargeDetailRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChargeDetailRecordService chargeDetailRecordService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testCreateChargeDetailRecord() throws Exception {
        ChargeDetailRecord record = new ChargeDetailRecord(1L, "1", "Car123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 10.0);
        ChargeDetailRecordResponse response = new ChargeDetailRecordResponse(true, null);
        when(chargeDetailRecordService.saveChargeDetailRecord(any(ChargeDetailRecord.class))).thenReturn(response);

        mockMvc.perform(post("/api/charge-detail-record")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk());  // Change from isCreated() to isOk()
    }

    @Test
    public void testGetChargeDetailRecordById() throws Exception {
        ChargeDetailRecord record = new ChargeDetailRecord(1L, "1", "Car123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 10.0);
        when(chargeDetailRecordService.getChargeDetailRecordById(1L)).thenReturn(record);

        mockMvc.perform(get("/api/charge-detail-record/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleId").value("Car123"));
    }
}

