package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Maintenance;

public interface IMaintenanceService {
    List<Maintenance> findAll();

    Maintenance findById(Long id);
}
