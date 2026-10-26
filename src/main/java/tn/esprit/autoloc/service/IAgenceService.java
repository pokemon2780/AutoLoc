package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Agence;

public interface IAgenceService {
    List<Agence> findAll();

    Agence findById(Long id);
    Agence create(Agence entity);

    Agence update(Long id, Agence entity);

    void delete(Long id);

    default void deleteById(Long id) {
        delete(id);
    }
}
