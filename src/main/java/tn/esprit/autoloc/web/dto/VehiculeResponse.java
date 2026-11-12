package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.util.List;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.CategorieVehicule;

public record VehiculeResponse(
        Long idVehicule,
        String immatriculation,
        String marque,
        String modele,
        CategorieVehicule categorie,
        BigDecimal tarifJournalier,
        StatutVehicule statut,
        Long agenceId,
        List<Long> equipementIds
) {}
