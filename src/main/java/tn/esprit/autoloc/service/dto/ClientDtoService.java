package tn.esprit.autoloc.service.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;
import tn.esprit.autoloc.web.mapper.ClientMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientDtoService {
    private final IClientService service;
    private final ClientMapper mapper;

    public List<ClientResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    public ClientResponse findById(Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @Transactional
    public ClientResponse create(ClientRequest request) {
        Client entity = mapper.toEntity(request);
        return mapper.toResponse(service.create(entity));
    }

    @Transactional
    public ClientResponse update(Long id, ClientRequest request) {
        Client entity = service.update(id, mapper.toEntity(request));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
