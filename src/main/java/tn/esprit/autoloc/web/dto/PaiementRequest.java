package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.autoloc.domain.ModePaiement;

@Getter
@Setter
public class PaiementRequest {
    @NotNull @DecimalMin("0.00") @Digits(integer = 8, fraction = 2)
    private BigDecimal montant;

    @NotNull
    private LocalDate datePaiement;

    @NotNull
    private ModePaiement modePaiement;

}
