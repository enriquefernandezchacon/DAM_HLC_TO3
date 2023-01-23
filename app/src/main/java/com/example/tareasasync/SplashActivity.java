package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private int espera = 4000;
    SplashActivity.AsyncTaskRunner runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        runner = new SplashActivity.AsyncTaskRunner();
        runner.execute(String.valueOf(espera));
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