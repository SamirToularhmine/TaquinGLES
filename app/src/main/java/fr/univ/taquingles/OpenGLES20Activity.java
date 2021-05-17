package fr.univ.taquingles;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */


public class OpenGLES20Activity extends Activity {

    // le conteneur View pour faire du rendu OpenGL
    private MyGLSurfaceView mGLView;
    private int counter;
    private int tempsTotal;
    private Chronometer chronometer;
    private int nbCoups;
    private int taille;

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
        this.taille = i.getIntExtra("TAILLE", 3);
        this.mGLView = new MyGLSurfaceView(this, this.taille);

        FrameLayout f = findViewById(R.id.gl_frame);
        f.addView(this.mGLView);
        this.chronometer = findViewById(R.id.chrono);


        this.nbCoups = 0;
        this.tempsTotal = -1;

        this.counter = i.getIntExtra("COUNTER", -1);
        if (this.counter == -1) {
            this.initChrono();
        } else {
            this.initTimer();
        }

        this.chronometer.start();

        Button closeButton = findViewById(R.id.close);
        closeButton.setOnClickListener(l -> finish());

    }

    private void initChrono(){
        this.chronometer.setCountDown(false);

        this.chronometer.setOnChronometerTickListener(c -> this.tempsTotal++);
    }

    private void initTimer() {
        this.chronometer.setOnChronometerTickListener(c -> {
            if (this.counter <= 0) {
                afficherPopup(R.string.perdu, false);
                this.chronometer.setText("00:00");
            } else {
                String text = this.getTemps(counter);
                this.chronometer.setText(text);
                this.counter--;
                this.tempsTotal++;
            }
        });
        String text = this.getTemps(counter);
        this.chronometer.setText(text);
    }

    public void afficherPopup(int texte, boolean gagne) {
        this.chronometer.stop();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup, null);

        TextView temps = popupView.findViewById(R.id.temps);
        String texteTemps;

        if (gagne){
            texteTemps = "Temps écoulé : " + this.getTemps(this.tempsTotal);
        }else{
            texteTemps = "Temps écoulé : " + this.getTemps(++this.tempsTotal);
        }
        temps.setText(texteTemps );

        TextView coups = popupView.findViewById(R.id.coups);
        TextView coupsTitre = findViewById(R.id.coupsTitre);
        String coupsTexte = "Nombre de coups : " + coupsTitre.getText();
        coups.setText(coupsTexte);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(popupView);
        alertDialog.setTitle(texte);
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button button = popupView.findViewById(R.id.recommencer);
        button.setOnClickListener(v -> {
            alertDialog.dismiss();
            this.restartGame();
            this.mGLView.restartGame();

        });

    }


    public void afficherSolution(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_solution, null);

        ImageView imageSolution = popupView.findViewById(R.id.imageSolution);

        int taille = getIntent().getIntExtra("TAILLE", 3);

        if (taille == 4) {
            imageSolution.setImageResource(R.drawable.taquin_44);
        } else if (taille == 5) {
            imageSolution.setImageResource(R.drawable.taquin_55);
        } else {
            imageSolution.setImageResource(R.drawable.taquin_33);
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(this.mGLView, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupView.performClick();
            popupWindow.dismiss();
            return true;
        });


    }

    public void quitter(View view) {
        finish();
    }

    public void restartGame() {
        this.nbCoups = 0;
        this.tempsTotal = 0;

        TextView coupsTitre = findViewById(R.id.coupsTitre);
        coupsTitre.setText("0");

        Intent i = getIntent();
        this.counter = i.getIntExtra("COUNTER", -1);

        if (counter == -1){
            this.chronometer.setBase(SystemClock.elapsedRealtime());
        }



        this.chronometer.start();
    }

    public void augmenterCoup(){
        this.nbCoups++;
        TextView coupsTitre = findViewById(R.id.coupsTitre);
        String coupsTexte = String.valueOf(nbCoups);
        coupsTitre.setText(coupsTexte);
    }

    private String getTemps(int temps){
        String secondes = String.valueOf(temps % 60);
        if (secondes.length() == 1){
            secondes = "0" + secondes;
        }
        String minutes = String.valueOf(temps / 60);
        if (minutes.length() == 1){
            minutes = "0" + minutes;
        }
        return minutes + ":" + secondes;
    }
}
