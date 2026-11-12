package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import tn.esprit.autoloc.domain.ModePaiement;

public record PaiementResponse(
        Long idPaiement,
        BigDecimal montant,
        LocalDate datePaiement,
        ModePaiement modePaiement,
        Long contratId
) {}
