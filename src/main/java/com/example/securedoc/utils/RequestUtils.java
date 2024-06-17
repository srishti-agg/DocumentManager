package com.example.securedoc.utils;

import com.example.securedoc.domain.Response;
import com.example.securedoc.exception.APIException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.nio.file.AccessDeniedException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.time.LocalTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RequestUtils {

    // TO SET RESPONSE IN CASE OF EXCEPTION
    private static final BiConsumer<HttpServletResponse, Response> writeResponse =
            (HttpServletResponse, response)->{
        try {
            var outputStream = HttpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream, response);
            outputStream.flush();

        }catch (Exception exception){

            throw new APIException(exception.getMessage());
        }
    };

    private static final BiFunction<Exception, HttpStatus, String> errorReason =
            ((exception, httpStatus) -> {
                if(httpStatus.isSameCodeAs(FORBIDDEN)){
                    return "You don't have enough permission";
                } else if (httpStatus.isSameCodeAs(UNAUTHORIZED)) {
                    return "You are not logged in";
                } else if(exception instanceof BadCredentialsException || exception instanceof DisabledException
            || exception instanceof LockedException || exception instanceof CredentialsExpiredException ||
                exception instanceof APIException){
                    return exception.getMessage();
                } else if(httpStatus.is5xxServerError()){
                    return "An internal server error occurred!";
                }
                return "An error occurred. Please try again.";
            });

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                           Exception exception){
        if (exception instanceof AccessDeniedException){
            Response apiResponse = getErrorResponse(request, response, exception, FORBIDDEN);
            writeResponse.accept(response, apiResponse);
        }
    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus httpStatus) {

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());

        return new Response(now().toString(), httpStatus.value(),request.getRequestURI(),
                HttpStatus.valueOf(httpStatus.value()), errorReason.apply(exception, httpStatus), getRootCauseMessage(exception),emptyMap());
    }
}
