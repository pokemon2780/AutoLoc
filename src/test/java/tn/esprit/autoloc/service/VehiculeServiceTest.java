package tn.esprit.autoloc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehiculeServiceTest {

    private IVehiculeRepository repository;
    private VehiculeServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(IVehiculeRepository.class);
        service = new VehiculeServiceImpl(repository);
    }

    @Test
    void createReturnsTheSavedEntity() {
        Vehicule input = new Vehicule();
        input.setTarifJournalier(BigDecimal.TEN);
        Vehicule saved = new Vehicule();
        saved.setIdVehicule(1L);
        when(repository.save(input)).thenReturn(saved);
        assertSame(saved, service.create(input));
    }

    @Test
    void createRejectsAnExistingIdentifier() {
        Vehicule input = new Vehicule();
        input.setIdVehicule(1L);
        assertThrows(IllegalArgumentException.class, () -> service.create(input));
        verifyNoInteractions(repository);
    }

    @Test
    void readReturnsAllEntitiesAndOneEntity() {
        Vehicule entity = new Vehicule();
        when(repository.findAll()).thenReturn(List.of(entity));
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        assertEquals(List.of(entity), service.findAll());
        assertSame(entity, service.findById(1L));
    }

    @Test
    void updatePreservesIdentifierAndAssociations() {
        Vehicule existing = new Vehicule();
        existing.setIdVehicule(1L);
        List<Maintenance> links = new ArrayList<>();
        links.add(new Maintenance());
        existing.setMaintenances(links);
        Vehicule input = new Vehicule();
        input.setIdVehicule(99L);
        input.setMarque("Valeur modifiée");
        input.setTarifJournalier(BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Vehicule result = service.update(1L, input);

        assertEquals(1L, result.getIdVehicule());
        assertEquals("Valeur modifiée", result.getMarque());
        assertSame(links, result.getMaintenances());
        assertEquals(1, result.getMaintenances().size());
    }

    @Test
    void missingEntityCannotBeReadUpdatedOrDeleted() {
        when(repository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(tn.esprit.autoloc.exception.ResourceNotFoundException.class, () -> service.findById(404L));
        assertThrows(tn.esprit.autoloc.exception.ResourceNotFoundException.class, () -> service.update(404L, new Vehicule()));
        assertThrows(tn.esprit.autoloc.exception.ResourceNotFoundException.class, () -> service.deleteById(404L));
        verify(repository, never()).save(any(Vehicule.class));
        verify(repository, never()).delete(any(Vehicule.class));
    }

    @Test
    void deleteRemovesTheExistingEntity() {
        Vehicule existing = new Vehicule();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        service.deleteById(1L);
        verify(repository).delete(existing);
    }
}
