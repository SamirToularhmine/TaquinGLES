package fr.univ.taquingles.taquin;

/**
 * Classe qui contient un objet du taquin
 * Les coordonnées x et y sont la position dans le ta talbeau
 */
public class Objet {
    private int x;
    private int y;
    private int initialX;
    private int initialY;
    private Couleur Couleur;
    private Forme forme;

    public Objet(int x, int y, Couleur Couleur, Forme forme) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.initialY = y;
        this.Couleur = Couleur;
        this.forme = forme;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Couleur getCouleur() {
        return Couleur;
    }

    public Forme getForme() {
        return forme;
    }


    /**
     * Vérifie que l'objet est a la palce initiale
     * @return true si l'obhet est à sa place initiale
     */
    public boolean isPlaceCorrecte(){
        return this.initialX == this.x && this.initialY == this.y;
    }
}
