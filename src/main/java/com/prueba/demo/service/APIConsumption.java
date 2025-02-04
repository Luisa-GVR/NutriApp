package com.prueba.demo.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.demo.model.Excercise;
import com.prueba.demo.model.ExcerciseResponse;
import com.prueba.demo.model.Foods;
import com.prueba.demo.model.FoodsResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class APIConsumption {

    //Nutritionix, hacerlo mas seguro!
    private static final String API_KEY = "04efb6f3d6527074db8c13c4ac662f40";
    private static final String API_ID = "7aa68925";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";

    public Foods getFoodInfo(String query) {
        HttpClient client = HttpClient.newHttpClient();

        String encodedFoodName = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String requestBody = "{\"query\":\"" + query + "\"}";

        System.out.println("req body: " + requestBody);

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
            FoodsResponse foodsResponse = objectMapper.readValue(jsonResponse, FoodsResponse.class);

            // Asegúrate de que la lista de alimentos no esté vacía
            if (foodsResponse.getFoods() != null && !foodsResponse.getFoods().isEmpty()) {
                // Accedemos al primer alimento de la lista
                Foods foods = foodsResponse.getFoods().get(0);

                // Devolvemos el objeto Food
                return foods;
            } else {
                System.out.println("No se encontraron alimentos.");
                return null;  // Si no hay alimentos en la lista
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Si hubo un error al procesar el JSON
        }

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
