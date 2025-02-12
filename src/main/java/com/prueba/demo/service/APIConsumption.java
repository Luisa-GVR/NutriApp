package com.prueba.demo.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.demo.model.Excercise;
import com.prueba.demo.model.ExcerciseResponse;
import com.prueba.demo.model.Food;
import com.prueba.demo.model.FoodResponse;
import com.prueba.demo.repository.FoodRepository;
import com.prueba.demo.service.dto.FoodPreferencesDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class APIConsumption {

    @Autowired
    private FoodRepository foodRepository;

    //Nutritionix, hacerlo mas seguro!

     //estos son de la nutricuenta
    /*
     private static final String API_KEY = "04efb6f3d6527074db8c13c4ac662f40";
    private static final String API_ID = "7aa68925";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";

    //esta dde mi cuenta

     */
     /*

    private static final String API_KEY = "602d0e1d856a6e905686723221befb1e";
    private static final String API_ID = "7fe9ac29";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";


*/
/* otro personal XD
    private static final String API_KEY = "5489f0b2ecbc6bfd795e90ce6d2c09d0";
    private static final String API_ID = "d1ca182a";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";

 */

    private static final String API_KEY = "1ffb74a6400c6112c94c03ff2ff19803";
    private static final String API_ID = "4096520a";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";

    public Food getFoodInfo(String query) {
        HttpClient client = HttpClient.newHttpClient();

        String encodedFoodName = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String requestBody = "{\"query\":\"" + query + "\"}";

        //System.out.println("req body: " + requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE))
                .header("x-app-id", API_ID)
                .header("x-app-key", API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Convertimos la respuesta a un String
        String jsonResponse = response.body();
        System.out.println(jsonResponse);  // Para depurar y ver la respuesta completa

        try {
            // Usamos ObjectMapper de Jackson para convertir el JSON en un objeto de tipo FoodsResponse
            ObjectMapper objectMapper = new ObjectMapper();
            FoodResponse foodResponse = objectMapper.readValue(jsonResponse, FoodResponse.class);

            // Asegúrate de que la lista de alimentos no esté vacía
            if (foodResponse.getFoods() != null && !foodResponse.getFoods().isEmpty()) {
                // Accedemos al primer alimento de la lista
                Food food = foodResponse.getFoods().get(0);

                // Devolvemos el objeto Food
                return food;
            } else {
                System.out.println("No se encontraron alimentos.");
                return null;  // Si no hay alimentos en la lista
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Si hubo un error al procesar el JSON
        }

    }

    public List<String> getFoodSuggestionsNeutral(String query) {
        HttpClient client = HttpClient.newHttpClient();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String requestBody = "{\"query\":\"" + query + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE))
                .header("x-app-id", API_ID)
                .header("x-app-key", API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            FoodResponse foodResponse = objectMapper.readValue(response.body(), FoodResponse.class);

            if (foodResponse.getFoods() != null && !foodResponse.getFoods().isEmpty()) {
                // Filter out invalid food names and save the valid ones to the database
                List<Food> foods = foodResponse.getFoods().stream()
                        .filter(food -> food.getFoodName() != null && !food.getFoodName().isEmpty()) // Avoid saving invalid data
                        .peek(food -> {
                            try {
                                // 1. Check if the food with the same name already exists in the database
                                Optional<Food> existingFood = foodRepository.findByFoodName(food.getFoodName());
                                if (existingFood.isEmpty()) {
                                    // 2. Print each Food object as JSON (for debugging):
                                    String foodJson = objectMapper.writeValueAsString(food);

                                    foodRepository.save(food); // Save valid foods to the database
                                }
                            } catch (JsonProcessingException e) {
                                System.err.println("Error serializing Food object to JSON: " + e.getMessage());
                            }
                        }) // Process valid foods
                        .collect(Collectors.toList());

                // Return the names of the first 3 foods from the list
                return foods.stream()
                        .limit(3)
                        .map(Food::getFoodName)
                        .collect(Collectors.toList());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private static final String EXERCISE_API_KEY = "7365eca4b1msh9743512e61a1b43p1581b0jsneb2caf6b1530";
    private static final String EXERCISE_API_HOST = "exercisedb.p.rapidapi.com";
    private static final String EXERCISE_API_URL = "https://exercisedb.p.rapidapi.com/status";
    public Excercise getExerciseInfo(String exerciseName) {
        HttpClient client = HttpClient.newHttpClient();
        String encodedExerciseName = java.net.URLEncoder.encode(exerciseName, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EXERCISE_API_URL + encodedExerciseName))
                .header("X-RapidAPI-Key", EXERCISE_API_KEY)
                .header("X-RapidAPI-Host", EXERCISE_API_HOST)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            ExcerciseResponse exercisesResponse = objectMapper.readValue(response.body(), ExcerciseResponse.class);

            if (exercisesResponse.getExcercises() != null && !exercisesResponse.getExcercises().isEmpty()) {
                return exercisesResponse.getExcercises().get(0);
            } else {
                System.out.println("No se encontraron ejercicios.");
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }





}
