package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;

@Component
public class ReservationMapper {
    public ReservationResponse toResponse(Reservation entity) {
        return new ReservationResponse(
                entity.getIdReservation(),
                entity.getDateDebut(),
                entity.getDateFin(),
                entity.getStatut(),
                entity.getClient() == null ? null : entity.getClient().getIdClient(),
                entity.getVehicule() == null ? null : entity.getVehicule().getIdVehicule());
    }

    public Reservation toEntity(ReservationRequest request) {
        Reservation entity = new Reservation();
        entity.setDateDebut(request.getDateDebut());
        entity.setDateFin(request.getDateFin());
        entity.setStatut(request.getStatut());
        return entity;
    }
}
