package com.healthsystem.controller;

import com.healthsystem.model.HealthRecord;
import com.healthsystem.service.HealthManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class HealthManagementController {

    @Autowired
    private HealthManagementService healthManagementService;

    @GetMapping("/health-management")
    public String healthManagement(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        List<HealthRecord> healthRecords = healthManagementService.getRecordsByUserId(userId);
        model.addAttribute("healthRecords", healthRecords != null ? healthRecords : Collections.emptyList());
        return "pages/health-management";
    }

    @GetMapping("/health-management/records")
    @ResponseBody
    public List<HealthRecord> getHealthRecords(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return Collections.emptyList();
        }
        List<HealthRecord> records = healthManagementService.getRecordsByUserId(userId);
        records.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate())); // 按日期降序
        System.out.println("Returning records (HealthManagement): " + records); // 调试
        return records;
    }

    @PostMapping("/health-management/add")
    @ResponseBody
    public ResponseEntity<HealthRecord> addRecord(@RequestBody HealthRecord record, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }
        record.setUserId(userId);
        System.out.println("Adding record: " + record); // 调试
        HealthRecord newRecord = healthManagementService.addRecord(record, userId);
        return ResponseEntity.ok(newRecord);
    }

    @PostMapping("/health-management/modify")
    @ResponseBody
    public ResponseEntity<HealthRecord> modifyRecord(@RequestBody ModifyRequest request) {
        HealthRecord updatedRecord = healthManagementService.modifyRecord(request.getId(), request.getField(), request.getValue());
        System.out.println("Modified record: " + updatedRecord); // 调试
        return ResponseEntity.ok(updatedRecord);
    }

    @DeleteMapping("/health-management/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteRecord(@RequestParam("id") Integer id) {
        healthManagementService.deleteRecord(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/health-management/search")
    @ResponseBody
    public ResponseEntity<List<HealthRecord>> searchRecords(@RequestBody SearchRequest request, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        List<HealthRecord> records = healthManagementService.searchRecords(
                userId,
                request.getField(),
                request.getStartDate(),
                request.getEndDate(),
                request.getMinValue(),
                request.getMaxValue()
        );
        System.out.println("Searched records: " + records); // 调试
        return ResponseEntity.ok(records);
    }
}

class ModifyRequest {
    private Integer id;
    private String field;
    private Object value;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
}

class SearchRequest {
    private String field;
    private String startDate;
    private String endDate;
    private double minValue;
    private double maxValue;

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public double getMinValue() { return minValue; }
    public void setMinValue(double minValue) { this.minValue = minValue; }
    public double getMaxValue() { return maxValue; }
    public void setMaxValue(double maxValue) { this.maxValue = maxValue; }
}