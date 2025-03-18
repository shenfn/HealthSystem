package com.healthsystem.model;

import lombok.Data;

@Data
public class HealthData {
    private Double weight;
    private Integer height;
    private Double bloodSugar;
    private Integer steps;
}