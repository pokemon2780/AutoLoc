package tn.esprit.autoloc.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ContratRequest {
    @NotNull
    private LocalDate dateSignature;

    @NotNull @DecimalMin("0.00") @Digits(integer = 8, fraction = 2)
    private BigDecimal montantTotal;

    @NotNull
    private Boolean valide;

    @Positive
    private Long reservationId;

    @NotNull @Size(max = 100)
    private List<@NotNull @Valid PaiementRequest> paiements = new ArrayList<>();

}
