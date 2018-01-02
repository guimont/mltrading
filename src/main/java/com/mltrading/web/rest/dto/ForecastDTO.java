package com.mltrading.web.rest.dto;

/**
 * Created by gmo on 27/02/2017.
 */
public class ForecastDTO {

    private String validator;
    private String modelType;
    private String target;
    private int globalLoop;
    private int inputLoop;


    public ForecastDTO(String validator, String modelType, String target, int globalLoop, int inputLoop) {
        this.validator = validator;
        this.target = target;
        this.globalLoop = globalLoop;
        this.inputLoop = inputLoop;
        this.modelType = modelType;
    }

    public ForecastDTO() {
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getGlobalLoop() {
        return globalLoop;
    }

    public void setGlobalLoop(int globalLoop) {
        this.globalLoop = globalLoop;
    }

    public int getInputLoop() {
        return inputLoop;
    }

    public void setInputLoop(int inputLoop) {
        this.inputLoop = inputLoop;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
}
