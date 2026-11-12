package tn.esprit.autoloc.web.dto;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(int statut, String message, Instant horodatage,
                               String chemin, Map<String, String> erreurs) {}
