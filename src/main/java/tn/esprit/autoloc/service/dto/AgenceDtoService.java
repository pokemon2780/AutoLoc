package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgenceDtoService {
    private final IAgenceService service;
    private final AgenceMapper mapper;

    public List<AgenceResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public AgenceResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public AgenceResponse create(AgenceRequest request) {
        Agence entity = mapper.toEntity(request);
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public AgenceResponse update(Long id, AgenceRequest request) {
        Agence entity = service.update(id, mapper.toEntity(request));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
