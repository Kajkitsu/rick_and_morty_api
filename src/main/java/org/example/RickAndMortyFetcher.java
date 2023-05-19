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
        var fetcher = new RickAndMortyFetcher();
        fetcher.fetchCharacterListByName("Pickle")
                .stream()
                .peek(it -> System.out.println(it.id() + ": " + it.name() + ", met:"))
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
                .map(it -> "\t" + it.id() + ": " + it.name())
                .forEach(System.out::println);
    }

    public List<CharacterDTO> fetchCharacterListByName(String name) {
        var apiUrl = "https://rickandmortyapi.com/api/character/?name=" + name;
        return fetch(CharacterListDTO.class, apiUrl)
                .map(CharacterListDTO::results)
                .orElse(Collections.emptyList());
    }

    public Optional<EpisodeDTO> fetchEpisodeByUrl(String url) {
        if (!url.startsWith("https://rickandmortyapi.com/api/episode/")) {
            throw new IllegalArgumentException("URL in wrong format");
        }
        return fetch(EpisodeDTO.class, url);
    }

    public Optional<CharacterDTO> fetchCharacterByUrl(String url) {
        if (!url.startsWith("https://rickandmortyapi.com/api/character/")) {
            throw new IllegalArgumentException("URL in wrong format");
        }
        return fetch(CharacterDTO.class, url);
    }

    private <T> Optional<T> fetch(Class<T> responseClass, String path) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .GET()
                .build();
        try {
            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return Optional.of(OBJECT_MAPPER.readValue(response.body(), responseClass));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}