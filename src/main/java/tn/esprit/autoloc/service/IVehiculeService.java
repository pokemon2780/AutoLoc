package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeService {
    List<Vehicule> findAll();

    Vehicule findById(Long id);

    Vehicule create(Vehicule vehicule);

    Vehicule update(Long id, Vehicule vehicule);

    void delete(Long id);

    default void deleteById(Long id) {
        delete(id);
    }
}
