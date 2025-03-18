package com.healthsystem.service;

import com.healthsystem.model.HealthRecord;
import com.healthsystem.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HealthInfoService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    public List<HealthRecord> getRecordsByUserId(Integer userId) {
        List<HealthRecord> records = healthRecordRepository.findByUserId(userId);
        if (records == null) {
            return new ArrayList<>(); // 防止返回 null
        }
        return records;
    }

    public ChartData prepareChartData(Integer userId) {
        List<HealthRecord> records = getRecordsByUserId(userId);
        ChartData chartData = new ChartData();

        List<String> dates = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        List<Double> bloodSugars = new ArrayList<>();
        List<Integer> steps = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (HealthRecord record : records) {
            dates.add(record.getDate().format(formatter));
            weights.add(record.getWeight() != null ? record.getWeight() : 0.0);
            bloodSugars.add(record.getBloodSugar() != null ? record.getBloodSugar() : 0.0);
            steps.add(record.getSteps() != null ? record.getSteps() : 0);
        }

        chartData.setDates(dates);
        chartData.setWeights(weights);
        chartData.setBloodSugars(bloodSugars);
        chartData.setSteps(steps);

        return chartData;
    }

    // 声明 ChartData 为 public 类
    public static class ChartData {
        private List<String> dates;
        private List<Double> weights;
        private List<Double> bloodSugars;
        private List<Integer> steps;

        // 默认构造函数
        public ChartData() {
            this.dates = new ArrayList<>();
            this.weights = new ArrayList<>();
            this.bloodSugars = new ArrayList<>();
            this.steps = new ArrayList<>();
        }

        // Getter 和 Setter
        public List<String> getDates() { return dates; }
        public void setDates(List<String> dates) { this.dates = dates; }
        public List<Double> getWeights() { return weights; }
        public void setWeights(List<Double> weights) { this.weights = weights; }
        public List<Double> getBloodSugars() { return bloodSugars; }
        public void setBloodSugars(List<Double> bloodSugars) { this.bloodSugars = bloodSugars; }
        public List<Integer> getSteps() { return steps; }
        public void setSteps(List<Integer> steps) { this.steps = steps; }
    }
}