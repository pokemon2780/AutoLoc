package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class VehiculeService implements IVehiculeService {

    private final IVehiculeRepository repository;

    public VehiculeService(IVehiculeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Vehicule> findAll() {
        return repository.findAll();
    }

    @Override
    public Vehicule findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicule", id));
    }

    @Override
    @Transactional
    public Vehicule create(Vehicule vehicule) {
        Objects.requireNonNull(vehicule, "L'entité est obligatoire");
        validate(vehicule);
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouvel objet ne doit pas avoir d'identifiant");
        }
        return repository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule update(Long id, Vehicule vehicule) {
        Objects.requireNonNull(vehicule, "L'entité est obligatoire");
        Vehicule existing = findById(id);
        validate(vehicule);
        existing.setImmatriculation(vehicule.getImmatriculation());
        existing.setMarque(vehicule.getMarque());
        existing.setModele(vehicule.getModele());
        existing.setCategorie(vehicule.getCategorie());
        existing.setTarifJournalier(vehicule.getTarifJournalier());
        existing.setStatut(vehicule.getStatut());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Vehicule existing = findById(id);
        repository.delete(existing);
    }

    private void validate(Vehicule vehicule) {
        if (vehicule.getTarifJournalier() == null || vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier doit être positif ou nul");
        }
    }
}
