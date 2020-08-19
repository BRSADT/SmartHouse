package com.example.samantha.androidclient;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static com.example.samantha.androidclient.APP.CHANNEL_1_ID;

public class SocketBackgroundService extends Service {

    int portNumber = 5234;
    boolean listening = true;
    private ExecutorService mExecutorService = null; // thread pool
    private static final String TAG = SocketBackgroundService.class.getSimpleName();
    private Thread workerThread = null;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() { //It will prepare everything for the foreground notification
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        new Thread(new Runnable() {

            @Override

            public void run() {

                try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                    while (listening) {
                        new AndroidThread(serverSocket.accept()).start();
                       notificationManager = NotificationManagerCompat.from(SocketBackgroundService.this);
                    }
                } catch (IOException e) {
                    System.err.println("Could not listen on port " + portNumber);
                }

            }

        }).start();


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //sendOnChannel are Methods to create the notification for each case

    public void sendOnChannel1() {
        String title = "AAHHHHH";
        String message = "El cielo se cae y tú casa se quema";

        Notification notification = new NotificationCompat.Builder(SocketBackgroundService.this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.firee)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(30, notification);
    }


    public void sendOnChannel2() {
        String title = "Han entrado";
        String message = "Alguien ha entrado a tu casa";
        Notification notification = new NotificationCompat.Builder(SocketBackgroundService.this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.puerta)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(35, notification);
    }


    public void sendOnChannel3() {
        String title = "Han salido";
        String message = "Una persona ha salido de tú casa";

        Notification notification = new NotificationCompat.Builder(SocketBackgroundService.this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.puerta)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(40, notification);
    }


    class AndroidThread extends Thread {

        SocketBackgroundService servi = new SocketBackgroundService();
        private NotificationManagerCompat notificationManager;
        private Socket socket = null;

        public AndroidThread(Socket socket) {
            super("AndroidThread");
            this.socket = socket;
     }

        public void run() {
            String regreso = "";
            try (    //Socket which will be hearing if JavaServer sent something
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));) {
                String inputLine;
                Notification notificacion = new Notification(

                        R.drawable.ic_launcher_background,

                        "Creando Servicio de Notificaciones",

                        System.currentTimeMillis());

                while ((inputLine = in.readLine()) != null) {
                    Log.i("MessageFromJavaS",regreso);
                    regreso = processInput(inputLine); //it will receive the alert from the server which will sent to processInput
                    if (regreso != "") {
                        out.println(regreso);//it will send back if the message was send
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public String processInput(String theInput) { //It will receive the sting of the warning and select which notification show
            String regreso = "";
            switch (theInput) {// theInput is the warning that Arduino sent.
                case "AlertaLLamas":
                    sendOnChannel1();
                    regreso = "mensajeenviado";
                    break;
                case "AlertaEntro":
                    sendOnChannel2();
                    regreso = "mensajeenviado";
                    break;
                case "AlertaSalio":
                    sendOnChannel3();
                    break;
                default:
            }
            return regreso;
        }
    }
}





