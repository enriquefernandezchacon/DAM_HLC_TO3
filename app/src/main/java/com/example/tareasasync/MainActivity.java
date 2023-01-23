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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiempoEdT = (EditText) findViewById(R.id.in_time);
        btnArrancar = (Button) findViewById(R.id.btn_arrancar);
        btnParar = (Button) findViewById(R.id.btn_parar);
        resultados = (TextView) findViewById(R.id.tv_result);

        btnArrancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultados.setText("");
                String tiempoDormido = tiempoEdT.getText().toString();
                if (tiempoDormido.matches("")) {
                    Toast.makeText(MainActivity.this, "Introduce un número de segundos", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    cierraTeclado();
                    resultados.append("Ejecutando Tarea pesada: "+tiempoDormido+" sg --> ");
                    tareaPesada(Integer.parseInt(tiempoDormido),resultados);
                    resultados.append("Terminada tarea pesada \n");
                }
            }
        });
    }


    //Esta función simula una operación pesada, que se va a tomar tiempo en terminarse.
    //Devuelve un string.
    protected void tareaPesada(int tiempo,TextView result){
        try {
            for(int i=0; i < tiempo ;i++) {
                result.append("..."+i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("TareaPesada", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TareaPesada", e.getMessage());
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



