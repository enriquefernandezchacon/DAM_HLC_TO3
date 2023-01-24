package com.example.tareasasync;

import android.util.Log;
import android.widget.TextView;

public class RunnableClass implements Runnable{

    private TextView textArea;
    private int tiempo;
    private boolean arrancado;

    public RunnableClass(int tiempo, TextView textArea) {
        this.tiempo = tiempo;
        this.textArea = textArea;
        arrancado = true;
    }

    @Override
    public void run() {
        textArea.setText("");
        textArea.append("Ejecutando Tarea Pesada: " + tiempo + "sg -->");
        try {
            for(int i=0; i < tiempo ;i++) {
                if (arrancado) {
                    //Escribbir en textarea
                    textArea.append("..."+i);
                    Thread.sleep(1000);
                }
            }
            textArea.append("...Terminada tarea pesada");
            if (!arrancado) {
                textArea.append("...Tarea detenida por el usuario");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("TareaPesada", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TareaPesada", e.getMessage());
        }
    }

    public void paraEjecucion() {
        arrancado = false;
    }
}
