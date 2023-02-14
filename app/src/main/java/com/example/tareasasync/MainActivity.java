package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText tiempoEdT;
    private TextView resultados;

    private Thread hilo;
    private int tiempo;
    private int iteracionBucle;
    private boolean flagTareaBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_unicorn_launcher_round);

        tiempoEdT = findViewById(R.id.in_time);
        Button btnArrancar = findViewById(R.id.btn_arrancar);
        Button btnParar = findViewById(R.id.btn_parar);
        resultados = findViewById(R.id.tv_result);

        btnArrancar.setOnClickListener(v -> {

            //Obtenemos el valor del campo de tiempoDormido
            String tiempoDormido = tiempoEdT.getText().toString();
            //Comprobamos que no este vacío
            if (tiempoDormido.matches("")) {
                Toast.makeText(MainActivity.this, "Introduce un número de segundos", Toast.LENGTH_SHORT).show();
                //Comprobamos que el hilo no este en ejecución
            } else if (hilo != null && hilo.isAlive()) {
                Toast.makeText(MainActivity.this, "El hilo ya esta en ejecución", Toast.LENGTH_SHORT).show();
                //Lanzamos el texto
            } else {
                cierraTeclado();
                //Instancio un nuevo objeto runnable
                flagTareaBackground = true;
                tiempo = Integer.parseInt(tiempoEdT.getText().toString());
                ejecutarTareaEnBackground();
            }
        });

        btnParar.setOnClickListener(v -> flagTareaBackground = false);
    }

    @Override
    protected void onPause() {
        //Si se cierra la activity, se para la ejecución
        if (hilo != null && hilo.isAlive()) {
            flagTareaBackground = false;
        }
        super.onPause();
    }

    protected void cierraTeclado() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void ejecutarTareaEnBackground() {
        hilo = new Thread(() -> {
            try {
                runOnUiThread(() -> {
                    //Mensaje tras finalizar la tarea
                    resultados.setText("");
                    resultados.append("Iniciando Tarea Pesada");
                });
                //Bucle para contar el tiempo
                for (int i = 1; i <= tiempo; i++) {
                    iteracionBucle = i;
                    //Comprueba que no se ha dado la orden de parar la ejecución
                    if (flagTareaBackground) {
                        //Escribbir en textarea
                        runOnUiThread(() -> resultados.append("..." + iteracionBucle));

                        Thread.sleep(1000);
                    }
                }

                runOnUiThread(() -> resultados.append("...Terminada tarea pesada"));

                //comprueba si se ha parado la tarea manualmente
                if (!flagTareaBackground) {
                    runOnUiThread(() -> resultados.append("...Tarea detenida por el usuario"));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("TareaPesada", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TareaPesada", e.getMessage());
            }

        });

        hilo.start();
    }

}



