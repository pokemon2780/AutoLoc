package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.web.dto.ReservationRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import tn.esprit.autoloc.service.dto.ReservationDtoService;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestion des réservations")
public class ReservationController {

    private final ReservationDtoService service;

    @Operation(summary = "Lister les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations retournée")
    @GetMapping
    public List<ReservationResponse> findAll() { return service.findAll(); }

    @Operation(summary = "Consulter une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @GetMapping("/{id}")
    public ReservationResponse findById(@PathVariable @Positive Long id) { return service.findById(id); }

    @Operation(summary = "Créer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest entity) { return service.create(entity); }

    @Operation(summary = "Modifier une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation modifiée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @PutMapping("/{id}")
    public ReservationResponse update(@PathVariable @Positive Long id, @Valid @RequestBody ReservationRequest entity) {
        return service.update(id, entity);
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) { service.deleteById(id); }
}
