package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    private int espera = 4000;
    SplashActivity.AsyncTaskRunner runner;

    //
    private String resp;
    private ProgressDialog progressDialog;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ExecutorService service = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        service.execute(new Runnable() {
            @Override
            public void run() {
                //onPreExecute
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(SplashActivity.this,
                                "Dialogo de Progreso",
                                "Conectando al servidor...");
                    }
                });
                //doing background
                //publishProgress("Reciviendo datos..."); // Calls onProgressUpdate()

                Toast.makeText(SplashActivity.this, "Reciviendo datos...", Toast.LENGTH_SHORT).show();

                resp = tareaPesada(espera);
                //doPostExecute
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        //runner = new SplashActivity.AsyncTaskRunner();
        //runner.execute(String.valueOf(espera));
    }

    protected String tareaPesada(int tiempo){
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


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            publishProgress("Reciviendo datos..."); // Calls onProgressUpdate()
            resp = tareaPesada(Integer.parseInt(params[0]));
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SplashActivity.this,
                    "Dialogo de Progreso",
                    "Conectando al servidor...");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            Toast.makeText(SplashActivity.this, text[0], Toast.LENGTH_SHORT).show();
        }

        //Esta función simula una operación pesada, que se va a tomar tiempo en terminarse.
        //Devuelve un string.
        protected String tareaPesada(int tiempo){
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        runner.cancel(true);
    }
}