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
}
