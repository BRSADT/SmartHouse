package com.example.samantha.androidclient;

import java.io.Serializable;

public class datosUsuario  implements Serializable
{
String nombre;
String apellido;
String administrador;
String edad;
String usuario;
String contra;

    public datosUsuario() {
    }

    public datosUsuario(String nombre, String apellido, String administrador, String edad,String usuario,String contra) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.administrador = administrador;
        this.edad = edad;
        this.usuario=usuario;
        this.contra=contra;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
}
