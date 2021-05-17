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

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.univ.taquingles.taquin.Taquin;

/* La classe MyGLSurfaceView avec en particulier la gestion des événements
  et la création de l'objet renderer

*/


/* On va dessiner un carré qui peut se déplacer grace à une translation via l'écran tactile */

public class MyGLSurfaceView extends GLSurfaceView {

    /* Un attribut : le renderer (GLSurfaceView.Renderer est une interface générique disponible) */
    /* MyGLRenderer va implémenter les méthodes de cette interface */

    private final MyGLRenderer mRenderer;
    private Taquin taquin;

    public MyGLSurfaceView(Context context, int taille) {
        super(context);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // Création d'un context OpenGLES 2.0
        setEGLContextClientVersion(3);

        // Création du renderer qui va être lié au conteneur View créé
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
        this.taquin =  new Taquin(taille);
        this.mRenderer.init(taquin, context);

        // Option pour indiquer qu'on redessine uniquement si les données changent
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /* pour gérer la translation */
    private float mPreviousX;
    private float mPreviousY;
    private boolean condition = false;
    private int nbBlinking = 0;

    /* Comment interpréter les événements sur l'écran tactile */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Les coordonnées du point touché sur l'écran
        float x = e.getX();
        float y = e.getY();

        // la taille de l'écran en pixels
        float screen_x = getWidth();
        float screen_y = getHeight();

        /* accès aux paramètres du rendu (cf MyGLRenderer.java)
        soit la position courante du centre du carré
         */
        float[] pos = new float[] {0, 0};

        /* Conversion des coordonnées pixel en coordonnées OpenGL
        Attention l'axe x est inversé par rapport à OpenGLSL
        On suppose que l'écran correspond à un carré d'arête 2 centré en 0
         */

        float x_opengl = 20.0f*x/getWidth() - 10.0f;
        float y_opengl = -40.0f * y/getHeight() + 20.0f;

        /* Le carré représenté a une arête de 2 (oui il va falloir changer cette valeur en dur !!)
        /* On teste si le point touché appartient au carré ou pas car on ne doit le déplacer que si ce point est dans le carré
        */


        if(this.nbBlinking == 0) {
            int test_square = this.mRenderer.checkPosition(x_opengl, y_opengl);

            if (test_square == -1 || test_square == 2) {
                final Handler handler = new Handler();
                final int[] i = {0};
                this.nbBlinking++;

                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (i[0] > 4) {
                            handler.removeMessages(0);
                            nbBlinking--;
                            return;
                        }
                        requestRender();
                        i[0]++;
                        handler.postDelayed(this, 1000 / 4);
                    }
                }, 1000 / 4);
            }

            if(test_square == 2){
                OpenGLES20Activity activity = (OpenGLES20Activity) this.getContext();
                MediaPlayer music = MediaPlayer.create(activity, R.raw.victory_sound);
                music.seekTo(1000);
                music.start();
                activity.afficherPopup(R.string.gagne, true);
            }

            if ((condition || test_square == 1) && nbBlinking == 0) {

                switch (e.getAction()) {
                    /* Lorsqu'on touche l'écran on mémorise juste le point */
                    case MotionEvent.ACTION_DOWN:
                        mPreviousX = x;
                        mPreviousY = y;
                        condition = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
                        condition = false;
                }
            }
        }

        return true;
    }

    public void restartGame(){
        this.taquin.initShuffle();
        this.mRenderer.initialiserDrawQueue();
        this.requestRender();

    }
}
