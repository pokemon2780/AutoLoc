package tn.esprit.autoloc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContratServiceImpl implements IContratService {

    private final IContratRepository repository;

    @Override
    public List<Contrat> findAll() {
        return repository.findAll();
    }

    @Override
    public Contrat findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    @Transactional
    public Contrat create(Contrat contrat) {
        Objects.requireNonNull(contrat, "Le contrat est obligatoire");
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        validate(contrat);
        if (contrat.getPaiements() == null) {
            throw new IllegalArgumentException("La liste des paiements est obligatoire");
        }
        for (Paiement paiement : contrat.getPaiements()) {
            Objects.requireNonNull(paiement, "Un paiement ne peut pas etre null");
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
        Contrat existant = findById(id);
        validate(contrat);
        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.isValide());
        return repository.save(existant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        repository.deleteById(id);
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
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", paiementId));
        contrat.retirerPaiement(paiement);
        return repository.save(contrat);
    }

    private void validate(Contrat contrat) {
        if (contrat.getMontantTotal() != null
                && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas etre negatif");
        }
    }
}
