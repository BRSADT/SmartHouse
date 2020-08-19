package com.example.samantha.androidclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AgregarUsuarios extends AppCompatActivity implements Serializable {
    EditText usuario, nombre, apellido, pass, relacion;
    Button agregar;
    datosUsuario us, usuarioAgregar;
    String enviar = "", Instruccion = "", ip = "", regresar = "", res = "";
    List listaPrioridad;
    Spinner spin;
    ArrayAdapter adapterPrioridad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuarios);
        us = (datosUsuario) getIntent().getSerializableExtra("intUsuarios"); //We get information by Serializable from our object us, it will have the current user information
        spin = (Spinner) findViewById(R.id.spinner);
        listaPrioridad = new ArrayList();
        listaPrioridad.add(1);
        listaPrioridad.add(2);
        listaPrioridad.add(3);
        listaPrioridad.add(4);
        adapterPrioridad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaPrioridad);
        spin.setAdapter(adapterPrioridad);
        usuario = (EditText) findViewById(R.id.inUsuario);
        nombre = (EditText) findViewById(R.id.inNombre);
        apellido = (EditText) findViewById(R.id.inApellido);
        pass = (EditText) findViewById(R.id.inApellido);
        relacion = (EditText) findViewById(R.id.inRelacion);
        agregar = (Button) findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prioridad = spin.getSelectedItem().toString();
                Instruccion = "AgregarUsuarios";
                enviar = Instruccion + ";" + ip + ";" + usuario.getText() + ";" + nombre.getText() + ";" + apellido.getText() + ";" + pass.getText() + ";" + prioridad + ";" + relacion.getText(); //It will send to server a String that is separated by delimiter ; the first part of the string is the instruction
                if (usuario.getText().length() > 0 && pass.getText().length() > 0 && nombre.getText().length() > 0 && prioridad.length() > 0 && relacion.getText().length() > 0) { //It will check if textbox are not empty to proceed
                    new AgregarUsuarios.HilitoAgregar().execute();
                }
            }
        });
    }

    private class HilitoAgregar extends AsyncTask<Void, Void, String> implements Serializable {
        Conexion con = new Conexion();

        @Override
        protected String doInBackground(Void... params) {

            try {
                Socket androidSocket = new Socket(con.getHostName(), con.getPortNumber());
                PrintWriter out =
                        new PrintWriter(androidSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(androidSocket.getInputStream()));
                ip = androidSocket.getLocalAddress().toString();
                out.println(enviar);  //send the string to Java
                while ((res = in.readLine()) != null) { //receive response
                    regresar = res;
                    androidSocket.close();
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
            Log.i("cadena", regresar);
            Toast.makeText(getApplicationContext(), regresar, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AgregarUsuarios.this, Habitantes.class);
        i.putExtra("intUsuarios", (Serializable) us);//Serializable let save and share the object information through the app
        startActivity(i);
    }
}