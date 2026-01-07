package es.esimarket.backend.controllers.responses;

public record MessageResponse(
        String message,
        boolean isUser,
        String day,
        String hour
)
{}
