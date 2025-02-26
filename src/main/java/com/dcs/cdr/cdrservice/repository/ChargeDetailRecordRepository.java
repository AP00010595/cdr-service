package com.dcs.cdr.cdrservice.repository;

import com.dcs.cdr.cdrservice.model.ChargeDetailRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChargeDetailRecordRepository extends JpaRepository<ChargeDetailRecord, Long> {
    List<ChargeDetailRecord> findByVehicleIdOrderByStartTime(String vehicleId);
    List<ChargeDetailRecord> findByVehicleId(String vehicleId);
    Optional<ChargeDetailRecord> findTopByVehicleIdOrderByEndTimeDesc(String vehicleId);
    List<ChargeDetailRecord> findAllByOrderByStartTimeAsc();
}
