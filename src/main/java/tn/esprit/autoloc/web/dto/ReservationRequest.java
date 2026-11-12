package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.autoloc.domain.StatutReservation;

@Getter
@Setter
public class ReservationRequest {
    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @NotNull
    private StatutReservation statut;

    @Positive
    private Long clientId;

    @Positive
    private Long vehiculeId;

    @AssertTrue(message = "La date de fin doit être postérieure ou égale à la date de début")
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isPeriodeValide() {
        return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut);
    }
}
