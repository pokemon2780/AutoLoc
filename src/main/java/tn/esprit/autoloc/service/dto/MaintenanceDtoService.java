package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceRequest;
import tn.esprit.autoloc.web.dto.MaintenanceResponse;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceDtoService {
    private final IMaintenanceService service;
    private final MaintenanceMapper mapper;
    private final ReferenceResolver references;

    public List<MaintenanceResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public MaintenanceResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public MaintenanceResponse create(MaintenanceRequest request) {
        Maintenance entity = mapper.toEntity(request);
        entity.setVehicule(references.vehicule(request.getVehiculeId()));
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public MaintenanceResponse update(Long id, MaintenanceRequest request) {
        Maintenance entity = service.update(id, mapper.toEntity(request));
        entity.setVehicule(references.vehicule(request.getVehiculeId()));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
