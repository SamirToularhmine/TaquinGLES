package fr.univ.taquingles.formes;

import android.content.Context;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;


import fr.univ.taquingles.taquin.Couleur;

public class FormeParam {

    // Transform
    private float[] position;
    private float[] rotation;
    private float[] scale;

    //Position dans le taquin
    private int posI;
    private int posJ;

    private float[] modelMatrix;

    // Color
    private Couleur couleur;

    // Texture
    private int idTextureResource;
    private boolean textured;

    private static final long blinkDuration = 1000;
    private boolean blinking;
    private Couleur previousColor;
    private long blinkStart;
    private Context context;

    public FormeParam(float[] position, float[] rotation, float[] scale, Couleur couleur, int posI, int posJ, Context context) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.couleur = couleur;
        this.posI = posI;
        this.posJ = posJ;
        this.modelMatrix = new float[16];
        this.textured = false;
        this.blinking = false;
        this.context = context;

        this.processModelMatrix();
    }

    public FormeParam(float[] position, float[] rotation, float[] scale, int idTextureResource, int posI, int posJ, Context context) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.idTextureResource = idTextureResource;
        this.posI = posI;
        this.posJ = posJ;
        this.modelMatrix = new float[16];
        this.textured = true;
        this.couleur = Couleur.BOIS;
        this.blinking = false;
        this.context = context;

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

    // Méthode permettant de démarrer 4 jobs qui s'éxécuteront respectivement à 1/4, 2/4, 3/4 et 4/4 du temps de clignotement.
    // Ces jobs mette la couleur actuelle à blanc ou remette la couleur à celle définie initialement
    public void startBlinking(){
        this.blinkStart = System.currentTimeMillis();
        this.previousColor = this.couleur;
        this.blinking = true;

        final Handler handler = new Handler(Looper.getMainLooper());

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCouleur(Couleur.BLANC);
                v.vibrate(blinkDuration / 4);
            }
        }, blinkDuration / 4);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCouleur(previousColor);
            }
        }, 2 * blinkDuration / 4);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCouleur(Couleur.BLANC);
                v.vibrate(blinkDuration / 4);
            }
        }, 3 * blinkDuration / 4);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopBlinking();
                setCouleur(previousColor);
            }
        }, blinkDuration);
    }

    public void stopBlinking(){
        this.blinkStart = 0;
        this.couleur = this.previousColor;
        this.blinking = false;
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

    public int getPosI() {
        return posI;
    }

    public void setPosI(int posI) {
        this.posI = posI;
    }

    public int getPosJ() {
        return posJ;
    }

    public void setPosJ(int posJ) {
        this.posJ = posJ;
    }

    public boolean isTextured() {
        return this.textured;
    }

    public void setTextured(boolean textured) {
        this.textured = textured;
    }

    public boolean isBlinking() {
        return blinking;
    }

    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    public long getBlinkStart() {
        return blinkStart;
    }
}
