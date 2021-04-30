package fr.univ.taquingles.formes;

public class Pentagone extends AForme {

    public Pentagone() {
        super();

        // Le tableau des vertices
        this.setVertices(new float[]{
                -0.60f, -1.0f, 0.0f, // bas gauche 0
                0.60f, -1.0f, 0.0f, // bas droite 1

                1.0f, 0.20f, 0.0f, // haut droite 2
                -1.0f, 0.20f, 0.0f, // haut gauche 3

                0.0f, 1.0f, 0.0f, // milieu haut 4
                0.0f, 0.0f, 0.0f, // centre 5

        });

        // Le tableau des indices
        this.setIndices(new int[]{
                0, 5, 1,
                3, 5, 0,
                4, 5, 3,
                2, 5, 4,
                1, 5, 2,
        });
    }
}
