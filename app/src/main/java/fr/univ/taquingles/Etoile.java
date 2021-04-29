package fr.univ.taquingles;

public class Etoile extends AForme {

    public Etoile(){
        super();

        // Le tableau des vertices
        this.setVertices(new float[]{
                -0.9f,   -0.5f, 0.0f, // bas gauche
                0.0f,  1.0f, 0.0f, // milieu haut
                0.9f,  -0.5f, 0.0f, // bas droite


                0.0f,  -1.0f, 0.0f, // milieu bas
                -0.9f,  0.5f, 0.0f, // haut gauche
                0.9f,  0.5f, 0.0f // haut droite
        });

        // Le tableau des indices
        this.setIndices(new int[]{ 0, 1, 2, 3, 4, 5});
    }
}
