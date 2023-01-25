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
        //Con cada nueva ejecución se vacía el textView
        textArea.setText("");
        //Primer mensaje
        textArea.append("Ejecutando Tarea Pesada: " + tiempo + "sg -->");
        try {
            //Bucle para contar el tiempo
            for(int i=0; i < tiempo ;i++) {
                //Comprueba que no se ha dado la orden de parar la ejecución
                if (arrancado) {
                    //Escribbir en textarea
                    textArea.append("..."+i);
                    Thread.sleep(1000);
                }
            }
            //Mensaje tras finalizar la tarea
            textArea.append("...Terminada tarea pesada");
            //comprueba si se ha parado la tarea manualmente
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

    //Modifica el valor del flag para parar el bucle de ejecución
    public void paraEjecucion() {
        arrancado = false;
    }
}
