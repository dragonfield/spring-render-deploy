package com.example.presentation.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(
        @JsonProperty("code")
        String code,
        @JsonProperty("message")
        String message
) {
}
