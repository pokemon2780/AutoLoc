
package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;

    @OneToOne
    @JoinColumn(name = "id_reservation", unique = true)
    private Reservation reservation;

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paiement> paiements = new ArrayList<>();

    public void ajouterPaiement(Paiement paiement) {
        if (paiement == null) {
            throw new IllegalArgumentException("Le paiement est obligatoire");
        }
        if (paiement.getContrat() != null && paiement.getContrat() != this) {
            throw new IllegalArgumentException("Le paiement appartient déjà à un autre contrat");
        }
        if (!paiements.contains(paiement)) {
            paiements.add(paiement);
        }
        paiement.setContrat(this);
    }

    public void retirerPaiement(Paiement paiement) {
        if (paiements.remove(paiement)) {
            paiement.setContrat(null);
        }
    }
}
