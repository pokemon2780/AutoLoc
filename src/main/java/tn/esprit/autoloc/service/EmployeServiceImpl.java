package tn.esprit.autoloc.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository repository;

    @Override
    public List<Employe> findAll() {
        return repository.findAll();
    }

    @Override
    public Employe findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    @Transactional
    public Employe create(Employe employe) {
        Objects.requireNonNull(employe, "L'employe est obligatoire");
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouvel employe ne doit pas avoir d'identifiant");
        }
        validate(employe);
        return repository.save(employe);
    }

    @Override
    @Transactional
    public Employe update(Long id, Employe employe) {
        Objects.requireNonNull(employe, "L'employe est obligatoire");
        Employe existant = findById(id);
        validate(employe);
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        return repository.save(existant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        repository.deleteById(id);
    }

    private void validate(Employe employe) {
        if (employe.getNom() == null || employe.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'employe est obligatoire");
        }
        if (employe.getRole() == null) {
            throw new IllegalArgumentException("Le role de l'employe est obligatoire");
        }
    }
}
