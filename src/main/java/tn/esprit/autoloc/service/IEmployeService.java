package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Employe;

public interface IEmployeService {
    List<Employe> findAll();

    Employe findById(Long id);
    Employe create(Employe entity);

    Employe update(Long id, Employe entity);

    void delete(Long id);

    default void deleteById(Long id) {
        delete(id);
    }
}
