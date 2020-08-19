/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthouserbd;

import java.net.Socket;

/**
 *
 * @author BREND
 */
public class InstructionsAnd {

    ArduinoToServer ar2;
    JavaSQL CON;
    public String regreso = "";
    private String nombre = "";
    private String contra = "";
    private String ip = "";
    private String usuario = "";
    private String apellido = "";
    private String administrador = "";
    JavaSQL sent;

    InstructionsAnd(ArduinoToServer ar) {
        ar2 = ar;
        sent = new JavaSQL(ar2);
    }

    public String processInput(String theInput) {
       

        String parts[] = theInput.split(";"); //Java Will receive information about the  separated by ;   
        switch (parts[0]) { //parts[0] has the label of the instruction 

            case "Verificacion": 
                ip = parts[1]; //parts[1] has the ip 
                ip = ip.substring(1);
                ar2.ipArr.add(ip);//When a user is verified, the server will save the ip.
                nombre = parts[2]; //parts[2] has  the name
                contra=parts[3];// parts[3] has the password
                this.regreso = sent.verificarUsuario(nombre,contra);//It will send the information to a method in JavaSQL to verify if user exist
                break;
            case "AgregarUsuarios":
                usuario = parts[2];
                nombre = parts[3];
                apellido = parts[4];
                contra = parts[5];
               
                this.regreso = sent.AgregarUsuario(usuario, nombre, apellido, contra, parts[6], parts[7]);
                break;
          
            case "EliminarHabitante":
                this.regreso = sent.EliminarUsuario(parts[1]);//each occupant has an unique ID so it will delete the user with this ID
                 break;
            case "Habitantes":
                this.regreso = sent.TodosHabitantes(); //It will receive a string were each ocuppant is separated with ; and the information of each ocuppant will be separated by - 
                break;
            case "Historial":
                this.regreso = sent.TodoHistorial();
                break;
            case "Configuracion":
                
                int ventilador,
                 cocina,
                 sala,
                 foco1,
                 foco2,
                 focoCuarto2,
                 tono = 1;
                usuario = parts[1];
                if (parts[2].equals("true")) {
                    ventilador = 1;
                } else {
                    ventilador = 0;
                }
                if (parts[3].equals("true")) {
                    cocina = 1;
                } else {
                    cocina = 0;
                }

                if (parts[4].equals("true")) {
                    sala = 1;
                } else {
                    sala = 0;
                }

                if (parts[5].equals("true")) {
                    foco1 = 1;
                } else {
                    foco1 = 0;
                }

                if (parts[6].equals("true")) {
                    foco2 = 1;
                } else {
                    foco2 = 0;
                }

                if (parts[7].equals("true")) {
                    focoCuarto2 = 1;
                } else {
                    focoCuarto2 = 0;
                }
                if (parts[8].equals("Tono1")) {
                    tono = 1;
                }
                if (parts[8].equals("Tono2")) {
                    tono = 2;
                }
                if (parts[8].equals("Tono3")) {
                    tono = 3;
                }
                this.regreso = sent.Configuracion(usuario, ventilador, cocina, sala, foco1, foco2, focoCuarto2, tono);
 
                break;
            default:

        }

        return this.regreso;
    }
}
