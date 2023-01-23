package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    private int espera = 4000;
    private String mensajeDeTareaPesada;
    private ProgressDialog progressDialog;
    private ExecutorService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        service = Executors.newSingleThreadExecutor();

        service.execute(() -> {
            //Con este metodo, simulamos el onPreExecute
            runOnUiThread(() -> {
                progressDialog = ProgressDialog.show(SplashActivity.this,
                        "Dialogo de Progreso",
                        "Conectando al servidor...");
                Toast.makeText(SplashActivity.this, "Recibiendo datos...", Toast.LENGTH_SHORT).show();
            });

            //El código a continuación simula doInBackground
            mensajeDeTareaPesada = tareaPesada(espera);

            //Con este metodo, simulamos el onPostExecute
            runOnUiThread(() -> {
                Toast.makeText(SplashActivity.this, mensajeDeTareaPesada, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            });
        });
    }

    private String tareaPesada(int tiempo){
        try {
            Thread.sleep(tiempo);
            return "Dormido durante " + tiempo + " seconds";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
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