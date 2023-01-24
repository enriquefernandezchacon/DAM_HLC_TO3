package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText tiempoEdT;
    private Button btnArrancar;
    private Button btnParar;
    private TextView resultados;
    private RunnableClass tareaPesada;

    private Thread hilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_unicorn_launcher_round);

        tiempoEdT = (EditText) findViewById(R.id.in_time);
        btnArrancar = (Button) findViewById(R.id.btn_arrancar);
        btnParar = (Button) findViewById(R.id.btn_parar);
        resultados = (TextView) findViewById(R.id.tv_result);

        btnArrancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tiempoDormido = tiempoEdT.getText().toString();
                if (tiempoDormido.matches("")) {
                    Toast.makeText(MainActivity.this, "Introduce un número de segundos", Toast.LENGTH_SHORT).show();
                    return;
                } else if (hilo != null && hilo.isAlive()) {
                    Toast.makeText(MainActivity.this, "El hilo ya esta en ejecución", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    resultados.setText("");
                    cierraTeclado();
                    tareaPesada = new RunnableClass(Integer.parseInt(tiempoDormido), resultados);
                    hilo = new Thread(tareaPesada);
                    hilo.start();
                }
            }
        });

        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hilo != null && hilo.isAlive()) {
                    tareaPesada.paraEjecucion();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hilo != null && hilo.isAlive()) {
            hilo.interrupt();
            Toast.makeText(this, "Ejecución parada", Toast.LENGTH_SHORT).show();
        }
    }

    protected void cierraTeclado(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}



