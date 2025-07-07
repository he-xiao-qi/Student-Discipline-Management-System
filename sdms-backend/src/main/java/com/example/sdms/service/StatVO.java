package com.example.sdms.service;

public class StatVO {
    private String name; // 分类名（如学院、类型等）
    private String date; // 时间（如年月）
    private Integer count;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
} 