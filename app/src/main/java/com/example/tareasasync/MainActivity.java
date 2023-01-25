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
                //Obtenemos el valor del campo de tiempoDormido
                String tiempoDormido = tiempoEdT.getText().toString();
                //Comprobamos que no este vacío
                if (tiempoDormido.matches("")) {
                    Toast.makeText(MainActivity.this, "Introduce un número de segundos", Toast.LENGTH_SHORT).show();
                    return;
                //Comprobamos que el hilo no este en ejecución
                } else if (hilo != null && hilo.isAlive()) {
                    Toast.makeText(MainActivity.this, "El hilo ya esta en ejecución", Toast.LENGTH_SHORT).show();
                    return;
                //Lanzamos el texto
                } else {
                    cierraTeclado();
                    //Instancio un nuevo objeto runnable
                    tareaPesada = new RunnableClass(Integer.parseInt(tiempoDormido), resultados);
                    //Asigno el runnable a un nuevo hilo
                    hilo = new Thread(tareaPesada);
                    //Arranco el hilo
                    hilo.start();
                }
            }
        });

        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Compruebo si el hilo esta en ejecución para pararlo
                if (hilo != null && hilo.isAlive()) {
                    tareaPesada.paraEjecucion();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        //Si se cierra la activity, se para la ejecución
        if (hilo != null && hilo.isAlive()) {
            hilo.interrupt();
        }
        super.onPause();
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



