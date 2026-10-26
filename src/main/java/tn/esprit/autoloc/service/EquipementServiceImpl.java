package tn.esprit.autoloc.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository repository;

    @Override
    public List<Equipement> findAll() {
        return repository.findAll();
    }

    @Override
    public Equipement findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }

    @Override
    @Transactional
    public Equipement create(Equipement equipement) {
        Objects.requireNonNull(equipement, "L'equipement est obligatoire");
        if (equipement.getIdEquipement() != null) {
            throw new IllegalArgumentException("Un nouvel equipement ne doit pas avoir d'identifiant");
        }
        validate(equipement);
        return repository.save(equipement);
    }

    @Override
    @Transactional
    public Equipement update(Long id, Equipement equipement) {
        Objects.requireNonNull(equipement, "L'equipement est obligatoire");
        Equipement existant = findById(id);
        validate(equipement);
        existant.setLibelle(equipement.getLibelle());
        return repository.save(existant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        repository.deleteById(id);
    }

    private void validate(Equipement equipement) {
        if (equipement.getLibelle() == null || equipement.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libelle de l'equipement est obligatoire");
        }
    }
}
