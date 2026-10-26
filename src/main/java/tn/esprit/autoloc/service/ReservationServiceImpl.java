package tn.esprit.autoloc.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository repository;

    @Override
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    @Transactional
    public Reservation create(Reservation reservation) {
        Objects.requireNonNull(reservation, "La reservation est obligatoire");
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle reservation ne doit pas avoir d'identifiant");
        }
        validate(reservation);
        return repository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation update(Long id, Reservation reservation) {
        Objects.requireNonNull(reservation, "La reservation est obligatoire");
        Reservation existante = findById(id);
        validate(reservation);
        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        existante.setStatut(reservation.getStatut());
        return repository.save(existante);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        repository.deleteById(id);
    }

    private void validate(Reservation reservation) {
        if (reservation.getDateDebut() != null
                && reservation.getDateFin() != null
                && reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit suivre la date de debut");
        }
    }
}
