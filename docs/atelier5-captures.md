# Captures à fournir — Atelier 5

Les captures doivent être prises sur la machine où l'application AutoLoc est réellement démarrée ; elles ne doivent pas être simulées.

## Postman

Importer :
- `postman/AutoLoc-Atelier5.postman_collection.json`
- `postman/AutoLoc-local.postman_environment.json`

Sélectionner l'environnement **AutoLoc local**, puis exécuter et capturer au minimum :

1. GET `/api/agences` — 200
2. POST `/api/agences` — 201
3. GET `/api/agences/999999` — 404
4. POST agence invalide — 400
5. GET `/api/clients` — 200
6. POST `/api/clients` — 201
7. PUT `/api/clients/{{clientId}}` — 200
8. DELETE `/api/clients/{{clientId}}` — 204
9. GET `/api/vehicules` — 200
10. POST `/api/vehicules` — 201
11. POST véhicule avec tarif négatif — 400
12. DELETE `/api/vehicules/{{vehiculeId}}` — 204

Chaque capture doit montrer l'URL, le verbe HTTP, le code de statut et le corps/résultat de test quand il existe.

## Swagger UI

Ouvrir `http://localhost:8080/api/swagger-ui.html` et capturer :
- l'en-tête **AutoLoc API** ;
- les groupes **Agences**, **Clients**, **Vehicules** ;
- un endpoint GET déplié avec son résumé et ses réponses documentées ;
- un POST exécuté avec **Try it out** et le bloc **Curl / Request URL / Server response**.

## OpenAPI JSON

Ouvrir `http://localhost:8080/api/v3/api-docs` et capturer une partie du JSON montrant au moins `/agences`, `/clients` ou `/vehicules`.
