package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.service.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ClientService implements IClientService {

    private final IClientRepository repository;

    public ClientService(IClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client findById(Long id) {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client", id));
    }

    @Override
    @Transactional
    public Client create(Client client) {
        Objects.requireNonNull(client, "L'entité est obligatoire");
        validate(client);
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouvel objet ne doit pas avoir d'identifiant");
        }
        return repository.save(client);
    }

    @Override
    @Transactional
    public Client update(Long id, Client client) {
        Objects.requireNonNull(client, "L'entité est obligatoire");
        Client existing = findById(id);
        validate(client);
        existing.setNom(client.getNom());
        existing.setPrenom(client.getPrenom());
        existing.setEmail(client.getEmail());
        existing.setTelephone(client.getTelephone());
        existing.setNumPermis(client.getNumPermis());
        existing.setDateInscription(client.getDateInscription());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Client existing = findById(id);
        repository.delete(existing);
    }

    private void validate(Client client) {
        if (client.getNom() == null || client.getNom().isBlank()
                || client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalArgumentException("Le nom et l'e-mail sont obligatoires");
        }
    }
}
