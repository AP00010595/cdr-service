package com.dcs.cdr.cdrservice.Service;

import com.dcs.cdr.cdrservice.model.ChargeDetailRecord;
import com.dcs.cdr.cdrservice.repository.ChargeDetailRecordRepository;
import com.dcs.cdr.cdrservice.model.ChargeDetailRecordResponse;
import com.dcs.cdr.cdrservice.service.ChargeDetailRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChargeDetailRecordServiceTests {

    @Mock
    private ChargeDetailRecordRepository chargeDetailRecordRepository;

    @InjectMocks
    private ChargeDetailRecordService chargeDetailRecordService;

//    @Autowired
//    private Tracer tracer;

    private ChargeDetailRecord validRecord;
    private ChargeDetailRecord overlappingRecord;

    @BeforeEach
    void setUp() {
        validRecord = new ChargeDetailRecord(1L, "sess-001", "Car123",
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20.0);

        overlappingRecord = new ChargeDetailRecord(2L, "sess-002", "Car123",
                LocalDateTime.now().minusHours(1), LocalDateTime.now(), 15.0);
    }

    @Test
    void testSaveValidChargeDetailRecord() {
        when(chargeDetailRecordRepository.findTopByVehicleIdOrderByEndTimeDesc(validRecord.getVehicleId())).thenReturn(Optional.empty());
        when(chargeDetailRecordRepository.save(validRecord)).thenReturn(validRecord);

        ChargeDetailRecordResponse response = chargeDetailRecordService.saveChargeDetailRecord(validRecord);

        assertTrue(response.isSuccess(), "Record should be saved successfully");
        assertTrue(response.getValidationErrors().isEmpty(), "There should be no validation errors.");
        verify(chargeDetailRecordRepository, times(1)).save(validRecord);
    }

    @Test
    void testSaveChargeDetailRecordWithEndTimeBeforeStartTime() {
        ChargeDetailRecord invalidRecord = new ChargeDetailRecord(3L, "sess-003", "Car123",
                LocalDateTime.now().plusHours(2), LocalDateTime.now(), 20.0);

        ChargeDetailRecordResponse response = chargeDetailRecordService.saveChargeDetailRecord(invalidRecord);

        assertFalse(response.isSuccess(), "Record should not be saved as End Time is before Start Time");
        assertTrue(response.getValidationErrors().contains("End time cannot be earlier than start time."));
        verify(chargeDetailRecordRepository, never()).save(any(ChargeDetailRecord.class));
    }

    @Test
    void testSaveChargeDetailRecordWithOverlappingTimes() {
        when(chargeDetailRecordRepository.findTopByVehicleIdOrderByEndTimeDesc(overlappingRecord.getVehicleId()))
                .thenReturn(Optional.of(validRecord));

        ChargeDetailRecordResponse response = chargeDetailRecordService.saveChargeDetailRecord(overlappingRecord);

        assertFalse(response.isSuccess(), "Record should not be saved due to overlapping times");
        assertTrue(response.getValidationErrors().contains("Start time must be after the last recorded end time for this vehicle."));
        verify(chargeDetailRecordRepository, never()).save(any(ChargeDetailRecord.class));
    }

    @Test
    void testSaveChargeDetailRecordWithZeroTotalCost() {
        ChargeDetailRecord invalidRecord = new ChargeDetailRecord(null, "sess-004", "Car123",
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), 0.0);

        ChargeDetailRecordResponse response = chargeDetailRecordService.saveChargeDetailRecord(invalidRecord);

        assertFalse(response.isSuccess(), "Record should not be saved as total cost is zero");
        assertTrue(response.getValidationErrors().contains("Total cost must be greater than 0."));
        verify(chargeDetailRecordRepository, never()).save(any(ChargeDetailRecord.class));
    }

    @Test
    void testGetChargeDetailRecordById() {
        when(chargeDetailRecordRepository.findById(1L)).thenReturn(Optional.of(validRecord));

        ChargeDetailRecord retrievedRecord = chargeDetailRecordService.getChargeDetailRecordById(1L);

        assertNotNull(retrievedRecord);
        assertEquals("Car123", retrievedRecord.getVehicleId());
    }

    @Test
    void testGetChargeDetailRecordsByVehicleId() {
        List<ChargeDetailRecord> records = Arrays.asList(validRecord, overlappingRecord);
        when(chargeDetailRecordRepository.findByVehicleId("Car123")).thenReturn(records);

        List<ChargeDetailRecord> result = chargeDetailRecordService.getChargeDetailRecordsByVehicleId("Car123");

        assertEquals(2, result.size());
        assertEquals("Car123", result.get(0).getVehicleId());
    }

    @Test
    void testGetChargeDetailRecordsSortedByStartTime() {
        List<ChargeDetailRecord> sortedRecords = Arrays.asList(overlappingRecord, validRecord);
        when(chargeDetailRecordRepository.findAllByOrderByStartTimeAsc()).thenReturn(sortedRecords);

        List<ChargeDetailRecord> result = chargeDetailRecordService.getChargeDetailRecordsSortedByStartTime();

        assertEquals(2, result.size());
        assertTrue(result.get(0).getStartTime().isBefore(result.get(1).getStartTime()));
    }
}
