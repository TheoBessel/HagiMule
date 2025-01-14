# Projet HagiMule

Ce projet a pour but d'implanter une architecture de téléchargement parallèle de grands fichiers.

Il a été réalisé dans le cadre du cours d'intergiciel et données réparties donné en deuxième année à l'N7.

## Installation

Via gradle : TODO

## Architecture globale

Le projet possède trois composants principaux :

#### 1 / `Diary` /

Le `Diary`, ou annuaire, est un serveur accessible par RMI qui répertorie les fichiers des clients connectés ce qui permet l'orchestration des téléchargements.

À chaque connexion d'un client, il enregistre les fichiers proposés par ce dernier.

**1.1/ `UploadService` /**

L'`Upload` est un service tournant en parallèle du Daemon, celui-ci ce charge de récupérer les requêtes de fichiers qu'il reçoit et transfère les fichiers au demandeur via une connection TCP.

**1.3/ Heartbeats : `AliveClientsService` /**

Un mechanisme de *heartbeat* vérifie que les clients sont bien connectés, afin de savoir quel client est toujours disponible avec pour les fichiers, il supprime un owner du diary si celui-ci semble déconnecté.

#### 2 / `Daemon` /

Le `Daemon` tourne sur chaque client et permet le téléchargement de fragments de fichiers via des sockets TCP.

Ce dernier est une sorte de serveur qui tourne sur chaque client et expose des fragments de fichiers à la demande.

**2.1/ Heartbeats : `HeartbeatService` /**

Un mechanisme de *heartbeat* vérifie que le diary est bien connecté, afin de pouvoir continuer à envoyer et télécharger des fichiers. Si le diary n'est plus connecté, alors il tente une reconnection toutes les secondes.

**2.2/ `DirectoryWatcher` /**

Le `DirectoryWatcher` est un service associé au démon tournant en parallèle de celui-ci. Il surveille le dossier */downloads* où sont stockés les fichiers transimissibles et où sont téléchargés les fichiers.

#### 3 / `Download` /

Le `Downloader` tourne sur chaque client et permet à ce client de téléchager de manière parallèle des fichiers proposés par d'autre clients.

Ce dernier communique donc avec les `Daemon` des autres clients (ou du moins leur `UploadService` asssocié).

## Elements annexes

#### 1 / `FileInfo` /

`FileInfo` est notre interface représentant les fichiers pour le diary. Un fichier représenté par FileInfo possède : un nom, une taille et une liste de client (`ClientInfo`) le possédant.

#### 2 / `Fragment` /
De même, pour le téléchargement des fichiers, ceux-ci sont téléchargés par fragments, qui sont alors représentés par un `FileFragment` et possèdent : un nom, une taille, un offset et un client propriétaire.

#### 3 / `ClientInfo` /

Les clients sont eux représentés par un un `ClientInfo` et possède une adresse et un port.
