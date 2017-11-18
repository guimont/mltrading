package com.mltrading.web.rest.dto;

public class ExtractDTO {

    private String target;
    private int period;

    public ExtractDTO(String target, int period) {
        this.target = target;
        this.period = period;
    }

    public ExtractDTO() {
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
