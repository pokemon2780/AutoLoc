package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.web.dto.EquipementRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import tn.esprit.autoloc.service.dto.EquipementDtoService;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Equipements", description = "Gestion des équipements")
public class EquipementController {

    private final EquipementDtoService service;

    @Operation(summary = "Lister les équipements")
    @ApiResponse(responseCode = "200", description = "Liste des équipements retournée")
    @GetMapping
    public List<EquipementResponse> findAll() { return service.findAll(); }

    @Operation(summary = "Consulter un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @GetMapping("/{id}")
    public EquipementResponse findById(@PathVariable @Positive Long id) { return service.findById(id); }

    @Operation(summary = "Créer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Équipement créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipementResponse create(@Valid @RequestBody EquipementRequest entity) { return service.create(entity); }

    @Operation(summary = "Modifier un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @PutMapping("/{id}")
    public EquipementResponse update(@PathVariable @Positive Long id, @Valid @RequestBody EquipementRequest entity) {
        return service.update(id, entity);
    }

    @Operation(summary = "Supprimer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Équipement supprimé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) { service.deleteById(id); }
}
