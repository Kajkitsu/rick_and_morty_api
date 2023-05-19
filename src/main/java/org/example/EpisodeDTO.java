package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeDTO(
        Integer id,
        String name,
        //Adnoacja wystepuje, ponieważ w json pole to posiada klucz "air_date", a w Javie nazwy zmiennych reprezentujemy camel casem (np. "airDate")
        @JsonProperty("air_date")
        String airDate,
        String episode,
        List<String> characters,
        String url
) {
}

