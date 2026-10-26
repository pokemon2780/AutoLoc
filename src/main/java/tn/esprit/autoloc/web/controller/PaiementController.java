package tn.esprit.autoloc.web.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {
    private final IPaiementService service;

    public PaiementController(IPaiementService service) {
        this.service = service;
    }

    @GetMapping
    public List<Paiement> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Paiement findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
