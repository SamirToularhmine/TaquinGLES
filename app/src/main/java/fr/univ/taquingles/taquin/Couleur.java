package fr.univ.taquingles.taquin;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public enum Couleur {
    ROUGE(new float[]{1.0f, 0.0f, 0.0f, 1.0f}),
    VERT(new float[]{0.0f, 1.0f, 0.0f, 1.0f}),
    BLEU(new float[]{0.0f, 0.0f, 1.0f, 1.0f}),
    JAUNE(new float[]{1.0f, 1.0f, 0.0f, 1.0f}),
    ROSE(new float[]{1.0f, 0.0f, 1.0f, 1.0f}), // c'est du violet en réalité mais Samir voulait du rose donc on va dire que c'est du rose :)
    BOIS(new float[]{0.52f, 0.37f, 0.26f, 1.0f}),
    BLANC(new float[]{1.0f, 1.0f, 1.0f, 1.0f});

    private float[] couleur;

    public float[] getCouleurArray(int n) {
        float[] couleurs = new float[n * 4];

        for(int i = 0; i < n * 4; i += 4){
            couleurs[i] = this.couleur[0];
            couleurs[i + 1] = this.couleur[1];
            couleurs[i + 2] = this.couleur[2];
            couleurs[i + 3] = this.couleur[3];
        }

        return couleurs;
    }

    public FloatBuffer getCouleurBuffer(int n){
        float[] couleurs = this.getCouleurArray(n);

        ByteBuffer bc = ByteBuffer.allocateDirect(couleurs.length * 4);
        bc.order(ByteOrder.nativeOrder());
        FloatBuffer cb = bc.asFloatBuffer();
        cb.put(couleurs);
        cb.flip();

        return cb;
    }

    private Couleur(float[] rgba) {
        this.couleur = rgba;
    }
}