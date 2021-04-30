package fr.univ.taquingles.taquin;


import android.util.Log;

/**
 * Classe pour la gestion du taquin
 */
public class Taquin {
    private Objet[][] tableau; // est le tableau du taquin
    private int xVide;
    private int yVide;


    public Taquin(int taille) {
        if (taille==3)
            this.initialiser33();
        else if (taille == 4)
            this.initialiser44();
        else
            this.initialiser55();
    }

    /**
     * Méthode qui crée un taquin soluble 3 * 3
     */
    private void initialiser33(){
        this.tableau = new Objet[3][3];
        tableau[0][0] = new Objet(0, 0, Couleur.ROUGE, Forme.CARRE);
        tableau[1][0] = new Objet(1, 0, Couleur.VERT, Forme.CARRE);
        tableau[2][0] = new Objet(2, 0, Couleur.JAUNE, Forme.CARRE);

        tableau[0][1] = new Objet(0, 1, Couleur.ROUGE, Forme.TRIANGLE);
        tableau[1][1] = new Objet(1, 1, Couleur.VERT, Forme.TRIANGLE);
        tableau[2][1] = new Objet(2, 1, Couleur.JAUNE, Forme.TRIANGLE);

        tableau[0][2] = new Objet(0, 2, Couleur.ROUGE, Forme.TRIANGLE);
        tableau[1][2] = new Objet(1, 2, Couleur.VERT, Forme.TRIANGLE);
        tableau[2][2] = null;
        xVide = 2;
        yVide = 2;
    }

    /**
     * Méthode qui crée un taquin soluble 4 * 4
     */
    private void initialiser44(){
        this.tableau = new Objet[4][4];
        tableau[0][0] = new Objet(0, 0, Couleur.ROUGE, Forme.CARRE);
        tableau[1][0] = new Objet(1, 0, Couleur.VERT, Forme.CARRE);
        tableau[2][0] = new Objet(2, 0, Couleur.JAUNE, Forme.CARRE);
        tableau[3][0] = new Objet(3, 0, Couleur.BLEU, Forme.CARRE);

        tableau[0][1] = new Objet(0, 1, Couleur.ROUGE, Forme.TRIANGLE);
        tableau[1][1] = new Objet(1, 1, Couleur.VERT, Forme.TRIANGLE);
        tableau[2][1] = new Objet(2, 1, Couleur.JAUNE, Forme.TRIANGLE);
        tableau[3][1] = new Objet(3, 1, Couleur.BLEU, Forme.TRIANGLE);

        tableau[0][2] = new Objet(0, 2, Couleur.ROUGE, Forme.ETOILE);
        tableau[1][2] = new Objet(1, 2, Couleur.VERT, Forme.ETOILE);
        tableau[2][2] = new Objet(2, 2, Couleur.JAUNE, Forme.ETOILE);
        tableau[3][2] = new Objet(3, 2, Couleur.BLEU, Forme.ETOILE);

        tableau[0][3] = new Objet(0, 3, Couleur.ROUGE, Forme.LOSANGE);
        tableau[1][3] = new Objet(1, 3, Couleur.VERT, Forme.LOSANGE);
        tableau[2][3] = new Objet(2, 3, Couleur.JAUNE, Forme.LOSANGE);
        tableau[3][3] = null;
        xVide = 3;
        yVide = 3;
    }

    /**
     * Méthode qui crée un taquin soluble 5 * 5
     */
    private void initialiser55(){
        this.tableau = new Objet[5][5];
        tableau[0][0] = new Objet(0, 0, Couleur.ROUGE, Forme.CARRE);
        tableau[1][0] = new Objet(1, 0, Couleur.VERT, Forme.CARRE);
        tableau[2][0] = new Objet(2, 0, Couleur.JAUNE, Forme.CARRE);
        tableau[3][0] = new Objet(3, 0, Couleur.BLEU, Forme.CARRE);
        tableau[4][0] = new Objet(4, 0, Couleur.ROSE, Forme.CARRE);

        tableau[0][1] = new Objet(0, 1, Couleur.ROUGE, Forme.TRIANGLE);
        tableau[1][1] = new Objet(1, 1, Couleur.VERT, Forme.TRIANGLE);
        tableau[2][1] = new Objet(2, 1, Couleur.JAUNE, Forme.TRIANGLE);
        tableau[3][1] = new Objet(3, 1, Couleur.BLEU, Forme.TRIANGLE);
        tableau[4][1] = new Objet(4, 1, Couleur.ROSE, Forme.TRIANGLE);

        tableau[0][2] = new Objet(0, 2, Couleur.ROUGE, Forme.ETOILE);
        tableau[1][2] = new Objet(1, 2, Couleur.VERT, Forme.ETOILE);
        tableau[2][2] = new Objet(2, 2, Couleur.JAUNE, Forme.ETOILE);
        tableau[3][2] = new Objet(3, 2, Couleur.BLEU, Forme.ETOILE);
        tableau[4][2] = new Objet(4, 2, Couleur.ROSE, Forme.ETOILE);

        tableau[0][3] = new Objet(0, 3, Couleur.ROUGE, Forme.LOSANGE);
        tableau[1][3] = new Objet(1, 3, Couleur.VERT, Forme.LOSANGE);
        tableau[2][3] = new Objet(2, 3, Couleur.JAUNE, Forme.LOSANGE);
        tableau[3][3] = new Objet(3, 3, Couleur.BLEU, Forme.LOSANGE);
        tableau[4][3] = new Objet(4, 3, Couleur.ROSE, Forme.LOSANGE);

        tableau[0][4] = new Objet(0, 4, Couleur.ROUGE, Forme.PENTAGONE);
        tableau[1][4] = new Objet(1, 4, Couleur.VERT, Forme.PENTAGONE);
        tableau[2][4] = new Objet(2, 4, Couleur.JAUNE, Forme.PENTAGONE);
        tableau[3][4] = new Objet(3, 4, Couleur.BLEU, Forme.PENTAGONE);
        tableau[4][4] = null;

        xVide = 4;
        yVide = 4;
    }

    /**
     * Méthode qui effectue 1000 déplacements possibles afin de mélanger le taquin
     */
    public void initailShuffle(){
        int n;
        for (int i = 0; i < 1000; i++) {
            n = (int) (Math.random() * 4);
            switch (n){
                case 0:
                    bougerVideHaut();
                    break;
                case 1:
                    bougerVideBas();
                    break;
                case 2:
                    bougerVideGauche();
                    break;
                default:
                    bougerVideDroite();
                    break;
            }
        }
    }

    /**
     * Test si le tableau actuel est fini
     * @return true le taquin est finit
     */
    public boolean isFinished(){
        for (int i = 0; i < tableau.length - 1; i++) {
            for (int j = 0; j < tableau[i].length - 1; j++) {
                if (this.tableau[i][j] != null && !this.tableau[i][j].isPlaceCorrecte())
                    return false;
            }
        }
        return true;
    }

    /**
     * Déplacement du vide vers le haut
     * @return false si le déplacement n'est pas possible
     */
    public boolean bougerVideHaut(){
        if (this.xVide == 0){
            return false;
        }
        Objet dessus = tableau[xVide - 1][yVide];
        dessus.setX(dessus.getX() + 1);
        xVide--;
        tableau[dessus.getX()][dessus.getY()] = dessus;
        tableau[xVide][yVide] = null;
        return true;
    }

    /**
     * Déplacement du vide vers le bas
     * @return false si le déplacement n'est pas possible
     */
    public boolean bougerVideBas(){
        if (xVide == tableau.length - 1){
            return false;
        }
        Objet dessous = tableau[xVide + 1][yVide];
        dessous.setX(dessous.getX() - 1);
        xVide++;
        tableau[dessous.getX()][dessous.getY()] = dessous;
        tableau[xVide][yVide] = null;
        return true;
    }

    /**
     * Déplacement du vide vers la droite
     * @return false si le déplacement n'est pas possible
     */
    public boolean bougerVideDroite(){
        if (yVide == tableau[0].length - 1){
            return false;
        }
        Objet droite = tableau[xVide][yVide + 1];
        droite.setY(droite.getY() - 1);
        yVide++;
        tableau[droite.getX()][droite.getY()] = droite;
        tableau[xVide][yVide] = null;
        return true;
    }

    /**
     * Déplacement du vide vers la gauche
     * @return false si le déplacement n'est pas possible
     */
    public boolean bougerVideGauche(){
        if (yVide == 0){
            return false;
        }
        Objet gauche = tableau[xVide][yVide - 1];
        gauche.setY(gauche.getY() + 1);
        yVide--;
        tableau[gauche.getX()][gauche.getY()] = gauche;
        tableau[xVide][yVide] = null;
        return true;
    }

    public Objet[][] getTableau() {
        return tableau;
    }
}
