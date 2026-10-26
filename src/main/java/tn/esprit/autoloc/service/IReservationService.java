package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Reservation;

public interface IReservationService {
    List<Reservation> findAll();

    Reservation findById(Long id);
    Reservation create(Reservation entity);

    Reservation update(Long id, Reservation entity);

    void delete(Long id);

    default void deleteById(Long id) {
        delete(id);
    }
}
