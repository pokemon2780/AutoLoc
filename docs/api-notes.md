# Atelier 5 — Spring MVC & SpringDoc

## A. Endpoints réalisés

| Contrôleur | Verbe | Route complète | Service appelé | Succès | Erreurs documentées |
|---|---|---|---|---|---|
| AgenceController | GET | /api/agences | findAll() | 200 | — |
| AgenceController | GET | /api/agences/{id} | findById(id) | 200 | 404 |
| AgenceController | POST | /api/agences | create(agence) | 201 | 400 |
| AgenceController | PUT | /api/agences/{id} | update(id, agence) | 200 | 400, 404 |
| AgenceController | DELETE | /api/agences/{id} | deleteById(id) | 204 | 404 |
| ClientController | GET | /api/clients | findAll() | 200 | — |
| ClientController | GET | /api/clients/{id} | findById(id) | 200 | 404 |
| ClientController | POST | /api/clients | create(client) | 201 | 400 |
| ClientController | PUT | /api/clients/{id} | update(id, client) | 200 | 400, 404 |
| ClientController | DELETE | /api/clients/{id} | deleteById(id) | 204 | 404 |
| VehiculeController | GET | /api/vehicules | findAll() | 200 | — |
| VehiculeController | GET | /api/vehicules/{id} | findById(id) | 200 | 404 |
| VehiculeController | POST | /api/vehicules | create(vehicule) | 201 | 400 |
| VehiculeController | PUT | /api/vehicules/{id} | update(id, vehicule) | 200 | 400, 404 |
| VehiculeController | DELETE | /api/vehicules/{id} | deleteById(id) | 204 | 404 |

Le préfixe `/api` est défini une seule fois avec `server.servlet.context-path=/api`. Les contrôleurs utilisent donc `@RequestMapping("/agences")`, `@RequestMapping("/clients")` et `@RequestMapping("/vehicules")`.

## B. Annotations Jackson

Le projet contient plusieurs associations bidirectionnelles. Pour les associations qui ne doivent pas être exposées dans les deux sens, `@JsonIgnore` évite une récursion de sérialisation.

Pour une vraie composition parent/enfant comme Contrat → Paiement, le support recommande une paire `@JsonManagedReference` / `@JsonBackReference`. Cette partie doit être validée lors des scénarios Postman Contrat/Paiement afin de ne pas masquer une propriété nécessaire en entrée.

## C. Postman et Swagger

Postman sert à tester les requêtes rejouables et les codes HTTP. La collection `postman/AutoLoc-Atelier5.postman_collection.json` contient les CRUD d'Agence, Client et Vehicule, ainsi que des scénarios 400 et 404.

Swagger UI sert à explorer la documentation générée depuis le code. Avec le context path du projet :
- Swagger UI : `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON : `http://localhost:8080/api/v3/api-docs`

Les trois contrôleurs sont enrichis avec `@Tag`, `@Operation`, `@ApiResponse(s)` et `@Parameter`.

## D. Anomalies / points SonarQube à vérifier dans IntelliJ

| Point | Explication | Correction retenue |
|---|---|---|
| Injection par attribut | Dépendance cachée et champ non final | Injection constructeur via `@RequiredArgsConstructor` |
| Imports inutiles | Dégradent la lisibilité | Imports limités aux annotations réellement utilisées |
| Couplage aux entités JPA | Les contrôleurs exposent directement les entités | Accepté provisoirement par l'atelier ; DTO au chapitre suivant |

Les numéros de règles doivent être relevés dans SonarQube for IDE sur la machine locale.

## E. Questions de compréhension

1. Toutes les requêtes HTTP arrivent d'abord au `DispatcherServlet`. Spring MVC utilise ses mappings pour retrouver la méthode du contrôleur associée au verbe HTTP et au chemin.
2. Un contrôleur dépend de l'interface `I…Service` afin de rester découplé du repository et de l'implémentation concrète.
3. POST retourne 201 car une ressource est créée. DELETE retourne 204 car la suppression réussit sans corps. Une ressource absente produit 404 car `ResourceNotFoundException` est interceptée par le gestionnaire global.
4. `PaiementController` est en lecture seule car les paiements sont gérés par le cycle de vie de Contrat. Un POST sur `/api/paiements` doit donc recevoir 405 Method Not Allowed.
5. Exposer directement les entités JPA peut révéler des champs internes, provoquer des boucles JSON et coupler l'API au modèle de persistance. Les DTO réduiront ces risques.


## F. Vérification finale Atelier 5

- Les neuf contrôleurs REST sont présents sous `web.controller`.
- Tous les contrôleurs sont documentés avec `@Tag`, `@Operation` et `@ApiResponse(s)`.
- Les dépendances vers la couche métier utilisent uniquement les interfaces `I...Service`.
- `PaiementController` reste en lecture seule.
- `Contrat.paiements` utilise `@JsonManagedReference("contrat-paiements")` et `Paiement.contrat` utilise `@JsonBackReference("contrat-paiements")`.
- Le gestionnaire global produit les champs `statut`, `message` et `horodatage` pour les erreurs 400/404.
- La collection Postman couvre désormais les neuf contrôleurs, y compris le scénario Contrat/Paiement et le POST Paiement attendu à 405.
