package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class EquipementService implements IEquipementService {

    private final IEquipementRepository repository;

    public EquipementService(IEquipementRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Equipement> findAll() {
        return repository.findAll();
    }

    @Override
    public Equipement findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipement", id));
    }

    @Override
    @Transactional
    public Equipement create(Equipement entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        if (entity.getIdEquipement() != null) throw new IllegalArgumentException("Un nouvel objet ne doit pas avoir d'identifiant");
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Equipement update(Long id, Equipement entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        validate(entity);
        Equipement existing = findById(id);
        existing.setLibelle(entity.getLibelle());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Equipement entity) {
        if (entity.getLibelle() == null || entity.getLibelle().isBlank()) throw new IllegalArgumentException("Le libellé est obligatoire");
    }
}
