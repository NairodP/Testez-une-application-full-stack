# Application Yoga Studio

Cette application full-stack permet de gérer des sessions de yoga avec un système d'utilisateurs, d'enseignants et de sessions. Ce document présente les instructions essentielles pour installer, lancer et tester l'application.

> **Note importante** : Des README détaillés sont également disponibles dans les dossiers `/back` et `/front` avec des informations complémentaires sur l'architecture et les tests réalisés pour chaque partie de l'application.

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

3. Exécutez le script SQL fourni dans `ressources/sql/script.sql` pour créer le schéma de la base de données :

```bash
mysql -u <votre_utilisateur> -p yoga < ressources/sql/script.sql
```

4. Configurez les informations de connexion à la base de données dans le fichier `.env` du backend :

```
DB_URL=jdbc:mysql://localhost:3306/<le_nom_de_votre_base_de_données>
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

Le backend se lancera sur le port 8080 par défaut.

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
`front/coverage/jest/lcov-report/index.html`

#### Couverture des Tests E2E

Pour générer le rapport de couverture des tests E2E :

```bash
cd front
npm run e2e
npm run e2e:coverage
```

Le rapport de couverture sera disponible dans :
`front/coverage/lcov-report/index.html`

## Documentation complémentaire

Pour des informations détaillées sur l'implémentation et les tests réalisés, consultez les README spécifiques :

- Backend : `/back/README.md` - Détails sur l'architecture Java/Spring Boot et les tests unitaires/d'intégration
- Frontend : `/front/README.md` - Détails sur l'architecture Angular et les tests unitaires/e2e

## Ressources supplémentaires

- Collection Postman : `ressources/postman/yoga.postman_collection.json`