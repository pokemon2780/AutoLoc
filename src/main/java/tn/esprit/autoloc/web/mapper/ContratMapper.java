package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;

@Component
public class ContratMapper {
    private final PaiementMapper paiementMapper;

    public ContratMapper(PaiementMapper paiementMapper) {
        this.paiementMapper = paiementMapper;
    }

    public ContratResponse toResponse(Contrat entity) {
        return new ContratResponse(
                entity.getIdContrat(),
                entity.getDateSignature(),
                entity.getMontantTotal(),
                entity.isValide(),
                entity.getReservation() == null ? null : entity.getReservation().getIdReservation(),
                entity.getPaiements().stream().map(paiementMapper::toResponse).toList());
    }

    public Contrat toEntity(ContratRequest request) {
        Contrat entity = new Contrat();
        entity.setDateSignature(request.getDateSignature());
        entity.setMontantTotal(request.getMontantTotal());
        entity.setValide(request.getValide());
        request.getPaiements().forEach(item -> entity.ajouterPaiement(paiementMapper.toEntity(item)));
        return entity;
    }
}
