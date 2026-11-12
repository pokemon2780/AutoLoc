package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;

@Component
public class EquipementMapper {
    public EquipementResponse toResponse(Equipement entity) {
        return new EquipementResponse(
                entity.getIdEquipement(),
                entity.getLibelle());
    }

    public Equipement toEntity(EquipementRequest request) {
        Equipement entity = new Equipement();
        entity.setLibelle(request.getLibelle());
        return entity;
    }
}
