package com.dcs.cdr.cdrservice.service;

import com.dcs.cdr.cdrservice.model.ChargeDetailRecord;
import com.dcs.cdr.cdrservice.model.ChargeDetailRecordResponse;
import com.dcs.cdr.cdrservice.repository.ChargeDetailRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChargeDetailRecordService {

    private static final Logger logger = LoggerFactory.getLogger(ChargeDetailRecordService.class);

    @Autowired
    private ChargeDetailRecordRepository chargeDetailRecordRepository;

    @Transactional
    public ChargeDetailRecordResponse saveChargeDetailRecord(ChargeDetailRecord record) {
            logger.info("Entering saveChargeDetailRecord with record: {}", record);
            List<String> validationErrors = validateChargeDetailRecord(record);

            if (!validationErrors.isEmpty()) {
                logger.warn("Validation errors found: {}", validationErrors);
                return new ChargeDetailRecordResponse(false, validationErrors);
            }

            chargeDetailRecordRepository.save(record);
            logger.info("Exiting saveChargeDetailRecord with success");
            return new ChargeDetailRecordResponse(true, new ArrayList<>());
    }

    public ChargeDetailRecord getChargeDetailRecordById(Long id) {
            logger.info("Entering getChargeDetailRecordById with id: {}", id);
            ChargeDetailRecord record = chargeDetailRecordRepository.findById(id).orElse(null);
            logger.info("Exiting getChargeDetailRecordById with record: {}", record);
            return record;
    }

    public List<ChargeDetailRecord> getChargeDetailRecordsByVehicleId(String vehicleId) {
            logger.info("Entering getChargeDetailRecordsByVehicleId with vehicleId: {}", vehicleId);
            List<ChargeDetailRecord> records = chargeDetailRecordRepository.findByVehicleId(vehicleId);
            logger.info("Exiting getChargeDetailRecordsByVehicleId with records: {}", records);
            return records;
    }

    public List<ChargeDetailRecord> getChargeDetailRecordsSortedByStartTime() {
            logger.info("Entering getChargeDetailRecordsSortedByStartTime");
            List<ChargeDetailRecord> records = chargeDetailRecordRepository.findAllByOrderByStartTimeAsc();
            logger.info("Exiting getChargeDetailRecordsSortedByStartTime with records: {}", records);
            return records;
    }

    private List<String> validateChargeDetailRecord(ChargeDetailRecord record) {
        logger.info("Entering validateChargeDetailRecord with record: {}", record);
        List<String> errors = new ArrayList<>();

        if (record.getEndTime().isBefore(record.getStartTime())) {
            errors.add("End time cannot be earlier than start time.");
        }

        Optional<ChargeDetailRecord> lastRecord = chargeDetailRecordRepository.findTopByVehicleIdOrderByEndTimeDesc(record.getVehicleId());
        if (lastRecord.isPresent() && record.getStartTime().isBefore(lastRecord.get().getEndTime())) {
            errors.add("Start time must be after the last recorded end time for this vehicle.");
        }

        if (record.getTotalCost() <= 0) {
            errors.add("Total cost must be greater than 0.");
        }

        logger.info("Exiting validateChargeDetailRecord with errors: {}", errors);
        return errors;
    }
}

