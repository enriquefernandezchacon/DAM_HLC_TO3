package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class SplashActivity extends AppCompatActivity {

    private int espera;
    private String mensajeDeTareaPesada;
    private ProgressDialog progressDialog;
    private ExecutorService service;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_unicorn_launcher_round);

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        espera = ThreadLocalRandom.current().nextInt(5000, 10000);
        super.onResume();
        service = Executors.newFixedThreadPool(1);

        service.execute(() -> {
            //Con este metodo, simulamos el onPreExecute
            //Generamos los diálogos que se mostrarán al inicio
            runOnUiThread(() -> {
                progressDialog = ProgressDialog.show(SplashActivity.this,
                        "Dialogo de Progreso",
                        "Conectando al servidor...");
                Toast.makeText(SplashActivity.this, "Recibiendo datos...", Toast.LENGTH_SHORT).show();
            });

            //El código a continuación simula doInBackground();
            mensajeDeTareaPesada = tareaPesada(espera);

            //Con este metodo, simulamos el onPostExecute()
            //Se ejecutara tras la tarea pesada
            runOnUiThread(() -> {
                Toast.makeText(SplashActivity.this, mensajeDeTareaPesada, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            });
        });
    }

    private String tareaPesada(int tiempo) {
        try {
            Thread.sleep(tiempo);
            return "Dormido durante " + tiempo + " milisegundos";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Si se suspende la activity, se para la ejecución en el caso que este en marcha
        if (service != null) {
            service.shutdownNow();
        }
    }
}