package com.healthsystem.controller;

import com.healthsystem.model.HealthRecord;
import com.healthsystem.repository.HealthRecordRepository;
import com.healthsystem.utils.HealthCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // 验证用户是否登录
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("userId", userId);
        return "pages/dashboard"; // 返回 dashboard.html 模板
    }

    @GetMapping("/dashboard/records")
    @ResponseBody
    public Map<String, Object> getDashboardData(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");

        // 初始化默认值
        response.put("records", Collections.emptyList());
        response.put("healthScore", "N/A");
        response.put("overallHealth", "N/A");
        response.put("currentWeight", "N/A");
        response.put("latestBMI", "N/A");

        if (userId == null) {
            return response;
        }

        // 获取健康记录
        List<HealthRecord> records = healthRecordRepository.findByUserId(userId);
        records.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate())); // 按日期降序

        // 返回记录
        response.put("records", records);

        // 计算健康指标
        if (!records.isEmpty()) {
            HealthRecord latestRecord = records.get(0);

            // 健康评分
            int healthScore = HealthCalculator.calculateHealthScore(latestRecord);
            response.put("healthScore", healthScore);

            // 整体健康状况
            String overallHealth = HealthCalculator.getOverallHealthStatus(latestRecord);
            response.put("overallHealth", overallHealth);

            // 最新体重
            response.put("currentWeight", latestRecord.getWeight() != null ? latestRecord.getWeight() : "N/A");

            // 最新 BMI
            String latestBMI = "N/A";
            if (latestRecord.getWeight() != null && latestRecord.getHeight() != null) {
                latestBMI = String.format("%.1f", HealthCalculator.calculateBMI(latestRecord.getWeight(), latestRecord.getHeight()));
            }
            response.put("latestBMI", latestBMI);
        }

        System.out.println("Returning dashboard data: " + response); // 调试
        return response;
    }
}