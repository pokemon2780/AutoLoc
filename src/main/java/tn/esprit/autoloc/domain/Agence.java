
package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;
    private String ville;
    private String adresse;
    private String telephone;

    @OneToMany(mappedBy = "agence")
    @JsonIgnore
    private List<Employe> employes = new ArrayList<>();

    @OneToMany(mappedBy = "agence")
    @JsonIgnore
    private List<Vehicule> vehicules = new ArrayList<>();
}
