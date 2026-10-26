package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ContratService implements IContratService {

    private final IContratRepository repository;

    public ContratService(IContratRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Contrat> findAll() {
        return repository.findAll();
    }

    @Override
    public Contrat findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat", id));
    }
}
