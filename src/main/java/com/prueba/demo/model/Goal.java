package com.prueba.demo.model;

public enum Goal {

    deficit("deficit"),
    mantenimiento("mantenimiento"),
    volumen("volumen");

    private String value;

    Goal(String value) {
        this.value = value;
    }
    public String getNombre() {
        return value;
    }

    public static Goal fromString(String text) {
        if (text != null) {
            for (Goal goal : Goal.values()) {
                if (goal.getNombre().equalsIgnoreCase(text.trim().toLowerCase())) {
                    return goal;  // ❌ No uses "new Goal()"
                }
            }
        }
        throw new IllegalArgumentException("No se encontró una meta con el texto: " + text);
    }



    @Override
    public String toString() {
        return value;
    }
}
