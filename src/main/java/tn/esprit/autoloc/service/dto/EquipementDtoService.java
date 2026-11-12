package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipementDtoService {
    private final IEquipementService service;
    private final EquipementMapper mapper;

    public List<EquipementResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public EquipementResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public EquipementResponse create(EquipementRequest request) {
        Equipement entity = mapper.toEntity(request);
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public EquipementResponse update(Long id, EquipementRequest request) {
        Equipement entity = service.update(id, mapper.toEntity(request));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
