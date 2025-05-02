# Application Yoga Studio

Cette application full-stack permet de gérer des sessions de yoga avec un système d'utilisateurs, d'enseignants et de sessions.

## Table des matières

1. [Prérequis](#prérequis)
2. [Installation de la base de données](#installation-de-la-base-de-données)
3. [Installation de l'application](#installation-de-lapplication)
   - [Backend (Java/Spring Boot)](#backend)
   - [Frontend (Angular)](#frontend)
4. [Utilisation de l'application](#utilisation-de-lapplication)
5. [Tests](#tests)
   - [Tests Backend](#tests-backend)
   - [Tests Frontend](#tests-frontend)
6. [Rapports de couverture](#rapports-de-couverture)
   - [Couverture Backend](#couverture-backend)
   - [Couverture Frontend](#couverture-frontend)

## Prérequis

- Java 11+
- Maven
- Node.js (v14+) et npm
- MySQL
- Git

## Installation de la base de données

1. Assurez-vous que MySQL est installé et en cours d'exécution sur votre système
2. Créez une base de données pour l'application :

```sql
CREATE DATABASE yoga;
USE yoga;
```

3. Exécutez le script SQL fourni pour créer le schéma de la base de données :

```bash
mysql -u <votre_utilisateur> -p yoga < ressources/sql/script.sql
```

4. Configurez les informations de connexion à la base de données dans le fichier `.env` du backend :

```
DB_URL=jdbc:mysql://localhost:3306/yoga
DB_USERNAME=<votre_utilisateur>
DB_PASSWORD=<votre_mot_de_passe>
```

**Note** : Par défaut, le compte administrateur est :
- Login : yoga@studio.com
- Mot de passe : test!1234

## Installation de l'application

### Backend

1. Accédez au répertoire backend :

```bash
cd back
```

2. Installez les dépendances avec Maven :

```bash
mvn clean install
```

3. Lancez l'application Spring Boot :

```bash
mvn spring-boot:run
```

Le backend sera accessible à l'adresse : http://localhost:8080/api/

### Frontend

1. Accédez au répertoire frontend :

```bash
cd front
```

2. Installez les dépendances :

```bash
npm install
```

3. Lancez l'application Angular :

```bash
npm run start
```

L'application sera accessible à l'adresse : http://localhost:4200

## Utilisation de l'application

1. Accédez à l'application via votre navigateur à l'adresse : http://localhost:4200
2. Connectez-vous avec le compte administrateur ou créez un nouveau compte
3. Vous pourrez alors :
   - Voir la liste des sessions de yoga disponibles
   - Participer à des sessions
   - Gérer les sessions (créer, modifier, supprimer) si vous avez les droits administrateur

## Tests

### Tests Backend

Pour exécuter les tests unitaires et d'intégration du backend :

```bash
cd back
mvn clean test
```

### Tests Frontend

#### Tests Unitaires

Pour exécuter les tests unitaires du frontend :

```bash
cd front
npm run test
```

Pour exécuter les tests en mode watch (mise à jour automatique lors des modifications) :

```bash
cd front
npm run test:watch
```

#### Tests E2E (End-to-End)

Pour exécuter les tests end-to-end avec Cypress :

```bash
cd front
npm run e2e
```

## Rapports de couverture

### Couverture Backend

Pour générer le rapport de couverture JaCoCo pour le backend :

```bash
cd back
mvn clean test
```

Le rapport de couverture sera généré dans le répertoire :
`back/target/site/jacoco/index.html`

### Couverture Frontend

#### Couverture des Tests Unitaires

Pour générer le rapport de couverture des tests unitaires :

```bash
cd front
npm run test
```

Le rapport de couverture sera disponible dans :
`front/coverage/lcov-report/index.html`

#### Couverture des Tests E2E

Pour générer le rapport de couverture des tests E2E (exécuter d'abord les tests E2E) :

```bash
cd front
npm run e2e
npm run e2e:coverage
```

Le rapport de couverture sera disponible dans :
`front/coverage/lcov-report/index.html`

## Ressources supplémentaires

- Collection Postman : `ressources/postman/yoga.postman_collection.json`
  - Pour l'importer, suivez la documentation : [Importer des données dans Postman](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman)