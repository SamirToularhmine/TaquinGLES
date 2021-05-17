package fr.univ.taquingles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_principal);

        Slider slider = findViewById(R.id.sliderNiveau);
        slider.setLabelFormatter(v -> {
            if(Math.round(v) == 0){
                return "Temps illimité";
            }
            if(Math.round(v) == 1){
                return "5 minutes";
            }
            return "1 minute";
        });

        Spinner spinner = findViewById(R.id.spinner);

        String [] valeurs = new String[]{getString(R.string.defaut), getString(R.string.vert), "Covid-19"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, valeurs);
        spinner.setAdapter(adapter);
    }


    /**
     * Lance le jeu en mettant dans un intent toutes les données choisies par l'utilisateur
     */
    public void lancerJeu(View view) {
        Intent i = new Intent(this, OpenGLES20Activity.class);

        TabLayout tabs = findViewById(R.id.tabs);

        int idSelectedTaille = tabs.getSelectedTabPosition();
        int taille = 3;

        if (idSelectedTaille == 1){
            taille = 4;
        }else if(idSelectedTaille == 2){
            taille = 5;
        }

        i.putExtra("TAILLE", taille);

        Slider slider = findViewById(R.id.sliderNiveau);
        int counter = -1; // pas de durée par defaut
        int idSelectedNiveau = Math.round(slider.getValue());

        if (idSelectedNiveau == 1){
            counter = 60 * 5; // 5 minutes
        }else if(idSelectedNiveau == 2){
            counter = 60; // 1 minute
        }

        i.putExtra("COUNTER", counter);

        startActivity(i);
    }

    public void select33(View view) {
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.selectTab(tabs.getTabAt(0));
    }

    public void select44(View view) {
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.selectTab(tabs.getTabAt(1));
    }

    public void select55(View view) {
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.selectTab(tabs.getTabAt(2));
    }
}
