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
    }

    public void lancerJeu(View view) {
        Intent i = new Intent(this, OpenGLES20Activity.class);

        RadioGroup radioGroupTaille = findViewById(R.id.radiogroup_taille);
        int taille = 3;
        int idSelectedTaille = radioGroupTaille.getCheckedRadioButtonId();

        if (idSelectedTaille == R.id.radioButton_44){
            taille = 4;
        }else if(idSelectedTaille == R.id.radioButton_55){
            taille = 5;
        }

        i.putExtra("TAILLE", taille);


        RadioGroup radioGroupNiveau = findViewById(R.id.radiogroup_niveau);
        int counter = -1; // pas de durée par defaut
        int idSelectedNiveau = radioGroupNiveau.getCheckedRadioButtonId();

        if (idSelectedNiveau == R.id.radioButton_moyen){
            counter = 60 * 5; // 5 minutes
        }else if(idSelectedNiveau == R.id.radioButton_difficile){
            counter = 5;
        }

        i.putExtra("COUNTER", counter);

        startActivity(i);
    }

    public void select33(View view) {
        RadioGroup radioGroupNiveau = findViewById(R.id.radiogroup_taille);
        radioGroupNiveau.check(R.id.radioButton_33);
    }

    public void select44(View view) {
        RadioGroup radioGroupNiveau = findViewById(R.id.radiogroup_taille);
        radioGroupNiveau.check(R.id.radioButton_44);
    }

    public void select55(View view) {
        RadioGroup radioGroupNiveau = findViewById(R.id.radiogroup_taille);
        radioGroupNiveau.check(R.id.radioButton_55);
    }
}