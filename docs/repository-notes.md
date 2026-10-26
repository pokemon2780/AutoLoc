# Atelier 3 — Spring Data JPA
## Note technique : Repository et qualité du code

### 1. Choix des interfaces Repository

Dans le projet AutoLoc, l'interface `JpaRepository` a été retenue pour les neuf entités JPA.

Elle fournit les opérations CRUD, le tri, la pagination et des fonctionnalités complémentaires comme `saveAndFlush()` et `flush()`.

| Interface Repository | Interface étendue | Justification |
|---|---|---|
| IAgenceRepository | `JpaRepository<Agence, Long>` | Gestion CRUD des agences, recherche et pagination. |
| IClientRepository | `JpaRepository<Client, Long>` | Création, consultation, modification et suppression des clients. |
| IContratRepository | `JpaRepository<Contrat, Long>` | Gestion des contrats, CRUD complet et utilisation de saveAndFlush(). |
| IEmployeRepository | `JpaRepository<Employe, Long>` | Gestion des employés et recherche par identifiant. |
| IEquipementRepository | `JpaRepository<Equipement, Long>` | Gestion et consultation des équipements. |
| IMaintenanceRepository | `JpaRepository<Maintenance, Long>` | Enregistrement et suivi des maintenances. |
| IPaiementRepository | `JpaRepository<Paiement, Long>` | Consultation des paiements associés aux contrats. |
| IReservationRepository | `JpaRepository<Reservation, Long>` | Gestion des réservations et consultation des données. |
| IVehiculeRepository | `JpaRepository<Vehicule, Long>` | Gestion CRUD des véhicules, tri et pagination. |

**Justification générale :**

Le choix de `JpaRepository` permet de réduire le code répétitif et de simplifier la couche de persistance. Spring Data JPA génère automatiquement les implémentations des interfaces Repository.

### 2. Couche service

Les neuf interfaces I...Service et les neuf classes ...Service sont dans service et service.impl.
Les repositories sont injectés par constructeur. Toutes les entités disposent de findAll et findById.
Client et Vehicule disposent en plus de create, update et delete : ce sont les deux CRUD complets demandés.
Les lectures utilisent @Transactional(readOnly = true), les écritures @Transactional.
Un identifiant absent déclenche EntityNotFoundException. La création refuse un identifiant déjà renseigné.
La modification charge l'entité existante et conserve son identifiant ainsi que ses associations.
La gestion des associations reste distincte de la modification des attributs simples.
Les suppressions restent soumises aux contraintes de clés étrangères MySQL.
PaiementService expose uniquement la lecture ; la gestion des paiements via Contrat reste à développer lors du travail métier sur la composition.

### 3. Analyse SonarQube for IDE à réaliser dans IntelliJ

Une exécution réelle de SonarQube for IDE n'a pas été réalisée dans cet environnement.
Aucun résultat avant/après n'est affirmé sans rapport d'analyse.
Lancer l'analyse du projet, relever et corriger trois anomalies effectivement signalées, puis compléter :

| Fichier et ligne | Règle SonarQube | Anomalie observée | Correction effectuée | Résultat après analyse |
|---|---|---|---|---|
| À renseigner | À renseigner | À renseigner | À renseigner | À renseigner |
| À renseigner | À renseigner | À renseigner | À renseigner | À renseigner |
| À renseigner | À renseigner | À renseigner | À renseigner | À renseigner |

### 4. Validation locale

Exécuter les tests unitaires ClientServiceTest et VehiculeServiceTest, puis lancer l'application avec MySQL.
Vérifier dans les logs : Found 9 JPA repository interfaces.
Les tests Mockito vérifient la logique des services ; ils ne remplacent pas la vérification du schéma et des contraintes MySQL.
