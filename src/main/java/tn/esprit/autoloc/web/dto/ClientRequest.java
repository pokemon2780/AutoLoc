package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClientRequest {
    @NotBlank @Size(max = 255)
    private String nom;

    @NotBlank @Size(max = 255)
    private String prenom;

    @NotBlank @Email @Size(max = 255)
    private String email;

    @NotBlank @Pattern(regexp = "[+0-9() .-]{6,30}", message = "Numéro de téléphone invalide")
    private String telephone;

    @NotBlank @Size(max = 50)
    private String numPermis;

    @NotNull @PastOrPresent
    private LocalDate dateInscription;

}
