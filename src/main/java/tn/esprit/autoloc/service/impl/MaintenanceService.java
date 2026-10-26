package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class MaintenanceService implements IMaintenanceService {

    private final IMaintenanceRepository repository;

    public MaintenanceService(IMaintenanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Maintenance> findAll() {
        return repository.findAll();
    }

    @Override
    public Maintenance findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance", id));
    }
}
