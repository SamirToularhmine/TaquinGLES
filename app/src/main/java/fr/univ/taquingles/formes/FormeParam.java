package fr.univ.taquingles.formes;

import android.opengl.Matrix;

import fr.univ.taquingles.taquin.Couleur;

public class FormeParam {

    // Transform
    private float[] position;
    private float[] rotation;
    private float[] scale;

    private float[] modelMatrix;

    // Color
    private Couleur couleur;

    public FormeParam(float[] position, float[] rotation, float[] scale, Couleur couleur) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.couleur = couleur;

        this.modelMatrix = new float[16];

        this.processModelMatrix();
    }

    public void processModelMatrix(){
        // On met la matrice de modèle à la matrice identité
        Matrix.setIdentityM(this.modelMatrix, 0);

        // On applique une translation de la matrice de modèle grâce au vecteur position
        Matrix.translateM(this.modelMatrix, 0, this.position[0], this.position[1], this.position[2]);

        // On applique une rotation sur tous les angles grâce au vecteur de rotation
        Matrix.rotateM(this.modelMatrix, 0, this.rotation[0], 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(this.modelMatrix, 0, this.rotation[1], 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(this.modelMatrix, 0, this.rotation[2], 0.0f, 0.0f, 1.0f);

        // On applique un scale sur la matrice de modèle grâce au vecteur de scale
        Matrix.scaleM(this.modelMatrix, 0, this.scale[0], this.scale[1], this.scale[2]);
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getRotation() {
        return rotation;
    }

    public void setRotation(float[] rotation) {
        this.rotation = rotation;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public float[] getModelMatrix() {
        return this.modelMatrix;
    }
}
