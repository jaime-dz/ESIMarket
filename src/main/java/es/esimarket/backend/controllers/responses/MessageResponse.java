package es.esimarket.backend.controllers.responses;
import java.math.BigInteger;

public record MessageResponse(
        BigInteger id,
        String message,
        String senderID,
        String day,
        String hour,
        String clientId,
        Boolean isToxic
)
{}
