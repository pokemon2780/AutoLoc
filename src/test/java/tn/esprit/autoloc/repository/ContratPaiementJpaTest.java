package tn.esprit.autoloc.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import jakarta.persistence.EntityManager;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;

@DataJpaTest
class ContratPaiementJpaTest {

    @Autowired
    private IContratRepository contrats;

    @Autowired
    private IPaiementRepository paiements;

    @Autowired
    private EntityManager entityManager;

    @Test
    void creationModificationEtSuppressionEnCascade() {
        Contrat contrat = new Contrat();
        contrat.setMontantTotal(new BigDecimal("150.00"));
        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("50.00"));
        contrat.ajouterPaiement(paiement);

        Long contratId = contrats.saveAndFlush(contrat).getIdContrat();
        Long paiementId = paiement.getIdPaiement();
        entityManager.clear();

        assertTrue(paiements.existsById(paiementId));
        Contrat persisted = contrats.findById(contratId).orElseThrow();
        assertEquals(1, persisted.getPaiements().size());
        assertEquals(contratId, persisted.getPaiements().get(0).getContrat().getIdContrat());

        persisted.setMontantTotal(new BigDecimal("200.00"));
        contrats.saveAndFlush(persisted);
        entityManager.clear();
        assertEquals(new BigDecimal("200.00"), contrats.findById(contratId).orElseThrow().getMontantTotal());

        Contrat updated = contrats.findById(contratId).orElseThrow();
        Paiement attached = updated.getPaiements().get(0);
        updated.retirerPaiement(attached);
        contrats.saveAndFlush(updated);
        entityManager.clear();
        assertFalse(paiements.existsById(paiementId));
        assertNull(attached.getContrat());

        Paiement secondPaiement = new Paiement();
        secondPaiement.setMontant(new BigDecimal("25.00"));
        Contrat forDeletion = contrats.findById(contratId).orElseThrow();
        forDeletion.ajouterPaiement(secondPaiement);
        contrats.saveAndFlush(forDeletion);
        Long secondId = secondPaiement.getIdPaiement();
        entityManager.clear();

        contrats.deleteById(contratId);
        contrats.flush();
        entityManager.clear();
        assertFalse(contrats.existsById(contratId));
        assertFalse(paiements.existsById(secondId));
    }
}
