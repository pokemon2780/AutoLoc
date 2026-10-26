package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class AgenceService implements IAgenceService {

    private final IAgenceRepository repository;

    public AgenceService(IAgenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Agence> findAll() {
        return repository.findAll();
    }

    @Override
    public Agence findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence", id));
    }

    @Override
    @Transactional
    public Agence create(Agence entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        if (entity.getIdAgence() != null) throw new IllegalArgumentException("Un nouvel objet ne doit pas avoir d'identifiant");
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Agence update(Long id, Agence entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        validate(entity);
        Agence existing = findById(id);
        existing.setNom(entity.getNom());
        existing.setVille(entity.getVille());
        existing.setAdresse(entity.getAdresse());
        existing.setTelephone(entity.getTelephone());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Agence entity) {
        if (entity.getNom() == null || entity.getNom().isBlank()) throw new IllegalArgumentException("Le nom de l’agence est obligatoire");
    }
}
