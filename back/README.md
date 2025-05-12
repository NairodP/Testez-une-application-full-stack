# Yoga App - Backend

Ce projet est une application Spring Boot qui fournit l'API pour l'application Yoga Studio.

## Démarrer le projet

Cloner le dépôt Git :

> git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing

Aller dans le dossier du projet :

> cd Testez-une-application-full-stack/back

Installer les dépendances :

> mvn clean install

Lancer le backend :

> mvn spring-boot:run

## Ressources

### Configuration de base de données

Le projet utilise MySQL comme système de gestion de base de données. Le script SQL pour créer le schéma est disponible dans `ressources/sql/script.sql`.

Par défaut, le compte administrateur est :

- login: yoga@studio.com
- password: test!1234

### API Documentation

L'API est documentée avec Swagger et est accessible à l'adresse :

> http://localhost:8080/api/swagger-ui/index.html

## Tests

### Tests unitaires

Pour exécuter l'ensemble des tests unitaires :

> mvn test

### Tests d'intégration

Pour exécuter les tests d'intégration :

> mvn verify

### Rapport de couverture

Pour générer le rapport de couverture JaCoCo :

> mvn clean test

Le rapport de couverture sera disponible dans :

> back/target/site/jacoco/index.html

# Yoga Studio - Tests Backend

Ce document présente l'ensemble des tests implémentés pour le backend de l'application Yoga Studio. La suite de tests comprend des tests unitaires et d'intégration.

## Table des matières

1. [Tests unitaires](#tests-unitaires)
2. [Tests d'intégration](#tests-dintégration)
3. [Exécution des tests](#exécution-des-tests)
4. [Rapports de couverture](#rapports-de-couverture)

## Tests unitaires

L'application backend contient des tests unitaires qui couvrent les contrôleurs, services, repositories et mappers. Ces tests sont développés avec JUnit et Mockito.

### Liste des tests unitaires

#### Contrôleurs

1. **AuthController** - Tests d'authentification et d'enregistrement
2. **SessionController** - Tests de gestion des sessions
3. **TeacherController** - Tests de gestion des enseignants
4. **UserController** - Tests de gestion des utilisateurs

#### Services

1. **AuthService** - Tests de service d'authentification
2. **SessionService** - Tests de service de gestion des sessions
3. **TeacherService** - Tests de service de gestion des enseignants
4. **UserService** - Tests de service de gestion des utilisateurs

#### Mappers

1. **SessionMapper** - Tests de conversion entre entités et DTOs de sessions
2. **TeacherMapper** - Tests de conversion entre entités et DTOs d'enseignants
3. **UserMapper** - Tests de conversion entre entités et DTOs d'utilisateurs

## Exécution des tests

### Tests unitaires

Pour exécuter l'ensemble des tests unitaires :

```bash
mvn test
```

### Tests d'intégration

Pour exécuter les tests d'intégration :

```bash
mvn verify
```

## Rapports de couverture

Pour générer le rapport de couverture JaCoCo :

```bash
mvn clean test
```

Le rapport de couverture sera disponible dans :
`back/target/site/jacoco/index.html`

---

Cette documentation présente un aperçu détaillé des tests implémentés pour le backend de l'application Yoga Studio.
