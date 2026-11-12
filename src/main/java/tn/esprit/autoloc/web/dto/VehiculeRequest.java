package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.CategorieVehicule;

@Getter
@Setter
public class VehiculeRequest {
    @NotBlank @Size(max = 20)
    private String immatriculation;

    @NotBlank @Size(max = 50)
    private String marque;

    @NotBlank @Size(max = 50)
    private String modele;

    @NotNull
    private CategorieVehicule categorie;

    @NotNull @DecimalMin("0.00") @Digits(integer = 8, fraction = 2)
    private BigDecimal tarifJournalier;

    @NotNull
    private StatutVehicule statut;

    @Positive
    private Long agenceId;

    @NotNull @Size(max = 100)
    private List<@NotNull @Positive Long> equipementIds = new ArrayList<>();

}
