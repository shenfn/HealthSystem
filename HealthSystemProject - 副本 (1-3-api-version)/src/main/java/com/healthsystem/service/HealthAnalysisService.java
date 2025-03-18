package com.healthsystem.service;

import com.healthsystem.model.HealthRecord;
import com.healthsystem.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HealthAnalysisService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Value("${deepseek.api.key}")
    private String deepSeekApiKey;

    @Value("${deepseek.api.base-url}")
    private String deepSeekBaseUrl;

    public Map<String, Object> analyzeHealthRecords(Integer userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("records", new ArrayList<HealthRecord>());
        response.put("analysis", "暂无分析结果");

        // 获取用户健康记录
        List<HealthRecord> records = healthRecordRepository.findByUserId(userId);
        if (records == null || records.isEmpty()) {
            response.put("analysis", "没有可用的健康记录");
            return response;
        }
        records.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        response.put("records", records);

        // 格式化健康数据
        String healthData = records.stream()
                .map(record -> {
                    StringBuilder data = new StringBuilder();
                    data.append("日期: ").append(record.getDate()).append("\n");
                    if (record.getWeight() != null) data.append("体重: ").append(record.getWeight()).append("kg\n");
                    if (record.getBloodSugar() != null) data.append("血糖: ").append(record.getBloodSugar()).append("mmol/L\n");
                    if (record.getSteps() != null) data.append("步数: ").append(record.getSteps()).append("步\n");
                    return data.toString();
                })
                .collect(Collectors.joining("\n\n"));

        String prompt = "请根据以下健康数据进行专业分析，提供详细的健康报告，包括潜在风险、趋势分析和改进建议：\n" + healthData;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + deepSeekApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            requestBody.put("stream", false);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(new HashMap<String, String>() {{
                put("role", "system");
                put("content", "你是一个专业的健康分析助手，提供准确的健康建议。");
            }});
            messages.add(new HashMap<String, String>() {{
                put("role", "user");
                put("content", prompt);
            }});
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> deepSeekResponse = restTemplate.exchange(
                    deepSeekBaseUrl + "/chat/completions",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map<String, Object> body = deepSeekResponse.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) choice.get("message");
                    String analysis = message.get("content");
                    response.put("analysis", analysis);
                }
            }

        } catch (Exception e) {
            System.err.println("调用 DeepSeek API 失败: " + e.getMessage());
            response.put("analysis", "健康分析失败，请稍后重试");
        }

        return response;
    }
}