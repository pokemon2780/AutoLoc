package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;

@Component
public class AgenceMapper {
    public AgenceResponse toResponse(Agence entity) {
        return new AgenceResponse(
                entity.getIdAgence(),
                entity.getNom(),
                entity.getVille(),
                entity.getAdresse(),
                entity.getTelephone());
    }

    public Agence toEntity(AgenceRequest request) {
        Agence entity = new Agence();
        entity.setNom(request.getNom());
        entity.setVille(request.getVille());
        entity.setAdresse(request.getAdresse());
        entity.setTelephone(request.getTelephone());
        return entity;
    }
}
