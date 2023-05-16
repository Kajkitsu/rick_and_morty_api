package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RickAndMortyFetcher {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        // Tworzenie instancji HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Adres URL API
        String apiUrl = "https://rickandmortyapi.com/api/character/?name=pickle";

        // Tworzenie HttpRequest z adresem URL i parametrem
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            // Wysłanie żądania GET i przechwycenie odpowiedzi
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Pobranie statusu odpowiedzi
            int statusCode = response.statusCode();

            // Pobranie nagłówków odpowiedzi
            HttpHeaders headers = response.headers();

            // Pobranie ciała odpowiedzi
            String responseBody = response.body();

            // Mapowanie resposne body do obiektu klasy CharacterResponseDTO
            CharacterResponseDTO responseDTO = objectMapper.readValue(responseBody, CharacterResponseDTO.class);

            // Wyświetlanie przekonwertowanej odpowiedzi
            System.out.println("Response body: " + responseDTO.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}