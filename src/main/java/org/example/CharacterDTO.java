package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CharacterDTO(
        Integer id,
        String name,
        String species,
        String type,
        String gender,
        List<String> episode,
        String url
) {
}