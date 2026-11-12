package tn.esprit.autoloc.web.dto;

import java.time.LocalDate;
import tn.esprit.autoloc.domain.StatutReservation;

public record ReservationResponse(
        Long idReservation,
        LocalDate dateDebut,
        LocalDate dateFin,
        StatutReservation statut,
        Long clientId,
        Long vehiculeId
) {}
