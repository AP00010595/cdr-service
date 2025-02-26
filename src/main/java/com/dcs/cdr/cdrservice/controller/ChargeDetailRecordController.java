package com.dcs.cdr.cdrservice.controller;

import com.dcs.cdr.cdrservice.model.ChargeDetailRecord;
import com.dcs.cdr.cdrservice.model.ChargeDetailRecordResponse;
import com.dcs.cdr.cdrservice.service.ChargeDetailRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/charge-detail-record")
public class ChargeDetailRecordController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeDetailRecordController.class);
    private final ChargeDetailRecordService chargeDetailRecordService;

    public ChargeDetailRecordController(ChargeDetailRecordService chargeDetailRecordService) {
        this.chargeDetailRecordService = chargeDetailRecordService;
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ChargeDetailRecord>> getChargeDetailRecordsByVehicleId(@PathVariable String vehicleId) {
        logger.info("Entering getChargeDetailRecordsByVehicleId with vehicleId: {}", vehicleId);
        List<ChargeDetailRecord> records = chargeDetailRecordService.getChargeDetailRecordsByVehicleId(vehicleId);
        logger.info("Exiting getChargeDetailRecordsByVehicleId with records: {}", records);
        return ResponseEntity.ok(records);
    }

    @PostMapping
    public ResponseEntity<?> createChargeDetailRecord(@RequestBody ChargeDetailRecord chargeDetailRecord) {
        logger.info("Entering createChargeDetailRecord with chargeDetailRecord: {}", chargeDetailRecord);
        ChargeDetailRecordResponse response = chargeDetailRecordService.saveChargeDetailRecord(chargeDetailRecord);
        logger.info("Exiting createChargeDetailRecord with response: {}", response);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargeDetailRecord> getChargeDetailRecordById(@PathVariable Long id) {
        logger.info("Entering getChargeDetailRecordById with id: {}", id);
        ChargeDetailRecord record = chargeDetailRecordService.getChargeDetailRecordById(id);
        logger.info("Exiting getChargeDetailRecordById with record: {}", record);
        return ResponseEntity.ok(record);
    }
}
