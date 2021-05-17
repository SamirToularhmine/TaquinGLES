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

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import fr.univ.taquingles.formes.Etoile;
import fr.univ.taquingles.formes.FormeParam;
import fr.univ.taquingles.formes.Losange;
import fr.univ.taquingles.formes.Pentagone;
import fr.univ.taquingles.formes.Square;
import fr.univ.taquingles.formes.Triangle;
import fr.univ.taquingles.taquin.Directions;
import fr.univ.taquingles.taquin.Forme;
import fr.univ.taquingles.taquin.Objet;
import fr.univ.taquingles.taquin.Taquin;

/* MyGLRenderer implémente l'interface générique GLSurfaceView.Renderer */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private RendererManager rendererManager;
    private List<Pair<Forme, FormeParam>> drawQueue;
    private Taquin taquin;
    private Context context;
    private MyGLSurfaceView surfaceView;

    // Les matrices habituelles Model/View/Projection
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private float scale;

    public MyGLRenderer(MyGLSurfaceView surfaceView){
        this.rendererManager = RendererManager.getInstance();
        this.drawQueue = new ArrayList<>();
        this.scale = 1.0f;
        this.surfaceView = surfaceView;
    }

    public void init(Taquin taquin, Context c){
        this.taquin = taquin;
        this.context = c;
    }

    // Première méthode équivalente à la fonction init en OpenGLSL
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // la couleur du fond d'écran
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // On ajoute les différentes formes disponibles pour le taquin
        this.rendererManager.nouvelleForme(Forme.CARRE, new Square());
        this.rendererManager.nouvelleForme(Forme.TRIANGLE, new Triangle());
        this.rendererManager.nouvelleForme(Forme.LOSANGE, new Losange());
        this.rendererManager.nouvelleForme(Forme.ETOILE, new Etoile());
        this.rendererManager.nouvelleForme(Forme.PENTAGONE, new Pentagone());

        /* On initialise le renderer manager avec toutes les formes qu'il contient */
        this.rendererManager.init();

        // On active les textures
        GLES30.glEnable(GLES30.GL_TEXTURE_2D);

        // On active la transparence et on donne une fonction pour ordonner les couches
        GLES30.glEnable(GLES20.GL_BLEND);
        GLES30.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // On définit la couleur d'arrière plan à blanc
        GLES30.glClearColor(1, 1, 1, 1);

        // On mélange le taquin et on crée notre draw queue
        this.taquin.initShuffle();
        this.initialiserDrawQueue();

        // On ajoute la texture choisie par l'utilisateur
        this.rendererManager.addTexture(context, R.drawable.board);
    }

    public void initialiserDrawQueue() {
        Objet[][] objets = this.taquin.getTableau();
        int tailleTableau = objets.length;
        float margin = 3.5f;

        // En fonction de la taille du taquin, il faut espacer et agrandir plus ou moins les formes
        if(tailleTableau == 4){
            this.scale = 2;
            margin = 4.5f;
        }

        if(tailleTableau == 3){
            this.scale = 2.5f;
            margin = 7f;
        }

        this.drawQueue.clear();

        // On ajoute le carré représentant le plateau
        this.drawQueue.add(Pair.create(Forme.CARRE, new FormeParam(new float[]{0, 0, 0}, new float[]{0, 0, 0}, new float[]{10, 10, 1}, R.drawable.board, -1, -1, context)));

        // On y place les différentes formes à l'intérieur en prenant en compte leur couleur, leur position, leur rotation, leur scale
        for(int i = 0; i < objets.length; i++){
            for(int j = 0; j < objets[0].length; j++){
                Objet o = objets[i][j];
                if(o != null){
                    this.drawQueue.add(Pair.create(o.getForme(), new FormeParam(new float[]{-7 + (margin * j), 7 + (margin * -i), 0}, new float[]{0, 0, 0}, new float[]{scale, scale, 1}, o.getCouleur(), i, j, context)));
                }
            }
        }
    }

    /* Deuxième méthode équivalente à la fonction Display */
    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16]; // pour stocker une matrice

        // Nettoyage du buffer de couleur et de profondeur
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glClearColor(1, 1, 1, 1);

        // On calcule la MVP pour chaque forme de la drawQueue.
        // Les matrices de view et de projection ne changent pas, on ne les recalculent pas pour chaque objet
        Matrix.setIdentityM(mViewMatrix,0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        for(Pair<Forme, FormeParam> p : this.drawQueue){
            Matrix.setIdentityM(scratch, 0);
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, p.second.getModelMatrix(), 0);

            // On affiche l'objet à l'écran.
            this.rendererManager.draw(p, scratch);
        }

    }

    /* équivalent au Reshape en OpenGLSL */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // On définit le viewport et une projection orthographique. Le tout est relatif à la taille de l'écran
        float viewX = width / 100.0f;
        float viewY = height / 100.0f;
        GLES30.glViewport(0, 0, width, height);
        Matrix.orthoM(mProjectionMatrix, 0,-viewX, viewX, -viewY, viewY, 1.0f, -1.0f);
    }

    // Méthode permttant de regarder dans les élements afficher quel est celui qui a été cliqué
    // On fait bouger l'objet concerné ou en le fait clignoter si le déplacement est impossible
    // Tous les objets bougent si le taquin est résolu
    public int checkPosition(float x, float y) {
       for (Pair<Forme, FormeParam> current : this.drawQueue) {
           float[] pos = current.second.getPosition();
           if (((x < pos[0] + (1.0 * this.scale)) && (x > pos[0] - (1.0 * this.scale)) && (y < pos[1] + (1.0 * this.scale)) && (y > pos[1] - (1.0 * this.scale)))) {
               if(current.second.getPosI() != -1 && current.second.getPosJ() != -1){
                   int i = current.second.getPosI();
                   int j = current.second.getPosJ();
                   Directions directionVide = this.taquin.directionVide(i, j);
                   if(directionVide != null){
                       switch (directionVide){
                           case GAUCHE:
                               this.taquin.bougerVideDroite();
                               break;
                           case DROITE:
                               this.taquin.bougerVideGauche();
                               break;
                           case HAUT:
                               this.taquin.bougerVideBas();
                               break;
                           case BAS:
                               this.taquin.bougerVideHaut();
                               break;
                       }
                       OpenGLES20Activity activity = (OpenGLES20Activity) context;
                       activity.augmenterCoup();
                       this.initialiserDrawQueue();
                       this.surfaceView.requestRender();
                       if (this.taquin.isFinished()){
                           this.drawQueue.forEach(p -> p.second.startBlinking());
                           return 2;
                       }

                       return 1;
                   }else{
                       current.second.startBlinking();
                       return -1;
                   }
               }
           }
       }
       return 0;
    }

}
