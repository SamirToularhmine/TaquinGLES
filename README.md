# Rapport de travail sur le projet : Taquin

Auteurs : NEONAKIS Ionas et TOULARHMINE Samir

--- 
# Guide d'utilisation :

Afin de lancer une partie, il suffit de démarrer l'application. Il est alors proposé à l'utilisateur de sélectionner une taille pour son plateau.
3 tailles sont actuellement disponibles : 
- 3x3
- 4x4
- 5x5

Il est ensuite proposé à l'utilisateur de choisir son niveau de difficulté. Celui-ci correspond à la limite de temps imposée pour résoudre le taquin. Il y a trois difficultés proposées :
- Facile : temps illimité pour résoudre le taquin
- Intermédiaire : 5 minutes pour résoudre le taquin
- Difficile : 1 minute pour résoudre le taquin

Enfin, il est proposé à l'utilisateur de choisir la texture de son plateau. Une fois de plus, trois choix sont proposés : 
- Défaut : une texture de plateau boisé
- Vert : une texture de plateau moquetté de couleur vert
- Covid-19 : une texture rappelant qu'il est important de garder le sourire pendant cette période :-)

Il est alors possible pour l'utilisateur de démarrer la partie.

Une fois la partie démarrée, l'utilisateur peut observer une interface se découpant en 2 parties principales.

La partie supérieure contient un bouton "Solution" permettant d'afficher dans une popup la représentation du taquin résolu. C'est sur cette représentation que l'utilisateur doit se baser pour résoudre son taquin.

Il y a ensuite un chronomètre affichant le temps restant ou le temps écoulé en fonction du mode de difficulté choisi.

En dessous de ce chronomètre se trouve un compteur de coups.

Enfin, on y trouve un bouton "Fermer" mettant fin à la partie en cours et revenant ainsi au menu.

La vue principale contient le plateau de jeu. 

Pour déplacer une pièce, il suffit de cliquer sur la case correspondante. Si la case est adjacente à la case vide, alors la pièce est déplacée. Dans le cas contraire, la pièce clignotera en blanc 3 fois en faisant vibrer le téléphone de l'utilisateur pour lui indiquer que le déplacement est impossible.

Pour déplacer une pièce, il est également possible de laisser son doigt enfoncé sur la case vide et le glisser dans la direction voulue.

Quand le taquin est résolu, les pièces du plateau clignotent en blanc 3 fois et une popup apparait indiquant les résultats de l'utilisateur. Il lui est alors proposé de rejouer sur la même configuration ou alors de revenir au menu. Également, un son de victoire est joué.

Si le temps est écoulé, une popup similaire s'affiche proposant les mêmes actions. Cette fois, un son de défaite est joué.
