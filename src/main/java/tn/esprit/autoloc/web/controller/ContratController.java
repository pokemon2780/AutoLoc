package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.web.dto.ContratRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import tn.esprit.autoloc.service.dto.ContratDtoService;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final ContratDtoService service;

    @Operation(summary = "Lister les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats retournée")
    @GetMapping
    public List<ContratResponse> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public ContratResponse findById(@Parameter(description = "Identifiant du contrat") @PathVariable @Positive Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé"),
            @ApiResponse(responseCode = "400", description = "Données du contrat invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratResponse create(@Valid @RequestBody ContratRequest entity) {
        return service.create(entity);
    }

    @Operation(summary = "Modifier un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié"),
            @ApiResponse(responseCode = "400", description = "Données du contrat invalides"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @PutMapping("/{id}")
    public ContratResponse update(
            @Parameter(description = "Identifiant du contrat") @PathVariable @Positive Long id,
            @Valid @RequestBody ContratRequest entity) {
        return service.update(id, entity);
    }

    @Operation(summary = "Supprimer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "Identifiant du contrat") @PathVariable @Positive Long id) {
        service.deleteById(id);
    }

    @Operation(summary = "Ajouter un paiement au contrat")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Paiement ajouté"),
            @ApiResponse(responseCode = "400", description = "Paiement invalide"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")})
    @PostMapping("/{id}/paiements")
    @ResponseStatus(HttpStatus.CREATED)
    public ContratResponse addPaiement(@PathVariable @Positive Long id,
            @Valid @RequestBody tn.esprit.autoloc.web.dto.PaiementRequest request) {
        return service.addPaiement(id, request);
    }

    @Operation(summary = "Retirer un paiement du contrat")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Paiement retiré"),
            @ApiResponse(responseCode = "404", description = "Contrat ou paiement introuvable")})
    @DeleteMapping("/{id}/paiements/{paiementId}")
    public ContratResponse removePaiement(@PathVariable @Positive Long id,
            @PathVariable @Positive Long paiementId) {
        return service.removePaiement(id, paiementId);
    }
}
