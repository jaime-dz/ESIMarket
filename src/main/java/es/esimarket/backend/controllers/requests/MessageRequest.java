package es.esimarket.backend.controllers.requests;

public record MessageRequest (
        int idChat,
        String Texto,
        String clientId,
        String senderID,
        String hour
) {}
