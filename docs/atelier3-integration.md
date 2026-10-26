# Complément Atelier 3 : services et CRUD

Base : dépôt pokemon2780/AutoLoc, commit fc7b185.

## Récupérer les ajouts

Les services et les tests sont intégrés au dépôt. Dans une copie locale sans modifications en attente, exécuter `git pull`, puis recharger Maven dans IntelliJ.
Conserver `spring.jpa.hibernate.ddl-auto=update` et vérifier que MySQL est démarré.

## Contenu

- 9 interfaces I...Service dans tn.esprit.autoloc.service.
- 9 classes ...Service dans tn.esprit.autoloc.service.impl, annotées @Service et utilisant l'injection par constructeur.
- Consultation findAll / findById pour les neuf entités.
- CRUD complet Client et Vehicule : create, findAll, findById, update, delete.
- Une exception EntityNotFoundException.
- 12 tests unitaires Mockito (6 par CRUD).

Pour update, fournir tous les attributs simples de l'entité. L'identifiant cible est le paramètre id ; un identifiant différent dans l'objet fourni n'est pas recopié. Les associations existantes sont conservées.
Pour create, l'identifiant doit être null. Les associations fournies doivent respecter les contraintes JPA et référencer les entités appropriées ; aucun CRUD des associations n'est ajouté ici.
Une suppression peut être refusée par MySQL si d'autres lignes référencent l'entité. Aucune suppression en cascade n'est ajoutée à l'occasion de ce complément.
Les sept autres services sont en lecture seule : le CRUD complet est exigé uniquement pour deux entités dans les consignes transmises.

## Vérification sous Windows

Depuis PowerShell, à la racine du projet :

```powershell
.\mvnw.cmd "-Dtest=ClientServiceTest,VehiculeServiceTest" test
.\mvnw.cmd spring-boot:run
```

Les tests Mockito ne nécessitent pas MySQL. Le démarrage de l'application nécessite MySQL.
Vérifier Found 9 JPA repository interfaces et Started AutolocApiApplication dans les logs.
Les contrôleurs REST et Postman relèvent de l'atelier suivant et ne sont pas ajoutés ici.

## SonarQube

Lancer l'analyse dans IntelliJ et compléter le tableau de docs/repository-notes.md avec trois constats réels. Aucun rapport d'analyse n'est fabriqué.

## État de la validation du complément

La commande Maven des tests a été tentée dans l'environnement de préparation. Elle a échoué avant compilation : résolution DNS de repo.maven.apache.org impossible, donc téléchargement du parent Spring Boot 4.1.1 impossible. Les 12 tests sont fournis mais n'ont pas pu être exécutés ici. La compilation, les tests et la connexion MySQL restent à confirmer localement.
Le pom.xml existant contient aussi une double déclaration de mysql-connector-j : retirer l'un des deux blocs identiques avant le rendu.
