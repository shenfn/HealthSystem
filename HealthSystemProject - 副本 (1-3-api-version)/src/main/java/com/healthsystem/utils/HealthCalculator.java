package com.healthsystem.utils;

import com.healthsystem.model.HealthRecord;

public class HealthCalculator {
    public static double calculateBMI(double weight, int height) {
        return weight / Math.pow(height / 100.0, 2);
    }

    // 判断BMI范围
    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "体重过轻";
        } else if (bmi < 24.0) {
            return "正常范围";
        } else if (bmi < 28.0) {
            return "超重";
        } else {
            return "肥胖";
        }
    }

    // 判断血糖范围（空腹）
    public static String getBloodSugarCategory(double bloodSugar) {
        if (bloodSugar < 3.9) {
            return "血糖过低";
        } else if (bloodSugar <= 6.1) {
            return "正常血糖";
        } else if (bloodSugar <= 7.0) {
            return "血糖偏高";
        } else {
            return "血糖过高";
        }
    }

    // 判断血压范围
    public static String getBloodPressureCategory(int systolic, int diastolic) {
        if (systolic < 120 && diastolic < 80) {
            return "正常血压";
        } else if ((systolic >= 120 && systolic <= 139) || (diastolic >= 80 && diastolic <= 89)) {
            return "血压偏高";
        } else {
            return "高血压";
        }
    }

    // 计算健康评分
    public static int calculateHealthScore(HealthRecord record) {
        int score = 100;

        // BMI 评分
        if (record.getWeight() != null && record.getHeight() != null) {
            double bmi = calculateBMI(record.getWeight(), record.getHeight());
            if (bmi < 18.5 || bmi >= 24.0) {
                score -= 10;
            }
        }

        // 血糖评分
        if (record.getBloodSugar() != null) {
            if (record.getBloodSugar() > 7.0 || record.getBloodSugar() < 3.9) {
                score -= 15;
            } else if (record.getBloodSugar() > 6.1) {
                score -= 5;
            }
        }

        // 步数评分
        if (record.getSteps() != null) {
            if (record.getSteps() < 5000) {
                score -= 10;
            } else if (record.getSteps() >= 10000) {
                score += 5;
            }
        }

        return Math.max(0, Math.min(100, score));
    }

    // 评估整体健康状况
    public static String getOverallHealthStatus(HealthRecord record) {
        StringBuilder status = new StringBuilder();

        // BMI 状况
        if (record.getWeight() != null && record.getHeight() != null) {
            double bmi = calculateBMI(record.getWeight(), record.getHeight());
            status.append(getBMICategory(bmi));
        }

        // 血糖状况
        if (record.getBloodSugar() != null) {
            if (status.length() > 0) status.append("，");
            status.append(getBloodSugarCategory(record.getBloodSugar()));
        }

        // 步数状况
        if (record.getSteps() != null) {
            if (status.length() > 0) status.append("，");
            if (record.getSteps() < 5000) {
                status.append("运动不足");
            } else if (record.getSteps() >= 10000) {
                status.append("运动良好");
            } else {
                status.append("运动适中");
            }
        }

        return status.length() > 0 ? status.toString() : "暂无健康评估";
    }

    // 为 DeepSeek API 准备健康数据
    public static String prepareHealthDataForAnalysis(HealthRecord record) {
        StringBuilder data = new StringBuilder();
        data.append("健康数据分析请求:\n");
        data.append("日期: ").append(record.getDate()).append("\n");

        if (record.getWeight() != null) {
            data.append("体重: ").append(record.getWeight()).append("kg\n");
        }

        if (record.getHeight() != null) {
            data.append("身高: ").append(record.getHeight()).append("cm\n");
        }

        if (record.getWeight() != null && record.getHeight() != null) {
            double bmi = calculateBMI(record.getWeight(), record.getHeight());
            data.append("BMI: ").append(String.format("%.2f", bmi)).append(" (").append(getBMICategory(bmi)).append(")\n");
        }

        if (record.getBloodSugar() != null) {
            data.append("血糖: ").append(record.getBloodSugar()).append("mmol/L (").append(getBloodSugarCategory(record.getBloodSugar())).append(")\n");
        }

        if (record.getSteps() != null) {
            data.append("步数: ").append(record.getSteps()).append("步\n");
        }

        if (record.getSleepHours() != null) {
            data.append("睡眠时长: ").append(record.getSleepHours()).append("小时\n");
        }

        if (record.getNotes() != null && !record.getNotes().isEmpty()) {
            data.append("备注: ").append(record.getNotes()).append("\n");
        }

        return data.toString();
    }
}