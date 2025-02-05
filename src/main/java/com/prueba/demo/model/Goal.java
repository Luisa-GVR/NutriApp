package com.prueba.demo.model;

public enum Goal {

    deficit("deficit"),
    mantenimiento("mantenimiento"),
    volumen("volumen");

    private String value;

    Goal(String value) {
    }

    public static Goal fromString(String text) {
        for (Goal goal : Goal.values()) {
            if (goal.value.equalsIgnoreCase(text)) {
                return goal;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return value;
    }
}
