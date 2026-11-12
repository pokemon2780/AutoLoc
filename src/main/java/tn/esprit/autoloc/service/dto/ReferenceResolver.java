package tn.esprit.autoloc.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class ReferenceResolver {
    private final IAgenceRepository agences;
    private final IClientRepository clients;
    private final IVehiculeRepository vehicules;
    private final IReservationRepository reservations;
    private final IEquipementRepository equipements;

    public Agence agence(Long id) {
        return id == null ? null : agences.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }
    public Client client(Long id) {
        return id == null ? null : clients.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }
    public Vehicule vehicule(Long id) {
        return id == null ? null : vehicules.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }
    public Reservation reservation(Long id) {
        return id == null ? null : reservations.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }
    public List<Equipement> equipements(List<Long> ids) {
        List<Equipement> result = new ArrayList<>();
        for (Long id : new LinkedHashSet<>(ids)) {
            result.add(equipements.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Equipement", id)));
        }
        return result;
    }
}
