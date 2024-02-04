package br.com.lambda;

import br.com.lambda.request.LoginRequest;
import br.com.lambda.response.LoginResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UncheckedIOException;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        final var logger = context.getLogger();

        logger.log("Request received - " + request.getBody());

        try {
            final var loginRequest = objectMapper.readValue(request.getBody(), LoginRequest.class);

            var isAuthrorized = false;

            if ("admin".equalsIgnoreCase(loginRequest.username()) && "123".equalsIgnoreCase(loginRequest.password())) {
                isAuthrorized = true;
            }

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(new LoginResponse(isAuthrorized)))
                    .withIsBase64Encoded(false);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }

    }

}
