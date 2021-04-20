package fr.univ.taquingles.taquin;


/**
 * Classe pour la gestion du taquin
 */
public class Taquin {
    private Objet[][] tableau; // est le tableau du taquin
    private int xVide;
    private int yVide;
    private int n; // la taille de la premiere dimmension
    private int m; // la taille de la derniere dimmension

    public Taquin(int n, int m) {
        this.n = n;
        this.m = m;
        this.tableau = new Objet[n][m];
    }

    /**
     * Méthode qui crée un taquin soluble 3 * 3
     */
    public void initialiser33(){
        tableau[0][0] = new Objet(0, 0, COULEUR.ROUGE, FORME.CARRE);
        tableau[1][0] = new Objet(1, 0, COULEUR.VERT, FORME.CARRE);
        tableau[2][0] = new Objet(2, 0, COULEUR.JAUNE, FORME.CARRE);

        tableau[0][1] = new Objet(0, 1, COULEUR.ROUGE, FORME.TRIANGLE);
        tableau[1][1] = new Objet(1, 1, COULEUR.VERT, FORME.TRIANGLE);
        tableau[2][1] = new Objet(2, 1, COULEUR.JAUNE, FORME.TRIANGLE);

        tableau[0][2] = new Objet(0, 2, COULEUR.ROUGE, FORME.CERCLE);
        tableau[1][2] = new Objet(1, 2, COULEUR.VERT, FORME.CERCLE);
        tableau[2][2] = null;
        xVide = 2;
        yVide = 2;
    }

    /**
     * Méthode qui crée un taquin soluble 4 * 4
     */
    public void initialiser44(){
        tableau[0][0] = new Objet(0, 0, COULEUR.ROUGE, FORME.CARRE);
        tableau[1][0] = new Objet(1, 0, COULEUR.VERT, FORME.CARRE);
        tableau[2][0] = new Objet(2, 0, COULEUR.JAUNE, FORME.CARRE);
        tableau[3][0] = new Objet(3, 0, COULEUR.BLEU, FORME.CARRE);

        tableau[0][1] = new Objet(0, 1, COULEUR.ROUGE, FORME.TRIANGLE);
        tableau[1][1] = new Objet(1, 1, COULEUR.VERT, FORME.TRIANGLE);
        tableau[2][1] = new Objet(2, 1, COULEUR.JAUNE, FORME.TRIANGLE);
        tableau[3][1] = new Objet(3, 1, COULEUR.BLEU, FORME.TRIANGLE);

        tableau[0][2] = new Objet(0, 2, COULEUR.ROUGE, FORME.CERCLE);
        tableau[1][2] = new Objet(1, 2, COULEUR.VERT, FORME.CERCLE);
        tableau[2][2] = new Objet(2, 2, COULEUR.JAUNE, FORME.CERCLE);
        tableau[3][2] = new Objet(3, 2, COULEUR.BLEU, FORME.CERCLE);

        tableau[0][3] = new Objet(0, 3, COULEUR.ROUGE, FORME.LOSANGE);
        tableau[1][3] = new Objet(1, 3, COULEUR.VERT, FORME.LOSANGE);
        tableau[2][3] = new Objet(2, 3, COULEUR.BLEU, FORME.LOSANGE);
        tableau[3][3] = null;
        xVide = 3;
        yVide = 3;
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
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m - 1; j++) {
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
        if (xVide == n - 1){
            return false;
        }
        Objet dessus = tableau[xVide + 1][yVide];
        dessus.setX(dessus.getX() - 1);
        xVide++;
        tableau[dessus.getX()][dessus.getY()] = dessus;
        tableau[xVide][yVide] = null;
        return true;
    }

    /**
     * Déplacement du vide vers la droite
     * @return false si le déplacement n'est pas possible
     */
    public boolean bougerVideDroite(){
        if (yVide == m - 1){
            return false;
        }
        Objet dessus = tableau[xVide][yVide + 1];
        dessus.setY(dessus.getY() - 1);
        yVide++;
        tableau[dessus.getX()][dessus.getY()] = dessus;
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
        Objet dessus = tableau[xVide][yVide - 1];
        dessus.setY(dessus.getY() + 1);
        yVide--;
        tableau[dessus.getX()][dessus.getY()] = dessus;
        tableau[xVide][yVide] = null;
        return true;
    }

    public Objet[][] getTableau() {
        return tableau;
    }
}
