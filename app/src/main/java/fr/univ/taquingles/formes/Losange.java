package fr.univ.taquingles.formes;

public class Losange extends AForme {

    public Losange(){
        super();

        // Le tableau des vertices
        this.setVertices(new float[]{
                -0.75f,   0.0f, 0.0f,
                0.0f,  1.0f, 0.0f,
                0.75f,  0.0f, 0.0f,
                0.f,  -1.f, 0.0f });

        // Le tableau des indices
        this.setIndices(new int[]{ 0, 1, 2, 0, 3, 2 });
    }
}
