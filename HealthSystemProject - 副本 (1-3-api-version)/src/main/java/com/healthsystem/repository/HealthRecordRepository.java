package com.healthsystem.repository;

import com.healthsystem.model.HealthRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HealthRecordRepository {
    @Select("SELECT id, user_id AS userId, date, weight, height, blood_sugar AS bloodSugar, steps, sleep_hours AS sleepHours, notes, created_at AS createdAt FROM health_records WHERE user_id = #{userId} ORDER BY date DESC")
    List<HealthRecord> findByUserId(Integer userId);

    @Select("SELECT id, user_id AS userId, date, weight, height, blood_sugar AS bloodSugar, steps, sleep_hours AS sleepHours, notes, created_at AS createdAt FROM health_records WHERE id = #{id}")
    HealthRecord findById(Integer id);

    @Insert("INSERT INTO health_records (user_id, date, weight, height, blood_sugar, steps, sleep_hours, notes, created_at) " +
            "VALUES (#{userId}, #{date}, #{weight}, #{height}, #{bloodSugar}, #{steps}, #{sleepHours}, #{notes}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(HealthRecord record);

    @Update("UPDATE health_records SET date = #{date}, weight = #{weight}, height = #{height}, blood_sugar = #{bloodSugar}, " +
            "steps = #{steps}, sleep_hours = #{sleepHours}, notes = #{notes} WHERE id = #{id}")
    void update(HealthRecord record);

    @Delete("DELETE FROM health_records WHERE id = #{id}")
    void delete(Integer id);
}