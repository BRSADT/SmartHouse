package com.example.samantha.androidclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionPersonalizada extends AppCompatActivity implements Serializable {
    Switch ventilador, cocina, sala, foco1, foco2, focoCuarto2;
    Button guardar;
    String enviar = "", Instruccion = "", ip = "", regresar = "", res = "";

    Spinner spin;
    ArrayAdapter adapterTono;
    List listaTonos;
    datosUsuario us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_personalizada);
        us = (datosUsuario) getIntent().getSerializableExtra("intUsuarios"); //current user data
        spin = (Spinner) findViewById(R.id.spinnerTonos);
        listaTonos = new ArrayList();
        listaTonos.add("Tono1");
        listaTonos.add("Tono2");
        listaTonos.add("Tono3");
        adapterTono = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaTonos);
        spin.setAdapter(adapterTono);
        spin.setSelection(0);
        ventilador = (Switch) findViewById(R.id.swVentilador);
        cocina = (Switch) findViewById(R.id.swCocina);
        sala = (Switch) findViewById(R.id.swSala);
        foco1 = (Switch) findViewById(R.id.swFoco1);
        foco2 = (Switch) findViewById(R.id.swFoco2);
        focoCuarto2 = (Switch) findViewById(R.id.swFocoCuarto2);
        guardar = (Button) findViewById(R.id.btnGuardarConf);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enviar will be a string with the information of the user and the selected configuration
                enviar = "Configuracion" + ";" + String.valueOf(us.usuario) + ";" + String.valueOf(ventilador.isChecked()) + ";" + String.valueOf(cocina.isChecked()) + ";" + String.valueOf(sala.isChecked()) + ";" + String.valueOf(foco1.isChecked() + ";" + String.valueOf(foco2.isChecked() + ";" + String.valueOf(focoCuarto2.isChecked())) + ";" + String.valueOf(spin.getSelectedItem()));
                new ConfiguracionPersonalizada.HilitoAgregar().execute();
            }
        });

    }

    private class HilitoAgregar extends AsyncTask<Void, Void, String> implements Serializable {
        Conexion con = new Conexion();

        @Override
        protected String doInBackground(Void... params) {

            try {
                Socket AndroidSocket = new Socket(con.getHostName(), con.getPortNumber());
                PrintWriter out =
                        new PrintWriter(AndroidSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(AndroidSocket.getInputStream()));
                out.println(enviar);  //send to java
                while ((res = in.readLine()) != null) { //receive the response from java
                    regresar = res;
                    AndroidSocket.close();
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return regresar;
        }

        @Override
        protected void onPostExecute(String textof) {
            if (us.getAdministrador().equals("1")) {
                Intent i = new Intent(ConfiguracionPersonalizada.this, menuPAdministrador.class);
                i.putExtra("intUsuarios", (Serializable) us);//Serializable let save and share the object information through the app
                startActivity(i);
            } else {
                Intent i = new Intent(ConfiguracionPersonalizada.this, MenuPrincipal.class);
                i.putExtra("intUsuarios", (Serializable) us);//Serializable let save and share the object information through the app
                startActivity(i);

            }
        }
    }


    @Override
    public void onBackPressed() {
        if (us.getAdministrador().equals("1")) {
            Intent i = new Intent(ConfiguracionPersonalizada.this, menuPAdministrador.class);
            i.putExtra("intUsuarios", (Serializable) us);//Serializable let save and share the object information through the app
            startActivity(i);
        } else {
            Intent i = new Intent(ConfiguracionPersonalizada.this, MenuPrincipal.class);
            i.putExtra("intUsuarios", (Serializable) us);//Serializable let save and share the object information through the app
            startActivity(i);
        }
    }
}

