/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthouserbd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 *
 * @author BREND
 */
public class SmartHouseRBD {
ArduinoToServer er; 
JavaSQL CON;
int portNumber = 4444;
boolean listening = true;
SmartHouseRBD(){
  er=new ArduinoToServer();
        CON = new JavaSQL(er);
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                new AndroidMultiThread(serverSocket.accept(), er).start();
                System.out.println("A client has entered");
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
        }

}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        // TODO code application logic here
       
    SmartHouseRBD sh=new SmartHouseRBD();
    }
    
}
