package fr.univ.taquingles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Spinner spinner = findViewById(R.id.spinner);

        String [] valeurs = new String[]{"3 x 3", "4 x 4", "5 x 5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, valeurs);
        spinner.setAdapter(adapter);

        RadioGroup radioGroupNiveau = findViewById(R.id.radiogroup_niveau);

        radioGroupNiveau.check(R.id.radioButton_facile);

    }

    public void lancerJeu(View view) {
        Intent i = new Intent(this, OpenGLES20Activity.class);


        Spinner spinner = findViewById(R.id.spinner);
        int taille;
        switch (spinner.getSelectedItem().toString()){
            case "5 x 5":
                taille = 5;
                break;
            case "4 x 4":
                taille = 4;
                break;
            default:
                taille = 3;
        }

        i.putExtra("TAILLE", taille);


        RadioGroup radioGroupNiveau = findViewById(R.id.radiogroup_niveau);
        int counter = -1; // pas de durée par defaut
        int idSelected = radioGroupNiveau.getCheckedRadioButtonId();

        if (idSelected == R.id.radioButton_moyen){
            counter = 60 * 5; // 5 minutes
        }else if(idSelected == R.id.radioButton_difficile){
            counter = 60;
        }

        i.putExtra("COUNTER", counter);

        startActivity(i);
    }
}