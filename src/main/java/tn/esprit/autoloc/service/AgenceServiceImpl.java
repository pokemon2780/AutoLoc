package tn.esprit.autoloc.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository repository;

    @Override
    public List<Agence> findAll() {
        return repository.findAll();
    }

    @Override
    public Agence findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    @Override
    @Transactional
    public Agence create(Agence agence) {
        Objects.requireNonNull(agence, "L'agence est obligatoire");
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException("Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        validate(agence);
        return repository.save(agence);
    }

    @Override
    @Transactional
    public Agence update(Long id, Agence agence) {
        Objects.requireNonNull(agence, "L'agence est obligatoire");
        Agence existante = findById(id);
        validate(agence);
        existante.setNom(agence.getNom());
        existante.setVille(agence.getVille());
        existante.setAdresse(agence.getAdresse());
        existante.setTelephone(agence.getTelephone());
        return repository.save(existante);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        repository.deleteById(id);
    }

    private void validate(Agence agence) {
        if (agence.getNom() == null || agence.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
    }
}
