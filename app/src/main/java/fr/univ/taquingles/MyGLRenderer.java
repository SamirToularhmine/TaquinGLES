/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.univ.taquingles;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.util.Pair;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.univ.taquingles.taquin.Couleur;
import fr.univ.taquingles.taquin.Forme;

import static javax.microedition.khronos.opengles.GL11.GL_VIEWPORT;

/* MyGLRenderer implémente l'interface générique GLSurfaceView.Renderer */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    private RendererManager rendererManager;
    private List<Pair<Forme, FormeParam>> drawQueue;

    // Les matrices habituelles Model/View/Projection
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    public MyGLRenderer(){
        this.rendererManager = RendererManager.getInstance();
        this.drawQueue = new ArrayList<>();
    }


    /* Première méthode équivalente à la fonction init en OpenGLSL */
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // la couleur du fond d'écran
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // On ajoute les différentes formes disponibles
        this.rendererManager.nouvelleForme(Forme.TRIANGLE, new Triangle());
        this.rendererManager.nouvelleForme(Forme.CARRE, new Square());

        /* On initialise le renderer manager avec toutes les formes qu'il contient */
        this.rendererManager.init();

        /* On crée notre draw queue */

        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{1.25f, 12.5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.VERT)));
        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{3.75f, 12.5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.JAUNE)));
        this.drawQueue.add(Pair.create(Forme.TRIANGLE, new FormeParam(new float[]{6.25f, 12.5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.BLEU)));
        this.drawQueue.add(Pair.create(Forme.TRIANGLE, new FormeParam(new float[]{8.75f, 12.5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.ROUGE)));

        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{1.25f, 10, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.VERT)));
        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{3.75f, 10, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.JAUNE)));
        this.drawQueue.add(Pair.create(Forme.TRIANGLE, new FormeParam(new float[]{6.25f, 10, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.BLEU)));
        this.drawQueue.add(Pair.create(Forme.TRIANGLE, new FormeParam(new float[]{8.75f, 10f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.ROUGE)));

        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{1.25f, 7.5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.VERT)));
        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{3.75f, 7.5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.JAUNE)));
        this.drawQueue.add(Pair.create(Forme.TRIANGLE, new FormeParam(new float[]{6.25f, 7.5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.BLEU)));
        
        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{1.25f, 5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.VERT)));
        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{3.75f, 5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.JAUNE)));
        this.drawQueue.add(Pair.create(Forme.TRIANGLE, new FormeParam(new float[]{6.25f, 5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.BLEU)));
        this.drawQueue.add(Pair.create(Forme.TRIANGLE, new FormeParam(new float[]{8.75f, 5f, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1}, Couleur.ROUGE)));

    }

    /* Deuxième méthode équivalente à la fonction Display */
    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16]; // pour stocker une matrice

        // glClear rien de nouveau on vide le buffer de couleur et de profondeur */
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        /* on utilise une classe Matrix (similaire à glm) pour définir nos matrices P, V et M*/

        /* Pour le moment on va utiliser une projection orthographique
           donc View = Identity
         */

        /*pour positionner la caméra mais ici on n'en a pas besoin*/

        //Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setIdentityM(mViewMatrix,0);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        for(Pair<Forme, FormeParam> p : this.drawQueue){
            Matrix.setIdentityM(scratch, 0);

            Log.d("Renderer", "mSquarex"+Float.toString(0));
            Log.d("Renderer", "mSquarey"+Float.toString(0));

            /* scratch est la matrice PxVxM finale */
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, p.second.getModelMatrix(), 0);

            /* on appelle la méthode dessin du carré élémentaire */
            //mSquare.draw(scratch);
            this.rendererManager.draw(p, scratch);
        }

    }

    /* équivalent au Reshape en OpenGLSL */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        /* ici on aurait pu se passer de cette méthode et déclarer
        la projection qu'à la création de la surface !!
         */
        GLES30.glViewport(0, 0, width, height);
        float aspectRatio = (float) width / (float) height;
        Matrix.orthoM(mProjectionMatrix,  0, 0, aspectRatio, 0, 1, -1.0f, 1.0f);
        Matrix.scaleM(mProjectionMatrix, 0, 0.05f, 0.05f, 1.0f);

    }

    /* La gestion des shaders ... */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }


    /* Les méthodes nécessaires à la manipulation de la position finale du carré */
   public void setPosition(float x, float y) {
        /*mSquarePosition[0] += x;
        mSquarePosition[1] += y;*/
    }

    /*public float[] getPosition() {
        return mSquarePosition;
    }*/

}
