package es.esimarket.backend.controllers.responses;
import java.math.BigInteger;

public record MessageResponse(
        BigInteger id,
        String message,
        String senderDNI,
        String day,
        String hour,
        String clientId
)
{}
