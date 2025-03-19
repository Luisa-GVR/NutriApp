package com.prueba.demo.model;

public enum ExcerciseType {

    BACK("back"),
    CARDIO("cardio"),
    CHEST("chest"),
    LOWER_ARMS("lower arms"),
    LOWER_LEGS("lower legs"),
    NECK("neck"),
    SHOULDERS("shoulders"),
    UPPER_ARMS("upper arms"),
    UPPER_LEGS("upper legs"),
    WAIST("waist");

    private String value;

    ExcerciseType(String value) {
       this.value = value;
    }

    public static ExcerciseType fromString(String text) {
        for (ExcerciseType excerciseType : ExcerciseType.values()) {
            if (excerciseType.value.equalsIgnoreCase(text)) {
                return excerciseType;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return value;
    }

}
