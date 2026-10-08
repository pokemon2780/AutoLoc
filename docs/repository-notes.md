
# Atelier 3 — Spring Data JPA

## 1. Choix des interfaces Repository

| Interface | Interface étendue | Justification |
|---|---|---|
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD, listes et pagination |
| IClientRepository | JpaRepository<Client, Long> | Gestion des clients |
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, saveAndFlush |
| IEmployeRepository | JpaRepository<Employe, Long> | Gestion des employés |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD des équipements |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | Gestion des maintenances |
| IPaiementRepository | JpaRepository<Paiement, Long> | Consultation des paiements |
| IReservationRepository | JpaRepository<Reservation, Long> | Gestion des réservations |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD, tri et pagination |

## 2. Analyse SonarQube for IDE

| Anomalie détectée | Règle / explication | Correction apportée |
|---|---|---|
| À compléter | À compléter | À compléter |
| À compléter | À compléter | À compléter |
| À compléter | À compléter | À compléter |

## 3. Vérification

Nombre de repositories attendus : 9

Résultat observé :
À compléter après le démarrage de Spring Boot.
