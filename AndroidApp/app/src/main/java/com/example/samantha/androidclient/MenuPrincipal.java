package com.example.samantha.androidclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MenuPrincipal extends AppCompatActivity   implements Serializable {
    TextView NombreUs;
    Button btnAgregar,btnHistorial,btnConfiguracion;
    datosUsuario us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_padministrador);
        NombreUs=(TextView) findViewById(R.id.NombreUS);
        us= (datosUsuario) getIntent().getSerializableExtra("intUsuarios"); //getIntent give us the information of the actual user in the app.
        NombreUs.setText(us.nombre);
        btnAgregar=(Button)findViewById(R.id.btnAgregar);
        btnHistorial=(Button) findViewById(R.id.btnHistorial);
        btnAgregar=(Button)findViewById(R.id.btnAgregar);
        btnConfiguracion=(Button)findViewById(R.id.CongPer);
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuPrincipal.this, ConfiguracionPersonalizada.class);
                i.putExtra("intUsuarios", (Serializable) us);//Serializable let save and share the object information through the app
                startActivity(i);
            }});




        btnHistorial.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //if the button "Historial" is clicked, it will go to the Historial Menu
                Intent i = new Intent(MenuPrincipal.this, Historial.class);
                i.putExtra("intUsuarios", (Serializable) us);//Serializable let save and share the object information through the app
                startActivity(i);

            }
        });

    }
    @Override
    public void onBackPressed() {
     Intent i = new Intent(MenuPrincipal.this, accederApp.class);
        startActivity(i);
    }
}
