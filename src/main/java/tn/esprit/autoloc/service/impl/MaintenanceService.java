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

    @Override
    @Transactional
    public Maintenance create(Maintenance entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        if (entity.getIdMaintenance() != null) throw new IllegalArgumentException("Un nouvel objet ne doit pas avoir d'identifiant");
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Maintenance update(Long id, Maintenance entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        validate(entity);
        Maintenance existing = findById(id);
        existing.setDateDebut(entity.getDateDebut());
        existing.setDateFin(entity.getDateFin());
        existing.setDescription(entity.getDescription());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Maintenance entity) {
        if (entity.getDateDebut() != null && entity.getDateFin() != null && entity.getDateFin().isBefore(entity.getDateDebut())) throw new IllegalArgumentException("La date de fin doit suivre la date de début");
    }
}
