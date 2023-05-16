package org.example;


import java.util.List;

public record CharacterResponseDTO(
        InfoDTO info,
        List<CharacterDTO> results) {
}
