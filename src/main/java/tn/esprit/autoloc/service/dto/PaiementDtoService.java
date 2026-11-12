package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementResponse;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaiementDtoService {
    private final IPaiementService service;
    private final PaiementMapper mapper;

    public List<PaiementResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public PaiementResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }
}
