package com.prueba.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prueba.demo.principal.Photo;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("food_name")
    private String foodName;

    @JsonProperty("nf_calories")
    private double calories;

    @JsonProperty("nf_protein")
    private double protein;

    @JsonProperty("nf_total_carbohydrate")
    private double totalCarbohydrate;

    /*
    @JsonProperty("nf_total_fat")
    private double totalFat;

    @JsonProperty("serving_weight_grams")
    private double portionWeight;
*/

    @JsonProperty("photo") // Matches the JSON structure
    @Embedded
    private Photo photo;



    @JsonProperty("meal_type")
    private int mealType;

    //Getters y setters...

/*
    public double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(double totalFat) {
        this.totalFat = totalFat;
    }

    public double getPortionWeight() {
        return portionWeight;
    }

    public void setPortionWeight(double portionWeight) {
        this.portionWeight = portionWeight;
    }


 */
    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public double getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(double totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public int getMealType() {
        return mealType;
    }

    public void setMealType(int mealType) {
        this.mealType = mealType;
    }
}
