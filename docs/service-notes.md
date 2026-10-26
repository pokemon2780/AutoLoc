# Atelier 4 — Inversion de contrôle, injection de dépendances et couche Service

## A. Modes d'injection comparés

| Critère | Injection par constructeur | Injection par attribut `@Autowired` |
|---|---|---|
| Champ `final` | Oui | Non, en pratique |
| Dépendance visible | Oui, dans le constructeur | Non |
| Utilisable hors conteneur | Oui, en fournissant une dépendance | Risque de dépendance `null` |
| SonarQube | Recommandée | `java:S6813` peut être signalée |

Convention retenue dans AutoLoc : **injection par constructeur**, champs `private final`, dépendances typées par interface. Les implémentations utilisent `@RequiredArgsConstructor`, donc Spring utilise le constructeur généré par Lombok sans nécessiter `@Autowired`.

## B. Services

| Service | Dépendance injectée | Mode | Rôle / règle métier |
|---|---|---|---|
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur | CRUD ; nom obligatoire |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur | CRUD ; nom et rôle obligatoires |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur | CRUD ; tarif journalier non négatif |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur | CRUD ; libellé obligatoire |
| `ClientServiceImpl` | `IClientRepository` | Constructeur | CRUD ; nom et e-mail obligatoires |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur | CRUD ; date de fin >= date de début |
| `ContratServiceImpl` | `IContratRepository` | Constructeur | CRUD ; montant total non négatif ; paiements gérés via Contrat |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur | Lecture seule : `findById`, `findAll` |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur | CRUD ; date de fin >= date de début |

Les contrôleurs injectent chacun une interface `I…Service`, jamais un repository directement.

## C. Messages d'erreur du conteneur

1. **`NoSuchBeanDefinitionException` pour `IContratService`** : `@Service` oublié sur `ContratServiceImpl` ou classe placée hors du package scanné.
2. **`NoUniqueBeanDefinitionException`** : plusieurs beans implémentent le même type. Solution : `@Primary` sur l'implémentation par défaut ou `@Qualifier` au point d'injection.
3. **Dépendance circulaire** : deux services s'injectent mutuellement. Avec l'injection constructeur, le cycle empêche la création des beans. Il faut extraire la responsabilité commune ou rendre les dépendances unidirectionnelles.

## D. Anomalies SonarQube for IDE

| Règle | Problème | Correction |
|---|---|---|
| `java:S6813` | Injection par attribut | Injection par constructeur |
| `java:S112` | Exception générique `RuntimeException` | `ResourceNotFoundException` |
| `java:S1128` | Import inutilisé | Supprimer l'import |

Les déclenchements exacts restent à confirmer dans SonarQube for IDE, comme demandé dans le support.

## E. Questions de compréhension

1. **Où est le `new ContratServiceImpl` ?** Il n'est pas écrit dans le code applicatif. Spring crée le bean détecté grâce à `@Service`.
2. **Pourquoi le contrôleur dépend-il de `IContratService` ?** Pour dépendre d'une abstraction et réduire le couplage.
3. **Pourquoi un singleton doit-il rester sans état ?** Une instance est partagée entre plusieurs requêtes. Un champ mutable comme « contrat courant » provoquerait des interférences entre utilisateurs.
4. **Pourquoi charger l'entité existante avant `save` ?** Pour préserver l'identifiant et les associations non modifiées, notamment les collections avec `orphanRemoval`.
5. **`@Component` vs `@Bean`, `@Primary` vs `@Qualifier`, et Paiement ?** `@Component` rend une classe détectable par scan ; `@Bean` déclare explicitement un objet dans une configuration. `@Primary` choisit un candidat par défaut ; `@Qualifier` sélectionne explicitement un bean. `IPaiementService` est en lecture seule parce que les écritures de paiements passent par `Contrat`.

## Vérifications finales

- 9 interfaces `I…Service`.
- 9 classes `…ServiceImpl`.
- `@Service` + `@RequiredArgsConstructor` sur chaque implémentation.
- Repository injecté via un champ `private final`.
- 9 contrôleurs REST injectant les interfaces de service.
- Aucun `new Repository` et aucune injection par attribut.
