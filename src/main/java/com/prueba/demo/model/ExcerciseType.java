package com.prueba.demo.model;

public enum ExcerciseType {

    pechoybrazo("pechoybrazo"),
    piernacompleta("piernacompleta"),
    hombroyespalda("hombroyespalda");

    private String value;

    ExcerciseType(String value) {
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
