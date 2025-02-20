package com.prueba.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.demo.model.*;
import com.prueba.demo.repository.ExcerciseRepository;
import com.prueba.demo.repository.FoodRepository;
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

import static com.prueba.demo.model.ExcerciseType.piernacompleta;

@Service
public class APIConsumption {

    @Autowired
    private FoodRepository foodRepository;

    //Nutritionix, hacerlo mas seguro!

     //estos son de la nutricuenta

    private static final String API_KEY_CALORIES = "04efb6f3d6527074db8c13c4ac662f40";
    private static final String API_ID_CALORIES = "7aa68925";
    private static final String URL_BASE_CALORIES = "https://trackapi.nutritionix.com/v2/natural/exercise/";

    //esta dde mi cuenta



     /*

    private static final String API_KEY = "602d0e1d856a6e905686723221befb1e";
    private static final String API_ID = "7fe9ac29";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";



*/
 //otro personal XD
    private static final String API_KEY = "5489f0b2ecbc6bfd795e90ce6d2c09d0";
    private static final String API_ID = "d1ca182a";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";


    /*

    private static final String API_KEY = "1ffb74a6400c6112c94c03ff2ff19803";
    private static final String API_ID = "4096520a";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";
*/
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
    private static final String EXERCISE_API_URL = "https://exercisedb.p.rapidapi.com/exercises/bodyPart/";

    public Excercise getExerciseInfo(String exerciseName) {
        HttpClient client = HttpClient.newHttpClient();
        String encodedExerciseName = java.net.URLEncoder.encode(exerciseName, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EXERCISE_API_URL + encodedExerciseName + "?limit=3"))
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


    @Autowired
    ExcerciseRepository excerciseRepository;

    public List<String> getExerciseSuggestionsByMuscleGroup(String muscleGroup, ExcerciseType insertedExcerciseType, Goal goal) {
        HttpClient client = HttpClient.newHttpClient();

        String url = EXERCISE_API_URL + muscleGroup + "?limit=5";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", EXERCISE_API_KEY)
                .header("X-RapidAPI-Host", EXERCISE_API_HOST)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return Collections.emptyList();
            }

            ObjectMapper objectMapper = new ObjectMapper();

            // Deserializar directamente a una lista de Excercise objetos, solo extrayendo gifUrl y name
            List<Map<String, Object>> apiResponse = objectMapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});

            // Filtrar ejercicios válidos, asignar ExcerciseType e insertarlos en la base de datos
            List<String> exerciseNames = apiResponse.stream()
                    .map(exerciseData -> {
                        String gifUrl = (String) exerciseData.get("gifUrl");
                        String name = (String) exerciseData.get("name");

                        Optional<Excercise> existingExercise = Optional.ofNullable(excerciseRepository.findByExcerciseName(name));
                        if (existingExercise.isPresent()) {
                            // Si ya existe, devolver su nombre sin guardarlo nuevamente
                            return existingExercise.get().getExcerciseName();
                        }



                        Excercise exercise = new Excercise();
                        exercise.setGifURL(gifUrl);
                        exercise.setExcerciseName(name);
                        exercise.setExcerciseType(insertedExcerciseType);
                        exercise.setExcerciseType(insertedExcerciseType);

                        int series = 5;
                        if(insertedExcerciseType == piernacompleta){
                            series =4;
                            exercise.setSeries(series);
                        } else {
                            exercise.setSeries(series);
                        }

                        Random random = new Random();
                        int selectedReps = 0;
                        int time = 5;  // Valor por defecto

                        switch (goal) {
                            case deficit:
                                // Elegir aleatoriamente entre 12, 14 y 15
                                selectedReps = new int[]{12, 14, 15}[random.nextInt(3)];
                                time = (selectedReps == 14 || selectedReps == 15) ? 4 : 5;  // Si es 14 o 15, asignar 4, sino 5
                                break;
                            case volumen:
                                // Elegir aleatoriamente entre 6, 8 y 10
                                selectedReps = new int[]{6, 8, 10}[random.nextInt(3)];
                                time = (selectedReps == 10) ? 4 : 5;  // Si es 10, asignar 4, sino 5
                                break;
                            case mantenimiento:
                                // Elegir aleatoriamente entre 10 y 12
                                selectedReps = new int[]{10, 12}[random.nextInt(2)];
                                time = (selectedReps == 12) ? 4 : 5;  // Si es 12, asignar 4, sino 5
                                break;
                            default:
                                selectedReps = new int[]{10, 12}[random.nextInt(2)];
                                time = (selectedReps == 12) ? 4 : 5;  // Si es 12, asignar 4, sino 5
                        }

                        exercise.setReps(selectedReps);

                        int totalTime = (int) Math.round((selectedReps * time * series) / 60.0);
                        exercise.setTime(totalTime);

                        int caloriesBurned = getCaloriesBurned(name, totalTime);

                        exercise.setCalories(caloriesBurned);

                        // Guardar el ejercicio en la base de datos
                        Excercise savedExercise = excerciseRepository.save(exercise);

                        // Devolver el nombre del ejercicio
                        return savedExercise.getExcerciseName();
                    })
                    .collect(Collectors.toList());

            return exerciseNames;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }



    public int getCaloriesBurned(String exerciseName, int duration) {
        HttpClient client = HttpClient.newHttpClient();

        String query = exerciseName + " " + duration + " minutes";
        String requestBody = "{\"query\":\"" + query + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE_CALORIES))
                .header("x-app-id", API_ID_CALORIES)
                .header("x-app-key", API_KEY_CALORIES)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.has("exercises") && rootNode.get("exercises").isArray() && rootNode.get("exercises").size() > 0) {
                JsonNode exerciseNode = rootNode.get("exercises").get(0);
                return exerciseNode.get("nf_calories").asInt();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return 0; // En caso de error o si no se encontraron ejercicios
    }




    //cargar informacion, borrar a futuro? sirvio para hacer el mockupdata
/*
    public List<String> getFoodSuggestionsFromFile(String filePath, int mealType) {
        List<String> foodSuggestions = new ArrayList<>();
        List<String> queries = readQueriesFromFile(filePath);

        HttpClient client = HttpClient.newHttpClient();

        for (String query : queries) {
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
                                    food.setMealType(Collections.singletonList(mealType));

                                    // 1. Check if the food with the same name already exists in the database
                                    Optional<Food> existingFood = foodRepository.findByFoodName(food.getFoodName());
                                    if (existingFood.isEmpty()) {
                                        // 2. Print each Food object as JSON (for debugging):
                                        String foodJson = objectMapper.writeValueAsString(food);

                                        foodRepository.save(food); // Save valid foods to the database
                                        saveFoodToCSV(food);

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

        }

        return Collections.emptyList();
    }

    private List<String> readQueriesFromFile(String filePath) {
        List<String> queries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] queryArray = line.split(",");
                queries.addAll(Arrays.asList(queryArray));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries;
    }


    //CAMBIAR EL CSVFILEPATH!!!
    private void saveFoodToCSV(Food food) {
        String csvFilePath = "foods.csv"; // Path to your CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            // Prepare the CSV line for the current food
            String csvLine = String.format("%s,%f,%f,%f,%f,%f,%d",
                    food.getFoodName(),
                    food.getCalories(),
                    food.getProtein(),
                    food.getTotalCarbohydrate(),
                    food.getTotalFat(),
                    food.getPortionWeight(),
                    food.getMealType());

            // Write the line to the CSV file
            writer.write(csvLine);
            writer.newLine(); // Move to the next line for the next entry
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

}
