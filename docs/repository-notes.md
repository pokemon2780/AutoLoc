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
PaiementService expose uniquement la lecture ; la création et la suppression des paiements sont pilotées par ContratService.
ContratService propose aussi create, update, delete, addPaiement et removePaiement. La modification des attributs simples ne remplace pas la collection des paiements.

### Exemple Contrat–Paiement : cascade et orphanRemoval

La relation `Contrat.paiements` utilise `@OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)`.
Les méthodes `ajouterPaiement` et `retirerPaiement` synchronisent les deux côtés de l'association (`Paiement.contrat` est le côté propriétaire).

| Opération | Appel | Comportement JPA attendu |
|---|---|---|
| Création du contrat et du paiement | `contrat.ajouterPaiement(paiement); contratRepository.save(contrat);` | Le paiement est enregistré avec le contrat (`PERSIST`). |
| Lecture | `contratRepository.findById(id)` puis `getPaiements()` en transaction | Les paiements sont rattachés au contrat. |
| Mise à jour du contrat | `contratService.update(id, valeurs)` | Les champs simples changent, sans écraser les paiements. |
| Suppression d'un paiement | `contratService.removePaiement(idContrat, idPaiement)` | La suppression de la collection entraîne la suppression de la ligne Paiement (`orphanRemoval`). |
| Suppression d'un contrat | `contratService.delete(idContrat)` | Les paiements encore rattachés sont supprimés avec le contrat (`REMOVE`). |

Le test d'intégration `ContratPaiementJpaTest` couvre ces opérations avec une base H2 en mémoire. Il faut l'exécuter pour confirmer le comportement sur cette version des dépendances : `./mvnw -Dtest=ContratPaiementJpaTest test` (ou `mvnw.cmd` sous Windows).

### 3. Qualité du code et vérification SonarQube

Fichier concerné	Règle SonarQube	Anomalie détectée	Correction effectuée	Résultat après correction
ClientService.java	java:S112	Utilisation d'une exception générique RuntimeException.	Remplacement par une exception personnalisée EntityNotFoundException.	Gestion plus précise des exceptions.
ClientService.java	java:S1128	Présence d'une importation inutilisée.	Suppression de l'importation non utilisée.	Code plus propre et plus lisible.
ContratService.java	java:S106	Utilisation de System.out.println() pour afficher des informations.	Remplacement par un système de journalisation Logger.	Journalisation plus adaptée à une application Spring Boot.

