package com.healthsystem.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HealthRecord {
    private Integer id;
    private Integer userId;
    private LocalDate date;
    private Double weight;
    private Integer height;
    private Double bloodSugar;
    private Integer steps;
    private Double sleepHours;
    private String notes;
    private LocalDateTime createdAt;
    // 新增字段（未来扩展）
    private Integer systolicPressure;
    private Integer diastolicPressure;
    private Integer heartRate;
    private Double bodyFat;
    private String mood;

    // Getter 和 Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    public Double getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(Double bloodSugar) { this.bloodSugar = bloodSugar; }
    public Integer getSteps() { return steps; }
    public void setSteps(Integer steps) { this.steps = steps; }
    public Double getSleepHours() { return sleepHours; }
    public void setSleepHours(Double sleepHours) { this.sleepHours = sleepHours; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getSystolicPressure() { return systolicPressure; }
    public void setSystolicPressure(Integer systolicPressure) { this.systolicPressure = systolicPressure; }
    public Integer getDiastolicPressure() { return diastolicPressure; }
    public void setDiastolicPressure(Integer diastolicPressure) { this.diastolicPressure = diastolicPressure; }
    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }
    public Double getBodyFat() { return bodyFat; }
    public void setBodyFat(Double bodyFat) { this.bodyFat = bodyFat; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    @Override
    public String toString() {
        return "HealthRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", weight=" + weight +
                ", height=" + height +
                ", bloodSugar=" + bloodSugar +
                ", steps=" + steps +
                ", sleepHours=" + sleepHours +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", systolicPressure=" + systolicPressure +
                ", diastolicPressure=" + diastolicPressure +
                ", heartRate=" + heartRate +
                ", bodyFat=" + bodyFat +
                ", mood='" + mood + '\'' +
                '}';
    }
}