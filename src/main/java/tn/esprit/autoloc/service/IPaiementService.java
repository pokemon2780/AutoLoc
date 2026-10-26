package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Paiement;

public interface IPaiementService {
    List<Paiement> findAll();

    Paiement findById(Long id);
}
