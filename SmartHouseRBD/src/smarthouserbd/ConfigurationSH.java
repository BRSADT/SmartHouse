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
public class ConfigurationSH {
       int ledCocina,ledSala,ventilador,ledCuarto11,ledCuarto12,ledCuarto2,tono;

    public int getLedCocina() {
        return ledCocina;
    }

    public void setLedCocina(int ledCocina) {
        this.ledCocina = ledCocina;
    }

    public int getLedSala() {
        return ledSala;
    }

    public void setLedSala(int ledSala) {
        this.ledSala = ledSala;
    }

    public int getVentilador() {
        return ventilador;
    }

    public void setVentilador(int ventilador) {
        this.ventilador = ventilador;
    }

    public int getLedCuarto11() {
        return ledCuarto11;
    }

    public void setLedCuarto11(int ledCuarto11) {
        this.ledCuarto11 = ledCuarto11;
    }

    public int getLedCuarto12() {
        return ledCuarto12;
    }

    public void setLedCuarto12(int ledCuarto12) {
        this.ledCuarto12 = ledCuarto12;
    }

    public int getLedCuarto2() {
        return ledCuarto2;
    }

    public void setLedCuarto2(int ledCuarto2) {
        this.ledCuarto2 = ledCuarto2;
    }

    public int getTono() {
        return tono;
    }

    public void setTono(int tono) {
        this.tono = tono;
    }

 
 
  
    public ConfigurationSH(int ledCocina, int ledSala, int ventilador, int ledCuarto11, int ledCuarto12, int ledCuarto2, int tono) {
        this.ledCocina = ledCocina;
        this.ledSala = ledSala;
        this.ventilador = ventilador;
        this.ledCuarto11 = ledCuarto11;
        this.ledCuarto12 = ledCuarto12;
        this.ledCuarto2 = ledCuarto2;
        this.tono = tono;
    }
}

