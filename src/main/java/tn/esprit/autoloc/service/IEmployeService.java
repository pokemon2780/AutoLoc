package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Employe;

public interface IEmployeService {
    List<Employe> findAll();

    Employe findById(Long id);
}
