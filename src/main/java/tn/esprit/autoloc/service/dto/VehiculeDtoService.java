package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehiculeDtoService {
    private final IVehiculeService service;
    private final VehiculeMapper mapper;
    private final ReferenceResolver references;

    public List<VehiculeResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public VehiculeResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public VehiculeResponse create(VehiculeRequest request) {
        Vehicule entity = mapper.toEntity(request);
        entity.setAgence(references.agence(request.getAgenceId()));
        entity.setEquipements(references.equipements(request.getEquipementIds()));
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public VehiculeResponse update(Long id, VehiculeRequest request) {
        Vehicule entity = service.update(id, mapper.toEntity(request));
        entity.setAgence(references.agence(request.getAgenceId()));
        entity.setEquipements(references.equipements(request.getEquipementIds()));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
