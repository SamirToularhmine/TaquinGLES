package fr.univ.taquingles;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */

public class OpenGLES20Activity extends Activity {

    // le conteneur View pour faire du rendu OpenGL
    private MyGLSurfaceView mGLView;
    private int counter;
    private Chronometer chronometer;
    private int nbCoups;
    private int taille;
    private long tempsDebut;

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

        this.counter = i.getIntExtra("COUNTER", -1);
        if (this.counter == -1) {
            this.initChrono();
        } else {
            this.initTimer();
        }

        this.chronometer.start();
        tempsDebut = SystemClock.elapsedRealtime();

        Button closeButton = findViewById(R.id.close);
        closeButton.setOnClickListener(l -> finish());

    }

    private void initChrono() {
        this.chronometer.setCountDown(false);
    }

    private void initTimer() {
        this.chronometer.setCountDown(true);
        this.chronometer.setBase(SystemClock.elapsedRealtime() + counter * 1000 + 1000) ;
        this.chronometer.setOnChronometerTickListener(c -> {
            if (chronometer.getBase() <= SystemClock.elapsedRealtime()) {
                afficherPopup(R.string.perdu, false);
            }
        });
    }

    public void afficherPopup(int texte, boolean gagne) {
        this.chronometer.stop();
        int tempsTotalz  = (int) ((SystemClock.elapsedRealtime() - this.tempsDebut) / 1000);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup, null);

        TextView temps = popupView.findViewById(R.id.temps);
        String texteTemps;
        if(gagne)
            texteTemps = "Temps écoulé : " + this.getTemps(tempsTotalz);
        else
            texteTemps  =  "Temps écoulé : " + this.getTemps(counter);

        temps.setText(texteTemps);

        TextView coups = popupView.findViewById(R.id.coups);
        String coupsTexte = String.valueOf(nbCoups);
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

        new MaterialAlertDialogBuilder(this)
                //.setView(imageSolution.getRootView())
                .setView(popupView)
                .show();
    }

    public void quitter(View view) {
        finish();
    }

    public void restartGame() {
        this.nbCoups = 0;

        TextView coupsTitre = findViewById(R.id.coupsTitre);
        coupsTitre.setText("0");

        Intent i = getIntent();
        this.counter = i.getIntExtra("COUNTER", 0);

        if (counter == -1) {
            this.chronometer.setBase(SystemClock.elapsedRealtime());
        }else{
            this.chronometer.setBase(SystemClock.elapsedRealtime() + counter * 1000 + 1000) ;
        }



        this.chronometer.start();
        this.tempsDebut = SystemClock.elapsedRealtime();
    }

    public void augmenterCoup() {
        this.nbCoups++;
        TextView coupsTitre = findViewById(R.id.coupsTitre);
        String coupsTexte = String.valueOf(nbCoups);
        coupsTitre.setText(coupsTexte);
    }

    private String getTemps(int temps) {
        String secondes = String.valueOf(temps % 60);
        if (secondes.length() == 1) {
            secondes = "0" + secondes;
        }
        String minutes = String.valueOf(temps / 60);
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        return minutes + ":" + secondes;
    }
}
