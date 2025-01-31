package com.prueba.demo.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.demo.model.Food;
import com.prueba.demo.model.FoodsResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    //Nutritionix, hacerlo mas seguro!
    private static final String API_KEY = "04efb6f3d6527074db8c13c4ac662f40";
    private static final String URL_BASE = "https://trackapi.nutritionix.com/v2/natural/nutrients";

    public Food obtenerInformacionDeAlimentos(String query) {
        HttpClient client = HttpClient.newHttpClient();

        // Construimos el cuerpo de la solicitud
        String jsonBody = "{ \"query\": \"" + query + "\" }";  // El query es lo que buscarás (por ejemplo: "apple")

        // Creamos la solicitud con la API Key en los encabezados
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE))
                .header("x-app-id", "7aa68925")
                .header("x-app-key", API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = null;
        try {
            // Enviamos la solicitud y obtenemos la respuesta
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consumir la API", e);
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
                Food food = foodsResponse.getFoods().get(0);

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



    public String obtenerDatos(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String json = response.body();
        return json;
    }

}
