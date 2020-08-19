package com.example.samantha.androidclient;

import android.app.Notification;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import static com.example.samantha.androidclient.APP.CHANNEL_1_ID;

public class accederApp extends AppCompatActivity implements Serializable {
    private Socket socket;
    StringBuilder texto = new StringBuilder();
    datosUsuario us;
    EditText inputUsuario, inputPassword;
    String textof = "";
    Button btnacceder;
    String StrUsuario = "", StrPassword = "", Instruccion = "", ip = "", enviar = "", res = "";
    TextView prueba;
    Intent intent = new Intent(this, SocketBackgroundService.class);
    SocketBackgroundService serv;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceder_app);
        us = new datosUsuario(); //us is an object from the class datosUsuario
        btnacceder = (Button) findViewById(R.id.btnAcceder);
        inputUsuario = (EditText) findViewById(R.id.InputUsuario);
        inputPassword = (EditText) findViewById(R.id.InputPass);
        prueba = (TextView) findViewById(R.id.textView3);
        startService(new Intent(accederApp.this, SocketBackgroundService.class));//It will start the background service when the user access to their app
        Log.i("Servicio", "despues");
        //When the user click the access button
        btnacceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("acceder", "despues");
                StrUsuario = inputUsuario.getText().toString();
                StrPassword = inputPassword.getText().toString();
                if (StrUsuario.length() > 0 && StrPassword.length() > 0) { //verify if textbox are not empty
                    new Hilito().execute();
                }
            }
        });


    }

    private class Hilito extends AsyncTask<Void, Void, String> implements Serializable {
        @Override
        protected String doInBackground(Void... params) {
            Conexion con = new Conexion();
            try {
                Socket AndroidSocket = new Socket(con.getHostName(), con.getPortNumber()); //We create a socket to send information to Java Server
                PrintWriter out =
                        new PrintWriter(AndroidSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(AndroidSocket.getInputStream()));
                Instruccion = "Verificacion";
                ip = AndroidSocket.getLocalAddress().toString();
                enviar = Instruccion + ";" + ip + ";" + StrUsuario + ";" + StrPassword;
                out.println(enviar); //here it sends the information to verify if the credential are right
                while ((res = in.readLine()) != null) { //Receive a response from server
                    textof = textof + res;
                    AndroidSocket.close();
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return textof;
        }

        @Override
        protected void onPostExecute(String textof) {
            Log.i("cadena", textof);
            if (textof.length() <= 4) { //if java return us a string with mora than 4 chars (;;;;) it means the info is correct.
                Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
            } else {
                String[] datos = textof.split(";");
                /*Our object us will be set with the information of the user that has access*/
                us.setAdministrador(datos[3]);
                us.setUsuario(datos[0]);
                us.setNombre(datos[1]);
                us.setApellido(datos[2]);
                if (us.getAdministrador().equals("1")) {//if the user is an administrator, it will send the user to the administrator menu
                    Intent i = new Intent(accederApp.this, menuPAdministrador.class);
                    i.putExtra("intUsuarios", (Serializable) us); //Serializable let save and share the object information through the app
                    startActivity(i);
                } else {
                    Intent i = new Intent(accederApp.this, MenuPrincipal.class);//if the user is not an administrator, it will send it to the normal menu
                    i.putExtra("intUsuarios", (Serializable) us);
                    startActivity(i);
                }
            }
        }
    }

}

