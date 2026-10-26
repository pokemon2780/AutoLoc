package tn.esprit.autoloc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.impl.ClientService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private IClientRepository repository;
    private ClientService service;

    @BeforeEach
    void setUp() {
        repository = mock(IClientRepository.class);
        service = new ClientService(repository);
    }

    @Test
    void createReturnsTheSavedEntity() {
        Client input = new Client();
        input.setNom("Alice");
        input.setEmail("alice@example.com");
        Client saved = new Client();
        saved.setIdClient(1L);
        when(repository.save(input)).thenReturn(saved);
        assertSame(saved, service.create(input));
    }

    @Test
    void createRejectsAnExistingIdentifier() {
        Client input = new Client();
        input.setIdClient(1L);
        assertThrows(IllegalArgumentException.class, () -> service.create(input));
        verifyNoInteractions(repository);
    }

    @Test
    void readReturnsAllEntitiesAndOneEntity() {
        Client entity = new Client();
        when(repository.findAll()).thenReturn(List.of(entity));
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        assertEquals(List.of(entity), service.findAll());
        assertSame(entity, service.findById(1L));
    }

    @Test
    void updatePreservesIdentifierAndAssociations() {
        Client existing = new Client();
        existing.setIdClient(1L);
        List<Reservation> links = new ArrayList<>();
        links.add(new Reservation());
        existing.setReservations(links);
        Client input = new Client();
        input.setIdClient(99L);
        input.setNom("Valeur modifiée");
        input.setEmail("alice@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Client result = service.update(1L, input);

        assertEquals(1L, result.getIdClient());
        assertEquals("Valeur modifiée", result.getNom());
        assertSame(links, result.getReservations());
        assertEquals(1, result.getReservations().size());
    }

    @Test
    void missingEntityCannotBeReadUpdatedOrDeleted() {
        when(repository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findById(404L));
        assertThrows(EntityNotFoundException.class, () -> service.update(404L, new Client()));
        assertThrows(EntityNotFoundException.class, () -> service.delete(404L));
        verify(repository, never()).save(any(Client.class));
        verify(repository, never()).delete(any(Client.class));
    }

    @Test
    void deleteRemovesTheExistingEntity() {
        Client existing = new Client();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        service.delete(1L);
        verify(repository).delete(existing);
    }
}
