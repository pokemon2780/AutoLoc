package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.web.dto.MaintenanceRequest;
import tn.esprit.autoloc.web.dto.MaintenanceResponse;

@Component
public class MaintenanceMapper {
    public MaintenanceResponse toResponse(Maintenance entity) {
        return new MaintenanceResponse(
                entity.getIdMaintenance(),
                entity.getDateDebut(),
                entity.getDateFin(),
                entity.getDescription(),
                entity.getVehicule() == null ? null : entity.getVehicule().getIdVehicule());
    }

    public Maintenance toEntity(MaintenanceRequest request) {
        Maintenance entity = new Maintenance();
        entity.setDateDebut(request.getDateDebut());
        entity.setDateFin(request.getDateFin());
        entity.setDescription(request.getDescription());
        return entity;
    }
}
