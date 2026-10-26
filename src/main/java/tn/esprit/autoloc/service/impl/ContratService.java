package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
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

    @Override
    @Transactional
    public Contrat create(Contrat contrat) {
        Objects.requireNonNull(contrat, "Le contrat est obligatoire");
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        if (contrat.getPaiements() == null) {
            throw new IllegalArgumentException("La liste des paiements est obligatoire");
        }
        for (Paiement paiement : contrat.getPaiements()) {
            Objects.requireNonNull(paiement, "Un paiement ne peut pas être null");
            if (paiement.getIdPaiement() != null) {
                throw new IllegalArgumentException("Un nouveau paiement ne doit pas avoir d'identifiant");
            }
            paiement.setContrat(contrat);
        }
        return repository.save(contrat);
    }

    @Override
    @Transactional
    public Contrat update(Long id, Contrat contrat) {
        Objects.requireNonNull(contrat, "Le contrat est obligatoire");
        Contrat existing = findById(id);
        existing.setDateSignature(contrat.getDateSignature());
        existing.setMontantTotal(contrat.getMontantTotal());
        existing.setValide(contrat.isValide());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    @Transactional
    public Contrat addPaiement(Long contratId, Paiement paiement) {
        Objects.requireNonNull(paiement, "Le paiement est obligatoire");
        if (paiement.getIdPaiement() != null) {
            throw new IllegalArgumentException("Un nouveau paiement ne doit pas avoir d'identifiant");
        }
        Contrat contrat = findById(contratId);
        contrat.ajouterPaiement(paiement);
        return repository.save(contrat);
    }

    @Override
    @Transactional
    public Contrat removePaiement(Long contratId, Long paiementId) {
        Objects.requireNonNull(paiementId, "L'identifiant du paiement est obligatoire");
        Contrat contrat = findById(contratId);
        Paiement paiement = contrat.getPaiements().stream()
                .filter(p -> paiementId.equals(p.getIdPaiement()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Paiement", paiementId));
        contrat.retirerPaiement(paiement);
        return repository.save(contrat);
    }
}
