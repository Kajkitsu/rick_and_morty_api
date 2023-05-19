package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RickAndMortyFetcher {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        RickAndMortyFetcher fetcher = new RickAndMortyFetcher();
        fetcher.fetchCharacterListByName("Pickle")
                .stream()
                .map(CharacterDTO::episode)
                .flatMap(Collection::stream)
                .map(fetcher::fetchEpisodeByUrl)
                .flatMap(Optional::stream)
                .map(EpisodeDTO::characters)
                .flatMap(Collection::stream)
                .distinct()
                .map(fetcher::fetchCharacterByUrl)
                .flatMap(Optional::stream)
                .distinct()
                .map(it -> it.id() + ": " + it.name())
                .forEach(System.out::println);
    }

    public List<CharacterDTO> fetchCharacterListByName(String name) {
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

    public Optional<EpisodeDTO> fetchEpisodeByUrl(String url) {
        if (!url.startsWith("https://rickandmortyapi.com/api/episode/")) {
            throw new IllegalArgumentException("URL in wrong format");
        }
        String responseBody = fetch(url);
        try {
            EpisodeDTO responseDTO = OBJECT_MAPPER.readValue(responseBody, EpisodeDTO.class);
            return Optional.of(responseDTO);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<CharacterDTO> fetchCharacterByUrl(String url) {
        if (!url.startsWith("https://rickandmortyapi.com/api/character/")) {
            throw new IllegalArgumentException("URL in wrong format");
        }
        String responseBody = fetch(url);
        try {
            CharacterDTO responseDTO = OBJECT_MAPPER.readValue(responseBody, CharacterDTO.class);
            return Optional.of(responseDTO);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
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