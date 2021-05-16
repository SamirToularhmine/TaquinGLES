package fr.univ.taquingles;



import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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
        int taille = i.getIntExtra("TAILLE", 3);
        this.mGLView = new MyGLSurfaceView(this, taille);

        FrameLayout f = findViewById(R.id.gl_frame);
        f.addView(this.mGLView);


        this.counter = i.getIntExtra("COUNTER", -1);
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
                if (this.counter <= 0) {
                    System.out.println("FIN DE LA PARTIE DE JEU");
                    afficherPopup("Vous avez perdu !");
                    chronometer.setText("0");
                    chronometer.stop();
                } else {
                    String text = String.valueOf(counter);
                    chronometer.setText(text);
                    this.counter--;
                }
            });

            chronometer.start();
        }
    }

    public void afficherPopup(String texte){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup, null);

        TextView textPopup = popupView.findViewById(R.id.textPopup);
        textPopup.setText(texte);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(this.mGLView, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        /*popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupView.performClick();

                //popupWindow.dismiss();
                return true;
            }
        });
        */

    }


    public void Quitter(View view) {
        finish();
    }
}
