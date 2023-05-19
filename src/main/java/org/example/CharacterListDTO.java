package org.example;

import java.util.List;

public record CharacterListDTO(
        InfoDTO info,
        List<CharacterDTO> results) {
}
