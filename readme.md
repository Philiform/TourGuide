# TourGuide

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

Il s'agit d'une API Rest **DE TEST** développée en Java avec Spring Boot 3.

Cette API permet:
* d'afficher les coordonnées GPS de l'utilisateur. 
* d'afficher la liste des 5 attractions les plus proches de l'utilisateur.
* d'afficher la liste des récompenses si l'utilisateur visite des attractions.
* d'afficher la liste des réductions que proposent les hôtels et des réductions pour différents spectacles

## Pour commencer

Faire un fork du projet ou le cloner.

### Pré-requis

* Java...........: jdk 17.0.7
* Spring Boot: 3.1.1
* Maven........: 3.8.7
* JUnit...........: 5

### Installer les dépendances

_Executez la commande_ ``mvn install:install-file -Dfile=/CHEMIN_DU_PROJET/libs/gpsUtil.jar -DgroupId=gpsUtil -DartifactId=gpsUtil -Dversion=1.0.0 -Dpackaging=jar``  
_Executez la commande_ ``mvn install:install-file -Dfile=/CHEMIN_DU_PROJET/libs/RewardCentral.jar -DgroupId=rewardCentral -DartifactId=rewardCentral -Dversion=1.0.0 -Dpackaging=jar``  
_Executez la commande_ ``mvn install:install-file -Dfile=/CHEMIN_DU_PROJET/libs/TripPricer.jar -DgroupId=tripPricer -DartifactId=tripPricer -Dversion=1.0.0 -Dpackaging=jar``

### Créer un fichier JAR

_Executez la commande_ ``mvn clean package``

## Démarrer l'API

Remplacer "cheminDuProjet" par le chemin du projet sur votre ordinateur.

_Executez la commande_ ``java -jar /cheminDuProjet/target/tourguide-0.0.1-SNAPSHOT.jar``

## Tester l'API

Dans un navigateur internet ou Postman copier et coller les adresses 'http:...' ci-dessous.

_exemples :_
* Page de démarrage: http://localhost:8080/
* Coordonnées GPS de l'utilisateur: http://localhost:8080/getLocation?userName=internalUser1
* Liste des 5 attractions les plus proches de l'utilisateur: http://localhost:8080/getNearbyAttractions?userName=internalUser1
* Liste des récompenses si l'utilisateur visite des attractions: http://localhost:8080/getRewards?userName=internalUser1
* Liste des réductions que proposent les hôtels et des réductions pour différents spectacles: http://localhost:8080/getTripDeals?userName=internalUser1

## Lancer les tests unitaires

_Executez la commande_ ``mvn clean test``

## Intégration Continue avec GitHub Action
![fichier de configuration](./.github/workflows/maven.yml)

## Développé avec

eclipse 4.28.0

## Versions
**Dernière version SNAPSHOT :** 0.0.1

## Auteur

**Philippe PERNET** _alias_ [@Philiform](https://github.com/Philiform)

## License

Ce projet n'est pas sous licence
