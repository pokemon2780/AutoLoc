# Atelier 4 — Inversion de contrôle, injection de dépendances et services AutoLoc

## A. Modes d'injection comparés

| Critère | Injection par constructeur | Injection par attribut `@Autowired` |
|---|---|---|
| Champ `final` | Oui | Non, en pratique |
| Dépendance visible dans le constructeur | Oui | Non |
| Instanciation hors de Spring | Possible avec un repository de test | Risque de dépendance `null` |
| Signalement SonarQube | Mode recommandé | `java:S6813` peut être signalée, à vérifier dans l'IDE |

Le projet garde partout l'injection par constructeur. `@Autowired` n'est pas nécessaire lorsqu'il n'existe qu'un constructeur.

## B. Services et dépendances injectées

| Service (implémentation existante) | Dépendance injectée | Mode | Justification |
|---|---|---|---|
| `AgenceService` | `IAgenceRepository` | Constructeur | Isoler l'accès aux agences |
| `ClientService` | `IClientRepository` | Constructeur | Isoler l'accès aux clients |
| `ContratService` | `IContratRepository` | Constructeur | Centraliser CRUD et gestion des paiements |
| `EmployeService` | `IEmployeRepository` | Constructeur | Isoler la persistance des employés |
| `EquipementService` | `IEquipementRepository` | Constructeur | Encadrer le libellé obligatoire |
| `MaintenanceService` | `IMaintenanceRepository` | Constructeur | Contrôler les dates |
| `PaiementService` | `IPaiementRepository` | Constructeur | Lecture seule ; écritures via contrat |
| `ReservationService` | `IReservationRepository` | Constructeur | Vérifier l'ordre des dates |
| `VehiculeService` | `IVehiculeRepository` | Constructeur | Refuser les tarifs négatifs |

Les neuf classes portent `@Service` et sont détectées sous `tn.esprit.autoloc`. Leurs dépendances `private final` sont injectées par un constructeur explicite (équivalent fonctionnel de `@RequiredArgsConstructor` utilisé dans le support). Aucun repository n'est instancié avec `new`.

Chaque contrôleur du package `web.controller` porte `@RestController` et reçoit **une interface `I…Service`** par constructeur, jamais un repository directement.

## C. Diagnostic des trois erreurs du conteneur

1. **`NoSuchBeanDefinitionException` pour `IContratService` :** l'implémentation manque `@Service` ou se trouve hors des packages scannés. Corriger l'annotation et vérifier le package de la classe.
2. **`NoUniqueBeanDefinitionException` pour les notificateurs :** deux beans satisfont la même interface. Mettre `@Primary` sur l'implémentation par défaut ou `@Qualifier("emailNotificateur")` sur le paramètre du constructeur.
3. **Dépendance circulaire Client ↔ Réservation :** les constructeurs ont besoin l'un de l'autre pour terminer l'instanciation. Extraire la responsabilité partagée dans un troisième service ou réorganiser le flux métier pour rendre les dépendances unidirectionnelles.

## D. Anomalies SonarQube for IDE

Le support demande une expérience temporaire et la restauration du code. Les règles suivantes sont connues, mais **leur détection réelle doit être confirmée par les résultats SonarQube for IDE** ; aucune capture ni rapport n'est fourni.

| Règle | Exemple analysé / anomalie | Correction à conserver | Statut |
|---|---|---|---|
| `java:S112` | Lever `RuntimeException` générique lors d'une recherche | `EntityNotFoundException` spécifique | Correction antérieure confirmée par l'étudiante ; analyse à conserver |
| `java:S6813` | `@Autowired` sur un attribut au lieu d'une injection constructeur | Champ `final` et constructeur | Code actuellement conforme ; déclenchement non vérifié |
| `java:S1128` | Import Java non utilisé | Supprimer l'import inutile | Exemple à vérifier dans l'IDE ; ne pas présenter comme issue détectée |

## E. Questions de compréhension

1. **Qui fait `new ContratService` ?** Le conteneur IoC Spring crée le bean lors du démarrage et résout son repository à partir du contexte ; notre code ne fait pas cet appel.
2. **Pourquoi injecter `IContratService` ?** Le contrôleur dépend d'un contrat abstrait, ce qui permet de substituer une implémentation ou un mock pour les tests.
3. **Pourquoi le singleton reste-t-il sans état ?** Il est partagé entre requêtes concurrentes. Stocker un « contrat courant » dans un champ mutable ferait lire à un utilisateur le contrat d'un autre.
4. **Pourquoi charger avant `save` lors de `update` ?** Pour préserver l'identifiant et les associations, notamment les paiements soumis à `orphanRemoval`, puis ne modifier que les attributs autorisés.
5. **Différences :** `@Component` détecte automatiquement une classe du projet, `@Bean` déclare explicitement un objet via une méthode de configuration. `@Primary` choisit le bean préféré par défaut, `@Qualifier` cible explicitement l'un des beans. `IPaiementService` reste en lecture seule car les écritures appartiennent au cycle de vie du `Contrat`.

## Vérifications à effectuer

1. Vérifier le démarrage Spring Boot avec MySQL et les 9 repositories.
2. Vérifier les routes `/api/agences`, `/api/clients`, `/api/contrats`, `/api/employes`, `/api/equipements`, `/api/maintenances`, `/api/paiements`, `/api/reservations`, `/api/vehicules`.
3. Exécuter SonarQube for IDE sur `service`, `web.controller` et conserver les captures des trois alertes réellement détectées.
4. Les contrôleurs sont ajoutés sur demande, bien que le PDF réserve leur validation REST à l'Atelier 5.
