package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementRequest;
import tn.esprit.autoloc.web.dto.PaiementResponse;

@Component
public class PaiementMapper {
    public PaiementResponse toResponse(Paiement entity) {
        return new PaiementResponse(
                entity.getIdPaiement(),
                entity.getMontant(),
                entity.getDatePaiement(),
                entity.getModePaiement(),
                entity.getContrat() == null ? null : entity.getContrat().getIdContrat());
    }

    public Paiement toEntity(PaiementRequest request) {
        Paiement entity = new Paiement();
        entity.setMontant(request.getMontant());
        entity.setDatePaiement(request.getDatePaiement());
        entity.setModePaiement(request.getModePaiement());
        return entity;
    }
}
