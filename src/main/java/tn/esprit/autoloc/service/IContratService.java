package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Contrat;

public interface IContratService {
    List<Contrat> findAll();

    Contrat findById(Long id);
}
