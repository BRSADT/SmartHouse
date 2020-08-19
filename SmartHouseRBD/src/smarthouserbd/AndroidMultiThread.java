/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthouserbd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author BREND
 */
public class AndroidMultiThread extends Thread {

    ArduinoToServer ar2;
    private Socket socket = null;
    String SAndroid = "";
    JavaSQL sent;
    InstructionsAnd androidSentence;
    ConnectionSH con = new ConnectionSH();

    public AndroidMultiThread(Socket socket, ArduinoToServer ar) {
        super("AndroidMultiThread");
        this.socket = socket;
        ar2 = ar;
        androidSentence = new InstructionsAnd(ar);
        sent = new JavaSQL(ar);
    }

    public void run() { //it allow multiple users to ask petitions to the server 
        try (   
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));) {
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {// Android send an instruction "SAndroid" which will be read here
                DataOutputStream aServidor = new DataOutputStream(socket.getOutputStream());
                SAndroid = androidSentence.processInput(inputLine); //the sentence from android will be sent to InstructionAnd to get back the response or update the DB if necessary
                if (SAndroid != "") {
                    out.println(SAndroid); //the answer will be sent again to Android to process the answer  
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
