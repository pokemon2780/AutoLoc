package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class EmployeService implements IEmployeService {

    private final IEmployeRepository repository;

    public EmployeService(IEmployeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employe> findAll() {
        return repository.findAll();
    }

    @Override
    public Employe findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employe", id));
    }

    @Override
    @Transactional
    public Employe create(Employe entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        if (entity.getIdEmploye() != null) throw new IllegalArgumentException("Un nouvel objet ne doit pas avoir d'identifiant");
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Employe update(Long id, Employe entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        validate(entity);
        Employe existing = findById(id);
        existing.setNom(entity.getNom());
        existing.setPrenom(entity.getPrenom());
        existing.setRole(entity.getRole());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Employe entity) {
        if (entity.getNom() == null || entity.getNom().isBlank()) throw new IllegalArgumentException("Le nom de l’employé est obligatoire");
    }
}
