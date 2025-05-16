# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing

Go inside folder:

> cd yoga

Install dependencies:

> npm install

Launch Front-end:

> npm run start;


## Ressources

### Mockoon env 

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


### Test

#### E2E

Launching e2e test:

> npm run e2e

Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report is available here:

> front/coverage/lcov-report/index.html

#### Unitary test

Launching test:

> npm run test

for following change:

> npm run test:watch


# Yoga Studio - Tests Frontend

Ce document présente l'ensemble des tests implémentés pour le frontend de l'application Yoga Studio. La suite de tests comprend à la fois des tests unitaires (avec Jest) et des tests end-to-end (avec Cypress).

## Table des matières

1. [Tests unitaires (Jest)](#tests-unitaires-jest)
2. [Tests End-to-End (Cypress)](#tests-end-to-end-cypress)
3. [Exécution des tests](#exécution-des-tests)
4. [Rapports de couverture](#rapports-de-couverture)

## Tests unitaires et d'intégration (Jest)

L'application contient **13 fichiers** de tests qui couvrent les composants et services Angular. Ces tests sont développés avec Jest et peuvent être identifiés par le suffixe `.spec.ts` dans le code source.

Au moins 30% des tests sont des tests d'intégration (5 fichiers sur 13), dépassant ainsi le seuil requis.

### Tests d'intégration

Les tests d'intégration suivants vérifient les interactions entre plusieurs composants, services et le backend via des requêtes HTTP :

1. **RegisterComponent** (`/src/app/features/auth/components/register/register.component.spec.ts`)
   - Tests d'intégration avec AuthService et HttpClient
   - Vérification des requêtes HTTP réelles et simulation des réponses avec HttpTestingController

2. **LoginComponent** (`/src/app/features/auth/components/login/login.component.spec.ts`)
   - Tests d'intégration avec AuthService, SessionService et HttpClient
   - Vérification des flux complets d'authentification et des redirections

3. **SessionApiService** (`/src/app/features/sessions/services/session-api.service.spec.ts`)
   - Tests d'intégration HTTP pour toutes les opérations CRUD sur les sessions
   - Vérification des formats de requêtes et réponses

4. **UserService** (`/src/app/services/user.service.spec.ts`)
   - Tests d'intégration avec HttpClient pour la gestion des utilisateurs
   - Vérification des requêtes HTTP et des réponses

5. **TeacherService** (`/src/app/services/teacher.service.spec.ts`)
   - Tests d'intégration avec HttpClient pour la récupération des données d'enseignants
   - Vérification des méthodes de requêtes et gestion des erreurs

#### Avantages des tests d'intégration

La proportion significative de tests d'intégration (38% des tests) apporte plusieurs bénéfices au projet :

1. **Confiance accrue** : Les tests d'intégration vérifient que les différents composants du système fonctionnent correctement ensemble, ce qui augmente la fiabilité de l'application.

2. **Détection des problèmes d'interfaces** : Contrairement aux tests unitaires qui testent les composants en isolation, les tests d'intégration permettent de détecter les problèmes qui surviennent lorsque différentes parties du système interagissent.

3. **Test des flux de données** : Les tests d'intégration vérifient les flux de données complets, de l'interface utilisateur jusqu'aux requêtes HTTP, en validant que :
   - Les requêtes HTTP sont correctement formatées
   - Les réponses HTTP sont correctement traitées
   - Les données sont correctement affichées dans l'interface

4. **Couverture des cas réels d'utilisation** : Ces tests simulent des scénarios plus proches de l'utilisation réelle de l'application, comme l'inscription d'un utilisateur suivie de sa connexion.

Les tests d'intégration implémentés sont particulièrement pertinents pour les fonctionnalités critiques de l'application, telles que l'authentification (login/register) et la gestion des sessions de yoga, garantissant ainsi la stabilité des fonctionnalités essentielles.

### Liste exhaustive des tests unitaires et d'intégration

#### Composants

1. **AppComponent** (`/src/app/app.component.spec.ts`)
   - Vérification du rendu du composant principal
   - Test de la navigation
   - Test des changements d'état d'authentification

2. **RegisterComponent** (`/src/app/features/auth/components/register/register.component.spec.ts`)
   - Test de la soumission du formulaire d'inscription
   - Test de la validation des formulaires
   - Test de la gestion des erreurs

3. **LoginComponent** (`/src/app/features/auth/components/login/login.component.spec.ts`)
   - Test de la soumission du formulaire de connexion
   - Test de la redirection après connexion réussie
   - Test de la gestion des erreurs d'authentification

4. **FormComponent (Sessions)** (`/src/app/features/sessions/components/form/form.component.spec.ts`)
   - Test de la création d'une nouvelle session
   - Test de la modification d'une session existante
   - Test de la validation des formulaires de session

5. **DetailComponent (Sessions)** (`/src/app/features/sessions/components/detail/detail.component.spec.ts`)
   - Test de l'affichage des détails d'une session
   - Test des actions disponibles selon le rôle utilisateur
   - Test des fonctionnalités de participation/désinscription

6. **ListComponent (Sessions)** (`/src/app/features/sessions/components/list/list.component.spec.ts`)
   - Test de l'affichage de la liste des sessions
   - Test du filtrage et de l'affichage conditionnel pour les administrateurs

7. **NotFoundComponent** (`/src/app/components/not-found/not-found.component.spec.ts`)
   - Test du rendu de la page 404
   - Test de la navigation depuis la page d'erreur

8. **MeComponent** (`/src/app/components/me/me.component.spec.ts`)
   - Test de l'affichage du profil utilisateur
   - Test de la suppression de compte
   - Test des différentes permissions selon le type d'utilisateur

#### Services

9. **SessionApiService** (`/src/app/features/sessions/services/session-api.service.spec.ts`)
   - Test des requêtes API pour la récupération des sessions
   - Test de la création, modification et suppression de sessions
   - Test de la gestion des erreurs HTTP

10. **SessionService** (`/src/app/services/session.service.spec.ts`)
    - Test de la gestion des états de session
    - Test de l'enregistrement et de la récupération des informations de session

11. **TeacherService** (`/src/app/services/teacher.service.spec.ts`)
    - Test de la récupération des données des enseignants
    - Test de la gestion du cache pour les données d'enseignants

12. **UserService** (`/src/app/services/user.service.spec.ts`)
    - Test des opérations CRUD liées aux utilisateurs
    - Test de la récupération du profil utilisateur
    - Test de la suppression de compte utilisateur

## Tests End-to-End (Cypress)

L'application comprend **7 fichiers** de tests end-to-end qui simulent le comportement d'un utilisateur réel interagissant avec l'application. Ces tests sont développés avec Cypress et peuvent être identifiés par le suffixe `.cy.ts`.

### Liste exhaustive des tests E2E

1. **Login** (`/cypress/e2e/login.cy.ts`)
   - `Login successfull`: Test de connexion réussie avec redirection

2. **Register** (`/cypress/e2e/register.cy.ts`)
   - `Register successfully`: Test d'inscription réussie et redirection
   - `Register with invalid data`: Test de validation des formulaires avec données invalides

3. **Me (Profil utilisateur)** (`/cypress/e2e/me.cy.ts`)
   - `Should display user information correctly`: Test d'affichage des informations utilisateur
   - `Should navigate back when back button is clicked`: Test de navigation retour
   - `Should allow user to delete their account when not admin`: Test de suppression de compte pour utilisateur non-admin

4. **Session List** (`/cypress/e2e/sessions-list.cy.ts` et `/cypress/e2e/session-list.cy.ts`)
   - `Should display sessions list correctly`: Test d'affichage correct de la liste des sessions
   - `Should navigate to session details when clicking on a session`: Test de navigation vers les détails
   - `Should display Create button for admin users only`: Test d'affichage conditionnel pour les administrateurs
   - `Should navigate to create form when clicking Create button`: Test de navigation vers le formulaire de création
   - `Should handle empty sessions list`: Test de gestion des listes vides

5. **Session Detail** (`/cypress/e2e/session-detail.cy.ts`)
   - Tests en tant qu'administrateur:
     - `Should navigate to session details when clicking Detail button and show session information`: Test d'affichage des détails
     - `Should navigate back to sessions list when back button is clicked`: Test de navigation retour
     - `Should delete session and navigate back to sessions list when delete button is clicked`: Test de suppression de session

   - Tests en tant qu'utilisateur normal:
     - `Should navigate to session details and not show delete button`: Test d'affichage sans bouton de suppression
     - `Should handle "Do not participate" action`: Test de désinscription d'une session
     - `Should handle "Participate" action`: Test d'inscription à une session

6. **Session Form** (`/cypress/e2e/session-form.cy.ts`)
   - Tests de création de session:
     - Test de soumission d'un formulaire valide
     - Test de validation des champs obligatoires
     - Test d'annulation de la création

   - Tests de modification de session:
     - Test de chargement des données existantes
     - Test de mise à jour des informations
     - Test d'annulation de la modification

## Exécution des tests

### Tests unitaires

Pour exécuter l'ensemble des tests unitaires :

```bash
npm run test
```

Pour exécuter les tests en mode watch (mise à jour automatique lors des modifications) :

```bash
npm run test:watch
```

### Tests End-to-End

Pour ouvrir l'interface Cypress et exécuter les tests E2E interactivement :

```bash
npm run cypress:open
```

Pour exécuter tous les tests E2E en mode headless (sans interface graphique) :

```bash
npm run e2e
```

## Rapports de couverture

### Répartition des types de tests

La stratégie de test de l'application frontend est basée sur une approche pyramidale avec:

- **Tests unitaires**: 8 fichiers (environ 62%)
- **Tests d'intégration**: 5 fichiers (environ 38%)
- **Tests End-to-End (E2E)**: 6 fichiers (ceux-ci constituent une couche supplémentaire de tests)

Cette répartition dépasse largement l'objectif minimum de 30% de tests d'intégration requis pour le projet.

### Couverture des tests unitaires et d'intégration

Pour générer le rapport de couverture des tests unitaires et d'intégration (Jest) :

```bash
npm run test
```

Le rapport de couverture sera disponible dans :
`front/coverage/jest/lcov-report/index.html`

### Couverture des tests E2E

Pour générer le rapport de couverture des tests E2E :

```bash
npm run e2e
npm run e2e:coverage
```

Le rapport de couverture sera disponible dans :
`front/coverage/lcov-report/index.html`

### Analyse détaillée de la couverture

L'application frontend présente d'excellents résultats de couverture de tests, bien au-delà des exigences du projet.

#### Rapport de couverture des tests E2E (Cypress)

Les tests end-to-end fournissent une couverture globale de **93,42%** pour toutes les déclarations du code, ce qui dépasse largement les attentes. Voici les métriques détaillées :

| Métrique      | Couverture | Nombre       |
|---------------|------------|--------------|
| Déclarations  | **93,42%** | 199/213      |
| Branches      | **90,00%** | 63/70        |
| Fonctions     | **93,81%** | 91/97        |
| Lignes        | **93,01%** | 173/186      |

Cette excellente couverture démontre que la majorité des fonctionnalités de l'application est correctement testée dans des scénarios utilisateur réels. Les tests couvrent les principaux chemins critiques comme l'authentification, la gestion des sessions de yoga, et les interactions utilisateur.

#### Points forts

1. **Composants d'interface utilisateur** : Presque tous les composants atteignent une couverture supérieure à 90%, garantissant que les éléments d'UI fonctionnent comme prévu.

2. **Services** : Les services cruciaux comme `SessionService`, `TeacherService`, et `UserService` sont bien couverts, assurant la fiabilité des opérations de données.

3. **Fonctionnalités critiques** : Les opérations essentielles telles que l'inscription, la connexion, la création et la modification de sessions sont rigoureusement testées.

#### Conclusion

La couverture de tests frontend est exemplaire et répond amplement aux exigences du projet. Les tests garantissent la stabilité des fonctionnalités existantes et faciliteront les futures évolutions en détectant rapidement les régressions potentielles.

---

Cette documentation présente un aperçu détaillé des tests implémentés pour le frontend de l'application Yoga Studio. Elle liste précisément tous les fichiers de tests et les cas de test qui y sont contenus.
