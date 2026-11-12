package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;

@Component
public class VehiculeMapper {
    public VehiculeResponse toResponse(Vehicule entity) {
        return new VehiculeResponse(
                entity.getIdVehicule(),
                entity.getImmatriculation(),
                entity.getMarque(),
                entity.getModele(),
                entity.getCategorie(),
                entity.getTarifJournalier(),
                entity.getStatut(),
                entity.getAgence() == null ? null : entity.getAgence().getIdAgence(),
                entity.getEquipements().stream().map(item -> item.getIdEquipement()).toList());
    }

    public Vehicule toEntity(VehiculeRequest request) {
        Vehicule entity = new Vehicule();
        entity.setImmatriculation(request.getImmatriculation());
        entity.setMarque(request.getMarque());
        entity.setModele(request.getModele());
        entity.setCategorie(request.getCategorie());
        entity.setTarifJournalier(request.getTarifJournalier());
        entity.setStatut(request.getStatut());
        return entity;
    }
}
