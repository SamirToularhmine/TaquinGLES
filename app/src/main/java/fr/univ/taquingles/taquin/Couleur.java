package fr.univ.taquingles.taquin;

public enum Couleur {
    ROUGE(new float[]{1.0f, 0.0f, 0.0f, 1.0f}),
    VERT(new float[]{0.0f, 1.0f, 0.0f, 1.0f}),
    BLEU(new float[]{0.0f, 0.0f, 1.0f, 1.0f}),
    JAUNE(new float[]{1.0f, 1.0f, 0.0f, 1.0f}),
    ROSE(new float[]{1.0f, 0.0f, 1.0f, 1.0f}), // c'est du violet en réalité mais Samir voulait du rose donc on va dire que c'est du rose :)
    BOIS(new float[]{0.52f, 0.37f, 0.26f, 1.0f}),
    BLANC(new float[]{1.0f, 1.0f, 1.0f, 1.0f});

    private float[] couleur;

    public float[] getCouleurArray(){
        return this.couleur;
    }

    Couleur(float[] rgba) {
        this.couleur = rgba;
    }
}