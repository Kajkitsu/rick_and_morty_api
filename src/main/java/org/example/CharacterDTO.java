package org.example;

import java.util.List;

public record CharacterDTO(
    Integer id,
    String name,
    String status,
    String species,
    String type,
    String gender,
    String image,
    List<String> episode,
    String url,
    String created
){}