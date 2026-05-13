package com.shopcart.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionCoverageTest {

    @Test
    void testApiErrorResponseCoverage() {
        LocalDateTime now = LocalDateTime.now();
        ApiErrorResponse response = new ApiErrorResponse(now, 400, "Bad Request", "invalid", "/api/test");

        assertEquals(now, response.getTimestamp());
        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getError());
        assertEquals("invalid", response.getMessage());
        assertEquals("/api/test", response.getPath());
        assertNotNull(response.toString());

        ApiErrorResponse same = ApiErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("invalid")
                .path("/api/test")
                .build();

        assertEquals(response, same);
        assertEquals(response.hashCode(), same.hashCode());
        assertNotEquals(response, null);
        assertNotEquals(response, new Object());
        assertTrue(response.canEqual(same));
        assertFalse(response.canEqual("x"));
        assertNotNull(ApiErrorResponse.builder()
                .timestamp(now)
                .status(200)
                .error("OK")
                .message("m")
                .path("/")
                .toString());

        ApiErrorResponse different = new ApiErrorResponse(now, 500, "Server Error", "boom", "/api/x");
        assertNotEquals(response, different);
    }

    @Test
    void testResourceAndStockExceptionConstructors() {
        ResourceNotFoundException notFound = new ResourceNotFoundException("missing");
        OutOfStockException outOfStock = new OutOfStockException("no stock");

        assertEquals("missing", notFound.getMessage());
        assertEquals("no stock", outOfStock.getMessage());
    }

    @Test
    void testGlobalExceptionHandlerPaths() throws Exception {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/orders");

        ResponseEntity<ApiErrorResponse> notFound = handler.handleResourceNotFound(
                new ResourceNotFoundException("Order missing"), request);
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        assertEquals("Order missing", notFound.getBody().getMessage());
        assertEquals("/api/orders", notFound.getBody().getPath());

        ResponseEntity<ApiErrorResponse> badReq = handler.handleBadRequest(
                new IllegalArgumentException("Bad input"), request);
        assertEquals(HttpStatus.BAD_REQUEST, badReq.getStatusCode());
        assertEquals("Bad input", badReq.getBody().getMessage());

        Method method = ExceptionCoverageTest.class.getDeclaredMethod("sampleMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "quantity", "must be >= 1"));
        bindingResult.addError(new FieldError("request", "userId", "is required"));

        MethodArgumentNotValidException validationException = new MethodArgumentNotValidException(methodParameter,
                bindingResult);
        ResponseEntity<ApiErrorResponse> validation = handler.handleValidation(validationException, request);
        assertEquals(HttpStatus.BAD_REQUEST, validation.getStatusCode());
        assertTrue(validation.getBody().getMessage().contains("quantity: must be >= 1"));
        assertTrue(validation.getBody().getMessage().contains("userId: is required"));

        ConstraintViolationException cve = new ConstraintViolationException("constraint violation",
                Collections.emptySet());
        ResponseEntity<ApiErrorResponse> violation = handler.handleConstraintViolation(cve, request);
        assertEquals(HttpStatus.BAD_REQUEST, violation.getStatusCode());
        assertEquals("constraint violation", violation.getBody().getMessage());

        ResponseEntity<ApiErrorResponse> unexpected = handler.handleUnexpected(new RuntimeException("x"), request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, unexpected.getStatusCode());
        assertEquals("Unexpected server error", unexpected.getBody().getMessage());
        assertNotNull(unexpected.getBody().getTimestamp());
    }

    @SuppressWarnings("unused")
    private static void sampleMethod(String payload) {
    }
}
