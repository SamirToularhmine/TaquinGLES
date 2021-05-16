package fr.univ.taquingles;



import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;

import fr.univ.taquingles.taquin.Taquin;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */


public class OpenGLES20Activity extends Activity {

    // le conteneur View pour faire du rendu OpenGL
    private GLSurfaceView mGLView;
    private int counter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Définition de View pour cette activité */
        setContentView(R.layout.activity_jeu);

        /* Création de View et association à Activity
           MyGLSurfaceView : classe à implémenter et en particulier la partie renderer */

        /* Pour le plein écran */
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(
                //WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent i = getIntent();
        int taille = i.getIntExtra("taille", 3);
        this.mGLView = new MyGLSurfaceView(this, taille);

        FrameLayout f = findViewById(R.id.gl_frame);
        f.addView(this.mGLView);


        this.counter = i.getIntExtra("counter", 60);
        this.initChrono();

        Button closeButton = findViewById(R.id.close);
        closeButton.setOnClickListener(l -> {
            finish();
        });
    }

    private void initChrono(){
        Chronometer chronometer = findViewById(R.id.chrono);
        if (this.counter == -1 ){
            chronometer.setText("");
        }else {
            chronometer.setOnChronometerTickListener(c -> {
                if (this.counter < 0) {
                    System.out.println("FIN DE LA PARTIE DE JEU");
                    finish(); // @TODO A CHANGER PAR LA SUITE
                } else {
                    String text = String.valueOf(counter);
                    chronometer.setText(text);
                    this.counter--;
                }
            });

            chronometer.start();
        }
    }
}
