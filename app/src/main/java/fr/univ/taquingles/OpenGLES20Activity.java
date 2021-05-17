package fr.univ.taquingles;


import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.app.Activity;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    /**
     * Méthode qui déclare le chonometre comme un vrai chronomètre
     */
    private void initChrono() {
        this.chronometer.setCountDown(false);
    }

    /**
     * Méthode qui déclare le chronomètre comme un compte à rebours
     * Et définit les actions à effectuer lors que le temps arrive à 0
     */
    private void initTimer() {
        this.chronometer.setCountDown(true);
        this.chronometer.setBase(SystemClock.elapsedRealtime() + counter * 1000 + 1000) ;
        this.chronometer.setOnChronometerTickListener(c -> {
            if (chronometer.getBase() <= SystemClock.elapsedRealtime()) {
                MediaPlayer music = MediaPlayer.create(this, R.raw.lose);
                music.start();
                afficherPopup(R.string.perdu, false);
            }
        });
    }

    /**
     * Méthode qui affiche la popup en fin de jeu
     * @param texte est le texte à afficher
     * @param gagne booléen pour savoir si la partie est gagnée ou perdue
     */
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

    /**
     * Méthode qui crée une popup qui affiche la solution du taquin
     */
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
                .setView(popupView)
                .show();
    }

    /**
     * Méthode pour arreter l'activity
     */
    public void quitter(View view) {
        finish();
    }

    /**
     * Initialise tous les compteurs pour relancer une partie
     */
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

    /**
     * Augmente le nombre de coups et affiche le nombre de coups
     */
    public void augmenterCoup() {
        this.nbCoups++;
        TextView coupsTitre = findViewById(R.id.coupsTitre);
        String coupsTexte = String.valueOf(nbCoups);
        coupsTitre.setText(coupsTexte);
    }

    /**
     * Retourne le temps au format mm:ss
     * @param temps en secondes
     * @return string dans le bon format
     */
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
