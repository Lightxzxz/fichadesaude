package com.example.fichadesaude.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fichadesaude.R;
import com.example.fichadesaude.database.FichaDbHelper;
import com.example.fichadesaude.model.FichaSaude;

public class CadastroActivity extends AppCompatActivity {
    private EditText editNome, editIdade, editPeso, editAltura, editPressao;
    private Button btnSalvar;
    private FichaDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dbHelper = new FichaDbHelper(this);

        editNome = findViewById(R.id.editNome);
        editIdade = findViewById(R.id.editIdade);
        editPeso = findViewById(R.id.editPeso);
        editAltura = findViewById(R.id.editAltura);
        editPressao = findViewById(R.id.editPressao);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarFicha();
            }
        });
    }

    private void salvarFicha() {
        String nome = editNome.getText().toString().trim();
        String idadeStr = editIdade.getText().toString().trim();
        String pesoStr = editPeso.getText().toString().trim();
        String alturaStr = editAltura.getText().toString().trim();
        String pressao = editPressao.getText().toString().trim();

        if (nome.isEmpty() || idadeStr.isEmpty() || pesoStr.isEmpty() || alturaStr.isEmpty() || pressao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int idade = Integer.parseInt(idadeStr);
            double peso = Double.parseDouble(pesoStr);
            double altura = Double.parseDouble(alturaStr);

            FichaSaude ficha = new FichaSaude(nome, idade, peso, altura, pressao);
            long id = dbHelper.addFicha(ficha);

            if (id != -1) {
                Toast.makeText(this, "Ficha salva com sucesso!", Toast.LENGTH_SHORT).show();
                limparCampos();
            } else {
                Toast.makeText(this, "Erro ao salvar ficha!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Idade, Peso e Altura devem ser números válidos!", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparCampos() {
        editNome.setText("");
        editIdade.setText("");
        editPeso.setText("");
        editAltura.setText("");
        editPressao.setText("");
        editNome.requestFocus();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}