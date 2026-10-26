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
}
