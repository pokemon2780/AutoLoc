package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Maintenance;

public interface IMaintenanceService {
    List<Maintenance> findAll();

    Maintenance findById(Long id);
    Maintenance create(Maintenance entity);

    Maintenance update(Long id, Maintenance entity);

    void delete(Long id);

    default void deleteById(Long id) {
        delete(id);
    }
}
