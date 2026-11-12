package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.web.dto.PaiementRequest;
import tn.esprit.autoloc.web.dto.PaiementResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import tn.esprit.autoloc.service.dto.PaiementDtoService;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements")
public class PaiementController {

    private final PaiementDtoService service;

    @Operation(summary = "Lister les paiements")
    @ApiResponse(responseCode = "200", description = "Liste des paiements retournée")
    @GetMapping
    public List<PaiementResponse> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter un paiement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    @GetMapping("/{id}")
    public PaiementResponse findById(@Parameter(description = "Identifiant du paiement") @PathVariable @Positive Long id) {
        return service.findById(id);
    }
}
