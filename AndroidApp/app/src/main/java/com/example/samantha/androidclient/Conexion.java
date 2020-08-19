package com.example.samantha.androidclient;

import java.io.Serializable;

public class Conexion implements Serializable {
    int portNumber;
    String hostName;
   Conexion(){
        portNumber=4444;
        hostName="192.168.1.72"; //Java Server IP
    }
    public int getPortNumber() {
        return portNumber;
    }
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
