package com.example.fichadesaude.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fichadesaude.R;
import com.example.fichadesaude.database.FichaDbHelper;
import com.example.fichadesaude.model.FichaSaude;

public class VisualizacaoActivity extends AppCompatActivity {
    private TextView txtNome, txtIdade, txtPeso, txtAltura, txtIMC, txtInterpretacao, txtPressao;
    private Button btnEditar;
    private FichaDbHelper dbHelper;
    private int fichaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizacao);

        dbHelper = new FichaDbHelper(this);

        fichaId = getIntent().getIntExtra("FICHA_ID", -1);
        if (fichaId == -1) {
            finish();

        txtNome = findViewById(R.id.txtNome);
        txtIdade = findViewById(R.id.txtIdade);
        txtPeso = findViewById(R.id.txtPeso);
        txtAltura = findViewById(R.id.txtAltura);
        txtIMC = findViewById(R.id.txtIMC);
        txtInterpretacao = findViewById(R.id.txtInterpretacao);
        txtPressao = findViewById(R.id.txtPressao);
        btnEditar = findViewById(R.id.btnEditar);

        carregarFicha();

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(VisualizacaoActivity.this, CadastroActivity.class);
            intent.putExtra("FICHA_ID", fichaId);
            startActivity(intent);
        });
    }

    private void carregarFicha() {
        FichaSaude ficha = dbHelper.getFicha(fichaId);
        if (ficha != null) {
            txtNome.setText(ficha.getNome());
            txtIdade.setText(String.valueOf(ficha.getIdade()));
            txtPeso.setText(String.format("%.2f", ficha.getPeso()));
            txtAltura.setText(String.format("%.2f", ficha.getAltura()));
            txtPressao.setText(ficha.getPressaoArterial());

            double imc = ficha.calcularIMC();
            txtIMC.setText(String.format("%.2f", imc));
            txtInterpretacao.setText(ficha.interpretarIMC());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarFicha(); // Recarrega os dados caso tenha sido editado
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}