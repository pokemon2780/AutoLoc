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
}
