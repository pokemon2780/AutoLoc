# Atelier 6 — DTO, validation et erreurs centralisées

## Organisation

- `web/dto` : neuf DTO Request (champs modifiables), neuf DTO Response (réponses), ApiErrorResponse.
- `web/mapper` : neuf mappers explicites, sans accès à la base.
- `service/dto` : neuf services d'application transactionnels, plus ReferenceResolver pour charger les relations par identifiant. Ils réutilisent les services métier existants.
- `web/controller` : les neuf contrôleurs exposent uniquement les DTO. DELETE conserve une réponse 204 sans corps.
- `ApiExceptionHandler` : conseil REST commun, basé sur ResponseEntityExceptionHandler pour conserver les codes et en-têtes HTTP de Spring MVC.

`spring-boot-starter-validation` était déjà présent ; aucune dépendance en doublon n'a été ajoutée.
Les URL de l'atelier 5 sont conservées : /agences, /clients, /vehicules, /employes, /equipements, /reservations, /maintenances, /contrats, /paiements.
Les endpoints métier existants sont tous couverts. Swagger expose les schémas des DTO, pas les entités JPA.
Deux endpoints complètent la composition : POST /contrats/{id}/paiements et DELETE /contrats/{id}/paiements/{paiementId}.

## Contrat JSON

Les relations sont représentées par `agenceId`, `clientId`, `vehiculeId`, `reservationId`, `contratId` et `equipementIds`, sans objets JPA imbriqués ni collections inverses récursives.
ContratResponse contient une liste de PaiementResponse sans référence objet vers le contrat.
Les identifiants des ressources sont exclusivement dans les réponses ; les requêtes de création ne permettent pas de les choisir.
Les relations simples restent facultatives, conformément au mapping existant. Si un identifiant est fourni, il doit être positif et exister ; sinon réponse 400 ou 404.

PUT remplace les champs simples et les relations écrites : une relation omise/null est détachée, une liste equipementIds omise est vide. Les collections inverses restent gérées par leurs propres ressources.
Pour un contrat, les paiements initiaux peuvent être fournis à la création. PUT conserve les paiements existants et refuse une liste non vide ; utiliser les endpoints dédiés pour ajouter ou retirer un paiement.
Les mappers s'exécutent avant la fin de la transaction de service ; open-in-view est désactivé.

Exemple POST /vehicules (adapter les identifiants à la base) :

```json
{
  "immatriculation": "TEST-001",
  "marque": "Toyota",
  "modele": "Yaris",
  "categorie": "CITADINE",
  "tarifJournalier": 40.00,
  "statut": "DISPONIBLE",
  "agenceId": 1,
  "equipementIds": [1]
}
```

## Validation

- Textes requis : NotBlank et Size ; immatriculation 20 caractères, marque/modèle 50, permis 50, autres textes 255.
- Email : NotBlank, Email et Size.
- Téléphone : 6 à 30 caractères, chiffres et signes usuels (+, espaces, parenthèses, points, tirets).
- Montants : NotNull, DecimalMin(0), au plus 8 chiffres entiers et 2 décimales.
- Dates et énumérations requises ; dateInscription au plus aujourd'hui.
- Réservation/maintenance : dateFin >= dateDebut, contrôle AssertTrue.
- Listes : au plus 100 éléments, ni null ni identifiants non positifs ; validation imbriquée des paiements avec Valid.
- POST/PUT : Valid sur les DTO ; identifiants d'URL : Positive, validation native Spring MVC.

## Erreurs

Toutes les réponses d'erreur gérées ont les champs : statut, message, horodatage, chemin, erreurs.
Le champ erreurs associe le nom du champ invalide au message de validation, sans renvoyer sa valeur.

| HTTP | Cas |
|---|---|
| 400 | DTO invalide, identifiant invalide, JSON tronqué, enum/date/type invalide |
| 404 | Ressource ou référence inexistante |
| 409 | Unicité ou clé étrangère violée |
| 405 / 415 | Méthode ou Content-Type non pris en charge |
| 500 | Erreur inattendue ; message générique, détails uniquement dans les logs serveur |

## Tests et exécution

Les tests chargent une base H2 éphémère en mode MySQL via src/test/resources/application.properties. Ils ne se connectent pas à la base MySQL de travail.

```powershell
.\mvnw.cmd test
```

Atelier6ApiTest vérifie le CRUD des huit ressources modifiables, la consultation des paiements, les schémas de réponse, la validation, les relations, 400/404/409/405/415 et orphanRemoval via l'API. ApiExceptionHandlerTest vérifie que les erreurs 500 n'exposent pas les détails internes.
Deux anciens tests de suppression ont été corrigés pour simuler existsById et vérifier deleteById, les méthodes effectivement utilisées par les services.
Le doublon historique service.impl.PaiementService a été retiré : PaiementServiceImpl est la seule implémentation de IPaiementService.

## Captures réelles

Le workflow `.github/workflows/atelier6.yml` exécute les tests, démarre une instance H2 et produit l'artefact `atelier6-verification` : rapports, captures PNG et réponses HTTP effectivement reçues.
Le script scripts/capture_atelier6.py ne fabrique aucune réponse. Il échoue si le serveur ou les statuts attendus ne sont pas disponibles.

Pour produire les captures localement, lancer dans un premier terminal :

```powershell
.\mvnw.cmd spring-boot:test-run
```

Puis dans un second terminal :

```powershell
python -m pip install playwright==1.56.0
python -m playwright install chromium
python scripts/capture_atelier6.py
```

Les captures sont écrites dans docs/captures/atelier6 : Swagger, lecture DTO 200, validation 400 et erreur 404. L'instance de démonstration utilise le classpath de test et H2 ; arrêter le processus ensuite.
Pour une exécution normale MySQL, utiliser spring-boot:run et la configuration habituelle.
La collection postman/AutoLoc-Atelier6.postman_collection.json fournit aussi un parcours de validation.

## État de validation

La première tentative locale a échoué avant compilation : résolution DNS de repo.maven.apache.org impossible. Aucun succès de test ni capture d'exécution n'est affirmé sans les résultats du workflow ou d'une exécution locale réussie.
