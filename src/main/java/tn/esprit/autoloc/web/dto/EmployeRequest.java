package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.autoloc.domain.RoleEmploye;

@Getter
@Setter
public class EmployeRequest {
    @NotBlank @Size(max = 255)
    private String nom;

    @NotBlank @Size(max = 255)
    private String prenom;

    @NotNull
    private RoleEmploye role;

    @Positive
    private Long agenceId;

}
