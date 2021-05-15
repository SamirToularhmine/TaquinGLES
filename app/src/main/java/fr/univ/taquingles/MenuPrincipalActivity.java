package fr.univ.taquingles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

    }

    public void lancerJeu(View view) {
        Spinner spinner = findViewById(R.id.spinner);
        System.out.println(spinner.getSelectedItem().toString());
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

        Intent i = new Intent(this, OpenGLES20Activity.class);
        i.putExtra("taille", taille);
        startActivity(i);
    }
}