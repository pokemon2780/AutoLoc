package tn.esprit.autoloc.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import tn.esprit.autoloc.web.controller.ApiExceptionHandler;
import tn.esprit.autoloc.web.dto.ApiErrorResponse;
import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {
    @Test
    void unexpectedErrorsDoNotExposeInternalDetails() {
        var request = new ServletWebRequest(new MockHttpServletRequest("GET", "/clients"));
        var response = new ApiExceptionHandler().unexpected(new RuntimeException("secret SQL password"), request);
        assertEquals(500, response.getStatusCode().value());
        ApiErrorResponse body = (ApiErrorResponse) response.getBody();
        assertNotNull(body);
        assertEquals("Erreur interne du serveur", body.message());
        assertFalse(body.toString().contains("secret"));
    }
}
