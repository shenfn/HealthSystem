package com.healthsystem.controller;

import com.healthsystem.model.HealthRecord;
import com.healthsystem.service.HealthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class HealthInfoController {

    @Autowired
    private HealthInfoService healthInfoService;

    @GetMapping("/health-info")
    public String healthInfo(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        List<HealthRecord> healthRecords = healthInfoService.getRecordsByUserId(userId);
        model.addAttribute("healthRecords", healthRecords != null ? healthRecords : Collections.emptyList());
        return "pages/health-info";
    }

    @GetMapping("/health-info/records")
    @ResponseBody
    public List<HealthRecord> getHealthRecords(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return Collections.emptyList();
        }
        List<HealthRecord> records = healthInfoService.getRecordsByUserId(userId);
        records.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate())); // 按日期降序
        System.out.println("Returning records (HealthInfo): " + records); // 调试
        return records;
    }
}