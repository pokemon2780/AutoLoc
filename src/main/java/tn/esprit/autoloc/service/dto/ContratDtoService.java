package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;
import tn.esprit.autoloc.web.mapper.ContratMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContratDtoService {
    private final IContratService service;
    private final ContratMapper mapper;
    private final ReferenceResolver references;
    private final tn.esprit.autoloc.web.mapper.PaiementMapper paiementMapper;
    private final jakarta.persistence.EntityManager entityManager;

    public List<ContratResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public ContratResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public ContratResponse create(ContratRequest request) {
        Contrat entity = mapper.toEntity(request);
        entity.setReservation(references.reservation(request.getReservationId()));
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public ContratResponse update(Long id, ContratRequest request) {
        if (!request.getPaiements().isEmpty()) {
            throw new IllegalArgumentException("Modifier les paiements via /contrats/{id}/paiements");
        }
        Contrat entity = service.update(id, mapper.toEntity(request));
        entity.setReservation(references.reservation(request.getReservationId()));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }

    @Transactional
    public ContratResponse addPaiement(Long id, tn.esprit.autoloc.web.dto.PaiementRequest request) {
        Contrat entity = service.addPaiement(id, paiementMapper.toEntity(request));
        entityManager.flush();
        return mapper.toResponse(entity);
    }

    @Transactional
    public ContratResponse removePaiement(Long id, Long paiementId) {
        return mapper.toResponse(service.removePaiement(id, paiementId));
    }
}
