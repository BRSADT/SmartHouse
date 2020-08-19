/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthouserbd;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author BREND
 */
public class ArduinoToServer {

    private ServerSocket server = null;
    int portNumber = 5234; //port server opened in Android
    boolean listening = true;
    public ArduinoToServer ard;
    JavaSQL sent;
    ArrayList<String> ipArr = new ArrayList();
    ConnectionSH con = new ConnectionSH();
    static PanamaHitek_Arduino ino = new PanamaHitek_Arduino();
    private SerialPortEventListener listener = new SerialPortEventListener() { //Event Listener  of the serial port
        @Override
        public void serialEvent(SerialPortEvent spe) {
            Socket socketToAndroid;
            try {
                if (ino.isMessageAvailable()) { //Every byte  has been received from arduino
                    String opcion = ino.printMessage();// Arduino will send a sentence with instruction and information separated by (;) delimiter 
                    System.out.println("Arduino Message-"+opcion);
                    String partsOfTheInstruction[] = opcion.split(";");//partsOfTheInstruction will be an array which the first element is the instruction  
                    /*Notification Warnings*/

                    for (String ip : ipArr) { //the instruction (Tag Warning) will be sent to each user by socket
                        try {
                            socketToAndroid = new Socket(ip, 5234);
                            PrintWriter out
                                    = new PrintWriter(socketToAndroid.getOutputStream(), true);
                            BufferedReader in
                                    = new BufferedReader(
                                            new InputStreamReader(socketToAndroid.getInputStream()));
                            switch (partsOfTheInstruction[0]) {
                                case "LLAMA":
                                    out.println("AlertaLLamas");//Java Send To Android the Tag "WarningFlames" 
                                    break;
                                case "Entro":
                                    out.println("AlertaEntro"); //Java Will send a Warning if someone enter the House
                                    String re = sent.PreferenciasUsuario(partsOfTheInstruction[1]); //Update the DB if someone enter the house and return the preferences to send it back to arduino 
                                    String con[] = re.split(";");
                                    if (!String.valueOf(con[0]).equals("@")) {
                                        String persona = "";
                                        if (Integer.parseInt(con[0]) == 1) {
                                            persona = "LC";
                                        }
                                        if (Integer.parseInt(con[1]) == 1) {
                                            persona += "11";
                                        }
                                        if (Integer.parseInt(con[2]) == 1) {
                                            persona += "12";
                                        }
                                        if (Integer.parseInt(con[3]) == 1) {
                                            persona += "C2";
                                        }
                                        if (Integer.parseInt(con[4]) == 1) {
                                            persona += "LS";
                                        }
                                        if (Integer.parseInt(con[6]) == 1) {
                                            persona += "VE";
                                        }
                                        switch (Integer.parseInt(con[5])) {

                                            case 1:
                                                persona += "T1";
                                                break;

                                            case 2:
                                                persona += "T2";
                                                break;

                                            case 3:
                                                persona += "T3";
                                                break;
                                            default:
                                                persona += "T0";

                                        }
                                        if (!persona.isEmpty()) {
                                            ino.sendData(persona);

                                            persona = "";
                                            re = "";
                                            System.out.println("se enviara");
                                            partsOfTheInstruction[0] = "";
                                        }
                                    }
                                    break;
                                case "Salio":
                                    sent.salio();
                                    out.println("AlertaSalio");
                                    break;
                            }
                        } catch (UnknownHostException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        socketToAndroid = null;
                    }
                    partsOfTheInstruction[0] = "";
                }
            } catch (SerialPortException | ArduinoException ex) {
                Logger.getLogger(ArduinoToServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    ArduinoToServer() {
        sent = new JavaSQL(ard);
        try {
            ino.arduinoRXTX("COM3", 9600, listener); //Arduino and Java will communicate by serial communication 
            System.out.println("escuchando arduino");
        } catch (ArduinoException ex) {
            System.out.println("error al escuchar Arduino");
            Logger.getLogger(ArduinoToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
