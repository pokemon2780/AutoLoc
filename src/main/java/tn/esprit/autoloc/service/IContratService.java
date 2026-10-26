package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Contrat;

public interface IContratService {
    List<Contrat> findAll();

    Contrat findById(Long id);

    Contrat create(Contrat contrat);

    Contrat update(Long id, Contrat contrat);

    void delete(Long id);

    default void deleteById(Long id) {
        delete(id);
    }

    Contrat addPaiement(Long contratId, tn.esprit.autoloc.domain.Paiement paiement);

    Contrat removePaiement(Long contratId, Long paiementId);
}
