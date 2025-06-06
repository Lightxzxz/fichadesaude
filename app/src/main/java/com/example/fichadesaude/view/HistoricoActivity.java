package com.example.fichadesaude.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fichadesaude.R;
import com.example.fichadesaude.database.FichaDbHelper;
import com.example.fichadesaude.model.FichaSaude;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {
    private ListView listViewFichas;
    private TextView txtTotalFichas, txtMediaIdade, txtMediaIMC;
    private FichaDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        dbHelper = new FichaDbHelper(this);

        listViewFichas = findViewById(R.id.listViewFichas);
        txtTotalFichas = findViewById(R.id.txtTotalFichas);
        txtMediaIdade = findViewById(R.id.txtMediaIdade);
        txtMediaIMC = findViewById(R.id.txtMediaIMC);

        carregarFichas();
        carregarEstatisticas();

        listViewFichas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FichaSaude ficha = (FichaSaude) parent.getItemAtPosition(position);
                abrirVisualizacao(ficha.getId());
            }
        });
    }

    private void carregarFichas() {
        List<FichaSaude> fichas = dbHelper.getAllFichas();
        ArrayAdapter<FichaSaude> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, fichas);
        listViewFichas.setAdapter(adapter);
    }

    private void carregarEstatisticas() {
        int total = dbHelper.getFichasCount();
        double mediaIdade = dbHelper.getMediaIdade();
        double mediaIMC = dbHelper.getMediaIMC();

        txtTotalFichas.setText("Total de fichas: " + total);
        txtMediaIdade.setText("Média de idade: " + String.format("%.1f", mediaIdade));
        txtMediaIMC.setText("Média de IMC: " + String.format("%.1f", mediaIMC));
    }

    private void abrirVisualizacao(int fichaId) {
        Intent intent = new Intent(this, VisualizacaoActivity.class);
        intent.putExtra("FICHA_ID", fichaId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarFichas();
        carregarEstatisticas();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}