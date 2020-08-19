/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthouserbd;

/**
 *
 * @author BREND
 */
public class ConnectionSH {
     int portNumber;
    String IP;

    ConnectionSH(){
        portNumber=4444;
        IP="192.168.1.72";
    }
        ConnectionSH(int port,String name){
        portNumber=port;
        IP=name;
    }
    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String hostName) {
        this.IP = hostName;
    }
}
