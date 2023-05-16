package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class RickAndMortyFetcher {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        RickAndMortyFetcher fetcher = new RickAndMortyFetcher();
        fetcher.fetchCharacterByName("Summer")
                .stream()
                .map(CharacterDTO::name)
                .distinct()
                .forEach(System.out::println);
    }

    public List<CharacterDTO> fetchCharacterByName(String name) {
        String apiUrl = "https://rickandmortyapi.com/api/character/?name=" + name;
        String responseBody = fetch(apiUrl);
        try {
            CharacterResponseDTO responseDTO = OBJECT_MAPPER.readValue(responseBody, CharacterResponseDTO.class);
            return responseDTO.results();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String fetch(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .GET()
                .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}