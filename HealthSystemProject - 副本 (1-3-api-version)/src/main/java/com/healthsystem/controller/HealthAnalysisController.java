package com.healthsystem.controller;

import com.healthsystem.service.HealthAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HealthAnalysisController {

    @Autowired
    private HealthAnalysisService healthAnalysisService;

    @GetMapping("/health-analysis")
    public String healthAnalysis(HttpSession session) {
        // 验证用户登录状态
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "pages/health-analysis";
    }

    @GetMapping("/health-analysis/data")
    @ResponseBody
    public Map<String, Object> getHealthAnalysisData(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.put("records", null);
            response.put("analysis", "用户未登录");
            return response;
        }

        // 调用服务层获取分析数据
        return healthAnalysisService.analyzeHealthRecords(userId);
    }
}