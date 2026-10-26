package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class PaiementService implements IPaiementService {

    private final IPaiementRepository repository;

    public PaiementService(IPaiementRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Paiement> findAll() {
        return repository.findAll();
    }

    @Override
    public Paiement findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paiement", id));
    }
}
