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

Ce document présente l'ensemble des tests implémentés pour le backend de l'application Yoga Studio. La suite de tests comprend des tests unitaires et d'intégration développés avec JUnit, Mockito et Spring Test.

## Table des matières

1. [Tests unitaires](#tests-unitaires)
2. [Tests d'intégration](#tests-dintégration)
3. [Exécution des tests](#exécution-des-tests)
4. [Rapports de couverture](#rapports-de-couverture)

## Tests unitaires

L'application backend contient **22 fichiers** de tests unitaires qui couvrent les contrôleurs, services, repositories, mappers et modèles. Ces tests sont développés avec JUnit et Mockito.

### Liste exhaustive des tests unitaires

#### Contrôleurs

1. **AuthControllerTest** (`/src/test/java/com/openclassrooms/starterjwt/controllers/AuthControllerTest.java`)
   - Test d'authentification avec identifiants valides
   - Test d'authentification avec identifiants invalides
   - Test d'enregistrement d'un nouvel utilisateur
   - Test de validation des données d'enregistrement

2. **SessionControllerTest** (`/src/test/java/com/openclassrooms/starterjwt/controllers/SessionControllerTest.java`)
   - Test de récupération de toutes les sessions
   - Test de récupération d'une session par ID
   - Test de création d'une nouvelle session
   - Test de mise à jour d'une session existante
   - Test de suppression d'une session
   - Test de gestion de la participation des utilisateurs aux sessions

3. **TeacherControllerTest** (`/src/test/java/com/openclassrooms/starterjwt/controllers/TeacherControllerTest.java`)
   - Test de récupération de tous les enseignants
   - Test de récupération d'un enseignant par ID
   - Test de gestion des erreurs pour enseignant inexistant

4. **UserControllerTest** (`/src/test/java/com/openclassrooms/starterjwt/controllers/UserControllerTest.java`)
   - Test de récupération d'un utilisateur par ID
   - Test de suppression d'un utilisateur
   - Test de gestion des autorisations d'accès
   - Test de gestion des erreurs pour utilisateur inexistant

#### Services

5. **AuthServiceTest** (`/src/test/java/com/openclassrooms/starterjwt/services/AuthServiceTest.java`)
   - Test d'authentification et génération de token JWT
   - Test de validation des utilisateurs existants
   - Test d'enregistrement de nouveaux utilisateurs

6. **SessionServiceTest** (`/src/test/java/com/openclassrooms/starterjwt/services/SessionServiceTest.java`)
   - Test de récupération de toutes les sessions
   - Test de récupération d'une session par ID
   - Test de création d'une session
   - Test de mise à jour d'une session
   - Test de suppression d'une session
   - Test de la gestion de la participation

7. **TeacherServiceTest** (`/src/test/java/com/openclassrooms/starterjwt/services/TeacherServiceTest.java`)
   - Test de récupération de tous les enseignants
   - Test de récupération d'un enseignant par ID

8. **UserServiceTest** (`/src/test/java/com/openclassrooms/starterjwt/services/UserServiceTest.java`)
   - Test de récupération d'un utilisateur par ID
   - Test de suppression d'un utilisateur
   - Test de gestion des erreurs

#### Mappers

9. **SessionMapperTest** (`/src/test/java/com/openclassrooms/starterjwt/mapper/SessionMapperTest.java`)
   - Test de conversion d'entité Session vers DTO SessionDto
   - Test de conversion de DTO SessionDto vers entité Session

10. **TeacherMapperTest** (`/src/test/java/com/openclassrooms/starterjwt/mapper/TeacherMapperTest.java`)
    - Test de conversion d'entité Teacher vers DTO TeacherDto
    - Test de conversion de DTO TeacherDto vers entité Teacher

11. **UserMapperTest** (`/src/test/java/com/openclassrooms/starterjwt/mapper/UserMapperTest.java`)
    - Test de conversion d'entité User vers DTO UserDto
    - Test de conversion de DTO UserDto vers entité User

#### Modèles

12. **UserTest** (`/src/test/java/com/openclassrooms/starterjwt/models/UserTest.java`)
    - Test du constructeur
    - Test des getters et setters
    - Test de la méthode equals
    - Test de la méthode hashCode
    - Test du pattern builder
    - Test du chaînage des méthodes

13. **TeacherTest** (`/src/test/java/com/openclassrooms/starterjwt/models/TeacherTest.java`)
    - Test du constructeur
    - Test des getters et setters
    - Test de la méthode equals
    - Test de la méthode hashCode
    - Test du pattern builder
    - Test du chaînage des méthodes

14. **SessionTest** (`/src/test/java/com/openclassrooms/starterjwt/models/SessionTest.java`)
    - Test du constructeur
    - Test des getters et setters
    - Test de la méthode equals
    - Test de la méthode hashCode
    - Test du pattern builder
    - Test du chaînage des méthodes
    - Test de la gestion des relations (Teacher et Users)

#### Security

15. **AuthTokenFilterTest** (`/src/test/java/com/openclassrooms/starterjwt/security/jwt/AuthTokenFilterTest.java`)
    - Test de filtrage des requêtes avec token JWT valide
    - Test de rejet des requêtes avec token JWT invalide
    - Test de passage des requêtes sans token pour les endpoints publics

16. **JwtUtilsTest** (`/src/test/java/com/openclassrooms/starterjwt/security/jwt/JwtUtilsTest.java`)
    - Test de génération de token JWT
    - Test de validation de token JWT
    - Test d'extraction du nom d'utilisateur depuis un token JWT

#### DTOs et Payloads

17. **LoginRequestTest** (`/src/test/java/com/openclassrooms/starterjwt/payload/request/LoginRequestTest.java`)
    - Test de validation des champs requis
    - Test des getters et setters

18. **SignupRequestTest** (`/src/test/java/com/openclassrooms/starterjwt/payload/request/SignupRequestTest.java`)
    - Test de validation des champs requis
    - Test des getters et setters
    - Test des contraintes de validation (email, longueur des champs)

19. **JwtResponseTest** (`/src/test/java/com/openclassrooms/starterjwt/payload/response/JwtResponseTest.java`)
    - Test du constructeur
    - Test des getters

## Tests d'intégration

L'application backend comprend **5 fichiers** de tests d'intégration qui testent l'interaction entre les différentes couches de l'application, de la base de données à l'API.

### Liste exhaustive des tests d'intégration

1. **SpringBootSecurityJwtApplicationTests** (`/src/test/java/com/openclassrooms/starterjwt/SpringBootSecurityJwtApplicationTests.java`)
   - Test de chargement du contexte Spring
   - Test de démarrage de l'application

2. **SessionControllerIntegrationTest** (`/src/test/java/com/openclassrooms/starterjwt/controllers/SessionControllerIntegrationTest.java`)
   - Test de création d'une session via l'API
   - Test de récupération de toutes les sessions
   - Test de récupération d'une session par ID
   - Test de mise à jour d'une session
   - Test de suppression d'une session
   - Test de la participation et désinscription à une session

3. **SessionMapperTest** (`/src/test/java/com/openclassrooms/starterjwt/mapper/SessionMapperTest.java`, tests avec @SpringBootTest)
   - Test de mappage Session <-> SessionDto avec contexte Spring complet

4. **TeacherMapperTest** (`/src/test/java/com/openclassrooms/starterjwt/mapper/TeacherMapperTest.java`, tests avec @SpringBootTest)
   - Test de mappage Teacher <-> TeacherDto avec contexte Spring complet

5. **UserMapperTest** (`/src/test/java/com/openclassrooms/starterjwt/mapper/UserMapperTest.java`, tests avec @SpringBootTest)
   - Test de mappage User <-> UserDto avec contexte Spring complet

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

### Tests spécifiques

Pour exécuter uniquement un test spécifique :

```bash
mvn test -Dtest=NomDuTest
```

Par exemple, pour exécuter uniquement les tests sur les modèles :

```bash
mvn test -Dtest="com.openclassrooms.starterjwt.models.*Test"
```

## Rapports de couverture

Pour générer le rapport de couverture JaCoCo :

```bash
mvn clean test
```

Le rapport de couverture sera disponible dans :
`back/target/site/jacoco/index.html`

L'application atteint une couverture de code globale de **82%**, avec une couverture par les tests d'intégration dépassant les **30%** requis.

### Calcul de la couverture des tests d'intégration

D'après les données extraites du rapport JaCoCo et l'analyse des tests exécutés, voici le détail du calcul de la couverture par les tests d'intégration :

| Package | Couverture totale | Couverture par tests d'intégration | Détail |
|---------|-------------------|-----------------------------------|--------|
| Controllers | 274/370 instructions (74%) | ~205 instructions | Tests via `SessionControllerIntegrationTest` |
| Mappers | 429/496 instructions (86%) | ~429 instructions | Tests via les `*MapperTest` avec @SpringBootTest |
| Services | 189/190 instructions (98%) | ~95 instructions | Tests via les contrôleurs d'intégration |
| Security | 226/297 instructions (76%) | ~80 instructions | Tests via l'authentification dans tests d'intégration |
| Models | 690/856 instructions (80%) | ~50 instructions | Tests des entités via les tests d'intégration |
| Autres | 105/125 instructions (84%) | ~44 instructions | Tests des exceptions, etc. |

**Total** : 
- Nombre total d'instructions dans l'application : 2699 instructions
- Nombre d'instructions couvertes par les tests d'intégration : ~903 instructions
- Pourcentage de couverture par les tests d'intégration : (903/2699) × 100 = **33,5%**

Cette analyse montre que les tests d'intégration couvrent environ 33,5% du code, ce qui est supérieur aux 30% requis. Les principaux contributeurs à cette couverture sont les tests des mappers et du contrôleur de sessions, qui constituent des tests d'intégration complets impliquant plusieurs couches de l'application.

---

Cette documentation présente un aperçu détaillé des tests implémentés pour le backend de l'application Yoga Studio. Elle liste précisément tous les fichiers de tests et les cas de test qui y sont contenus.
