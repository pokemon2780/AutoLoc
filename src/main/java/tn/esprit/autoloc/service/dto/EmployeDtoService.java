package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeRequest;
import tn.esprit.autoloc.web.dto.EmployeResponse;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeDtoService {
    private final IEmployeService service;
    private final EmployeMapper mapper;
    private final ReferenceResolver references;

    public List<EmployeResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public EmployeResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public EmployeResponse create(EmployeRequest request) {
        Employe entity = mapper.toEntity(request);
        entity.setAgence(references.agence(request.getAgenceId()));
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public EmployeResponse update(Long id, EmployeRequest request) {
        Employe entity = service.update(id, mapper.toEntity(request));
        entity.setAgence(references.agence(request.getAgenceId()));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
