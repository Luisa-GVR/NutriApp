package com.prueba.demo.service.dto;

import java.util.List;

public class FoodPreferencesDTO {
    private List<String> allergies;
    private List<String> dislikedFoods;
    private List<String> likedFoods;

    public FoodPreferencesDTO(List<String> allergies, List<String> dislikedFoods, List<String> likedFoods) {
        this.allergies = allergies;
        this.dislikedFoods = dislikedFoods;
        this.likedFoods = likedFoods;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getDislikedFoods() {
        return dislikedFoods;
    }

    public void setDislikedFoods(List<String> dislikedFoods) {
        this.dislikedFoods = dislikedFoods;
    }

    public List<String> getLikedFoods() {
        return likedFoods;
    }

    public void setLikedFoods(List<String> likedFoods) {
        this.likedFoods = likedFoods;
    }
}
