# Projet serpents

Ce projet consiste en un jeu de serpents en mulitijoueurs où le dernier survivant gagne.

## Compilation et éxécution

Pour lancer le projet, ouvrir un terminal et se placer dans le répertoire "Snake", puis lancer la commande "./gradlew run".

## Comment jouer 

Après l'éxécution, une fenêtre "Accueil" s'ouvrira, choisissez un des deux modes proposés :

* mode "1 joueur" : le premier joueur contrôle le serpent orange avec les flêches du clavier et joue contre une IA

* mode "2 joueurs" : le premier joueur contrôle le serpent orange avec les flêches du clavier et le deuxième contrôle le serpent violet avec les touches "ZQSD"

Une autre fenêtre s'ouvrira alors et vous pourrez jouer au jeu. Le jeu se termine à la mort d'un des deux serpents annonçant le gagnant et la cause de la mort du perdant dans le terminal.

## Fonctionnalités

* Les serpents bougent automatiquement après un temps fixé dans la direction qui leur est donnée
* Utilisation des touches du clavier pour changer la direction du serpent
* Les serpents s'allongent d'un segment quand ils mangent de la nourriture (les segments rouges)
* Les serpents meurent si il y a collision avec un obstacle (les segments noirs, corps d'un autre serpent) ou auto-collision
* Le terrain est sans bords, quand un serpent sort du terrain, son corps disparait et réapparait progressivement au bord opposé à celui qu'il a emprunté 
* Une IA qui prend la direction de la nourriture la plus proche et qui bouge de maniere intelligente pour ne pas mourir


