package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationDtoService {
    private final IReservationService service;
    private final ReservationMapper mapper;
    private final ReferenceResolver references;

    public List<ReservationResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public ReservationResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        Reservation entity = mapper.toEntity(request);
        entity.setClient(references.client(request.getClientId()));
        entity.setVehicule(references.vehicule(request.getVehiculeId()));
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public ReservationResponse update(Long id, ReservationRequest request) {
        Reservation entity = service.update(id, mapper.toEntity(request));
        entity.setClient(references.client(request.getClientId()));
        entity.setVehicule(references.vehicule(request.getVehiculeId()));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
