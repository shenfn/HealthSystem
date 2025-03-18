package com.healthsystem.service;

import com.healthsystem.model.HealthRecord;
import com.healthsystem.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthManagementService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    public List<HealthRecord> getRecordsByUserId(Integer userId) {
        return healthRecordRepository.findByUserId(userId);
    }

    public HealthRecord addRecord(HealthRecord record, Integer userId) {
        if (record.getDate() == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        record.setUserId(userId);
        record.setCreatedAt(LocalDateTime.now());
        healthRecordRepository.insert(record);
        return record;
    }

    public HealthRecord modifyRecord(Integer id, String field, Object value) {
        HealthRecord record = healthRecordRepository.findById(id);
        if (record == null) {
            throw new IllegalArgumentException("记录不存在");
        }

        switch (field) {
            case "体重":
                record.setWeight(Double.parseDouble(value.toString()));
                break;
            case "身高":
                record.setHeight(Integer.parseInt(value.toString()));
                break;
            case "血糖":
                record.setBloodSugar(Double.parseDouble(value.toString()));
                break;
            case "步数":
                record.setSteps(Integer.parseInt(value.toString()));
                break;
            case "备注":
                record.setNotes(value.toString());
                break;
            default:
                throw new IllegalArgumentException("无效字段");
        }

        healthRecordRepository.update(record);
        return record;
    }

    public void deleteRecord(Integer id) {
        HealthRecord record = healthRecordRepository.findById(id);
        if (record == null) {
            throw new IllegalArgumentException("记录不存在");
        }
        healthRecordRepository.delete(id);
    }

    public List<HealthRecord> searchRecords(Integer userId, String field, String startDate, String endDate, double minValue, double maxValue) {
        List<HealthRecord> records = healthRecordRepository.findByUserId(userId);
        return records.stream()
                .filter(record -> {
                    LocalDate recordDate = record.getDate();
                    boolean dateMatch = true;
                    if (startDate != null && !startDate.isEmpty()) {
                        LocalDate start = LocalDate.parse(startDate);
                        dateMatch = dateMatch && !recordDate.isBefore(start);
                    }
                    if (endDate != null && !endDate.isEmpty()) {
                        LocalDate end = LocalDate.parse(endDate);
                        dateMatch = dateMatch && !recordDate.isAfter(end);
                    }

                    double value;
                    switch (field) {
                        case "体重":
                            value = record.getWeight() != null ? record.getWeight() : Double.MIN_VALUE;
                            break;
                        case "身高":
                            value = record.getHeight() != null ? record.getHeight() : Double.MIN_VALUE;
                            break;
                        case "血糖":
                            value = record.getBloodSugar() != null ? record.getBloodSugar() : Double.MIN_VALUE;
                            break;
                        case "步数":
                            value = record.getSteps() != null ? record.getSteps() : Double.MIN_VALUE;
                            break;
                        default:
                            return false;
                    }
                    return dateMatch && value >= minValue && value <= maxValue;
                })
                .collect(Collectors.toList());
    }
}