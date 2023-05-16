package org.example;

public record InfoDTO (
    Integer count,
    Integer pages,
    String next,
    String prev
) {}