package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AgenceRequest {
    @NotBlank @Size(max = 255)
    private String nom;

    @NotBlank @Size(max = 255)
    private String ville;

    @NotBlank @Size(max = 255)
    private String adresse;

    @NotBlank @Pattern(regexp = "[+0-9() .-]{6,30}", message = "Numéro de téléphone invalide")
    private String telephone;

}
