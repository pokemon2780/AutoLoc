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
import tn.esprit.autoloc.web.dto.AgenceRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import tn.esprit.autoloc.service.dto.AgenceDtoService;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences AutoLoc")
public class AgenceController {

    private final AgenceDtoService service;

    @Operation(summary = "Lister les agences")
    @ApiResponse(responseCode = "200", description = "Liste des agences retournée")
    @GetMapping
    public List<AgenceResponse> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence trouvée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @GetMapping("/{id}")
    public AgenceResponse findById(@Parameter(description = "Identifiant de l'agence") @PathVariable @Positive Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agence créée"),
            @ApiResponse(responseCode = "400", description = "Données de l'agence invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenceResponse create(@Valid @RequestBody AgenceRequest entity) {
        return service.create(entity);
    }

    @Operation(summary = "Modifier une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence modifiée"),
            @ApiResponse(responseCode = "400", description = "Données de l'agence invalides"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @PutMapping("/{id}")
    public AgenceResponse update(
            @Parameter(description = "Identifiant de l'agence") @PathVariable @Positive Long id,
            @Valid @RequestBody AgenceRequest entity) {
        return service.update(id, entity);
    }

    @Operation(summary = "Supprimer une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agence supprimée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "Identifiant de l'agence") @PathVariable @Positive Long id) {
        service.deleteById(id);
    }
}
