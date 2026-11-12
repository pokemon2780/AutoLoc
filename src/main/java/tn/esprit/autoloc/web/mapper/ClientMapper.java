package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;

@Component
public class ClientMapper {
    public ClientResponse toResponse(Client entity) {
        return new ClientResponse(
                entity.getIdClient(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getEmail(),
                entity.getTelephone(),
                entity.getNumPermis(),
                entity.getDateInscription());
    }

    public Client toEntity(ClientRequest request) {
        Client entity = new Client();
        entity.setNom(request.getNom());
        entity.setPrenom(request.getPrenom());
        entity.setEmail(request.getEmail());
        entity.setTelephone(request.getTelephone());
        entity.setNumPermis(request.getNumPermis());
        entity.setDateInscription(request.getDateInscription());
        return entity;
    }
}
