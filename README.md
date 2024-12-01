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

#### 2 / `Daemon` /

Le `Daemon` tourne sur chaque client et permet le téléchargement de fragments de fichiers via des sockets TCP.

Ce dernier est une sorte de serveur qui tourne sur chaque client et expose des fragments de fichiers à la demande.

#### 3 / `Download` /

Le `Download` tourne sur chaque client et permet à ce client de téléchager de manière parallèle des fichiers proposés par d'autre clients.

Ce dernier communique donc avec les `Daemon` des autres clients.