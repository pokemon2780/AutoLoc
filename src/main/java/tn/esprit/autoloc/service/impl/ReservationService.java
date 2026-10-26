package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ReservationService implements IReservationService {

    private final IReservationRepository repository;

    public ReservationService(IReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation", id));
    }

    @Override
    @Transactional
    public Reservation create(Reservation entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        if (entity.getIdReservation() != null) throw new IllegalArgumentException("Un nouvel objet ne doit pas avoir d'identifiant");
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Reservation update(Long id, Reservation entity) {
        Objects.requireNonNull(entity, "L'entité est obligatoire");
        validate(entity);
        Reservation existing = findById(id);
        existing.setDateDebut(entity.getDateDebut());
        existing.setDateFin(entity.getDateFin());
        existing.setStatut(entity.getStatut());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Reservation entity) {
        if (entity.getDateDebut() != null && entity.getDateFin() != null && entity.getDateFin().isBefore(entity.getDateDebut())) throw new IllegalArgumentException("La date de fin doit suivre la date de début");
    }
}
