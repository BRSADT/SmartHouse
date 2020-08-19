/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthouserbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

/**
 *
 * @author BREND
 */
public class JavaSQL {

    int contadorpersonas = 0;
    ArduinoToServer arduino;
    ArrayList<String> x = new ArrayList();
    ConfigurationSH confi;
    Connection conect = null;
    public Connection con;
    String Usuario = "", Nombre = "", Admin = "", Apellidos = "";
    int conta = 0;

    JavaSQL(ArduinoToServer ar) {
        arduino = ar;
    }

    public void Conectar() {
        String a;
        String b;
        String c;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException x) {
        }

        try {

            a = "jdbc:mysql://localhost/DulceHogar";
            b = "root";
            c = "";
            con = DriverManager.getConnection(a, b, c);
        } catch (SQLException ex) {
            System.out.println("Error.");
        }

    }

    String verificarUsuario(String nombre, String contra) { //It will verify on the DB if the credentials(User and Password) exist
        String resultado = "";
        try {
            Conectar();
            PreparedStatement stmt;
            ResultSet rs;
            stmt = con.prepareStatement("SELECT `usuario`, `pass`, `administrador`, `nombre`,`Apellidos` FROM `Usuarios` WHERE `usuario`=? && pass=?");
            stmt.setString(1, nombre);
            stmt.setString(2, contra);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario = rs.getString("usuario");
                Nombre = rs.getString("nombre");
                Apellidos = rs.getString("Apellidos");
                Admin = rs.getString("administrador");
            }
            if (Usuario.length() > 0) {
                resultado = Usuario + ";" + Nombre + ";" + Apellidos + ";" + Admin + ";";
                System.out.println("resultado");

            } else {
                resultado = ";;;;";
                System.out.println("no encontro el resultado");
            };
            con.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return resultado;// It will send ;;;; if user doesn't exist

    }

    String AgregarUsuario(String usuario, String nombre, String apellido, String contra, String Prioridad, String Relacion) { //It will add a user in the DB
        String resultado = "";
        try {
            if (Verificarusuario(usuario) > 0) {
                resultado = "Ya existe el usuario";
            } else {
                Conectar();

                PreparedStatement stmt;
                ResultSet rs;

                stmt = con.prepareStatement("insert into  usuarios (usuario,pass,administrador,nombre,Apellidos,Prioridad,Relacion) VALUES  (?,?,?,?,?,?,?)");
                stmt.setString(1, usuario);
                stmt.setString(2, contra);
                stmt.setString(3, "0");
                stmt.setString(4, nombre);
                stmt.setString(5, apellido);
                stmt.setString(6, Prioridad);
                stmt.setString(7, Relacion);

                stmt.executeUpdate();
                stmt = con.prepareStatement("insert into  configuracion (usuarios_configuracion,ledCocina,ledSala,ventilador,ledCuarto11,ledCuarto12,ledCuarto2,tono) VALUES  (?,?,?,?,?,?,?,?)");
                stmt.setString(1, usuario);
                stmt.setInt(2, 0);
                stmt.setInt(3, 0);
                stmt.setInt(4, 0);
                stmt.setInt(5, 0);
                stmt.setInt(6, 0);
                stmt.setInt(7, 0);
                stmt.setInt(8, 0);
                stmt.executeUpdate();
                con.close();
                resultado = "Agregado";
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return resultado;
    }

    public int Verificarusuario(String usuario) { //It will Verify if a user exist (without the password)

        try {
            Conectar();
            PreparedStatement stmt;
            ResultSet rs;
            stmt = con.prepareStatement("SELECT `usuario`,count(*) FROM `Usuarios`  WHERE  usuario =? GROUP BY `usuario`");
            stmt.setString(1, usuario);
            rs = stmt.executeQuery();
            while (rs.next()) {
                conta++;
            }
            System.out.println("existe" + conta);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return conta;
    }

    String TodoHistorial() { //It will get all the Information of the occupant who have entered  to the house and separated by multiples delimiters

        String nombres = "", apellidos = "", codigos = "", fechas = "", horas = "", apetemp = "", nombretemp = "";
        String resultado = "";
        try {
            System.out.println("a eliminar");
            Conectar();
            PreparedStatement stmt;
            ResultSet rs;
            stmt = con.prepareStatement("SELECT Codigo,Hora,Fecha,TipodeAcceso,Nombre,Apellidos FROM historial Left join usuarios on usuarios.usuario=codigo");
            rs = stmt.executeQuery();
            while (rs.next()) {
                codigos += rs.getString("Codigo");
                codigos += "@";
                try {
                    nombretemp = rs.getString("Codigo");
                    nombretemp += "@";
                    if (rs.getString("Nombre") != null) {
                        nombretemp = rs.getString("Nombre");
                        nombretemp += "@";
                    }
                } catch (Exception e) {
                }
                nombres += nombretemp;
                try {
                    apetemp = rs.getString("Codigo");
                    apetemp += "@";
                    if (rs.getString("Apellidos") != null) {
                        apetemp = rs.getString("Apellidos");
                        apetemp += "@";
                    }
                } catch (Exception e) {

                }
                apellidos += apetemp;

                fechas += rs.getString("Fecha");
                fechas += "@";
                horas += rs.getString("Hora");
                horas += "@";
            }
            if (nombres.length() > 0) {
                resultado = nombres + ";" + apellidos + ";" + codigos + ";" + fechas + ";" + horas + ";" + contadorpersonas;
                System.out.println("resultado");
                System.out.println(resultado);
            } else {
                resultado = ";;";
                System.out.println("no encontro el resultado");
            };
            con.close();

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        System.out.println("resultado=" + resultado);
        return resultado;

    }

    String TodosHabitantes() { //It will get the information of every occupant in the house

        String nombres = "", apellidos = "", codigos = "";
        String resultado = "";
        try {
            Conectar();
            PreparedStatement stmt;
            ResultSet rs;
            stmt = con.prepareStatement("SELECT `usuario`,`nombre` ,`Apellidos`FROM `usuarios`");
            rs = stmt.executeQuery();
            while (rs.next()) {
                nombres = nombres + rs.getString("nombre");
                nombres += "-";
                apellidos += rs.getString("Apellidos");
                apellidos += "-";
                codigos += rs.getString("usuario");
                codigos += "-";
            }
            if (nombres.length() > 0) {
                resultado = nombres + ";" + apellidos + ";" + codigos;
                System.out.println("resultado");

            } else {
                resultado = ";;";
                System.out.println("no encontro el resultado");
            };

            con.close();

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        System.out.println("resultado=" + resultado);
        return resultado;

    }

    String EliminarUsuario(String codigo) {
        String resultado = "";
        String nombres = "", apellidos = "", codigos = "";
        try {
            Conectar();
            PreparedStatement stmt;
            ResultSet rs;
            System.out.println("se eliminara" + codigo);
            stmt = con.prepareStatement("delete from  usuarios Where usuario=?");
            stmt.setString(1, codigo);
            stmt.executeUpdate();
            resultado = "Eliminado";
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        try {
            PreparedStatement stmt;
            ResultSet rs;

            stmt = con.prepareStatement("SELECT `usuario`,`nombre` ,`Apellidos`FROM `usuarios`");

            rs = stmt.executeQuery();
            while (rs.next()) {
                nombres = nombres + rs.getString("nombre");
                nombres += "-";
                apellidos += rs.getString("Apellidos");
                apellidos += "-";
                codigos += rs.getString("usuario");
                codigos += "-";
            }
            if (nombres.length() > 0) {
                resultado = nombres + ";" + apellidos + ";" + codigos;
            } else {
                resultado = ";;";
            };

            con.close();

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return resultado;

    }

    public String Configuracion(String codigo, int ventilador, int cocina, int sala, int foco1, int foco2, int focoCuarto2, int tono) {
        String resultado = "";
        confi = null;
        try {

            Conectar();
            PreparedStatement stmt;
            ResultSet rs;

            stmt = con.prepareStatement("update configuracion set ledCocina=?,ledSala=?,ventilador=?,ledCuarto11=?,ledCuarto12=?,ledCuarto2=?,tono=? where usuarios_configuracion=?");
            stmt.setInt(1, cocina);
            stmt.setInt(2, sala);
            stmt.setInt(3, ventilador);
            stmt.setInt(4, foco1);
            stmt.setInt(5, foco2);
            stmt.setInt(6, focoCuarto2);
            stmt.setInt(7, tono);
            stmt.setString(8, codigo);
            stmt.executeUpdate();
            con.close();
            resultado = "Agregado";
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return resultado;
    }

    public void salio() {
        if (contadorpersonas > 0) {
            contadorpersonas--;
        }
        Conectar();
        try {
            PreparedStatement stmt;
            ResultSet rs;
            Formatter fmt = new Formatter();
            Calendar cal = Calendar.getInstance();
            fmt = new Formatter();
            fmt.format("%tl:%tM:%tS", cal, cal, cal);
            System.out.println(fmt);//hora

            long time = System.currentTimeMillis();

            java.sql.Date date = new java.sql.Date(time);
            System.out.println(date); //fecha
            stmt = con.prepareStatement("insert into  historial (Codigo,Hora,Fecha,TipodeAcceso) VALUES  (?,?,?,?)");
            stmt.setString(1, "Persona");
            stmt.setString(2, fmt.toString());
            stmt.setString(3, date.toString());
            stmt.setInt(4, 1);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

    String PreferenciasUsuario(String Codigo) { //This method will return the preferences of the user and insert into the DB if someone enter the house 
        String resultado = "";
        try {
            System.out.println("persona que entro " + Codigo);
            contadorpersonas++;
            Conectar();

            PreparedStatement stmt;
            ResultSet rs;

            stmt = con.prepareStatement("SELECT `ledCocina`, `ledSala`, `ventilador`, `ledCuarto11`,`ledCuarto12` ,`ledCuarto2` , `tono` FROM `Configuracion` WHERE `usuarios_configuracion`=?");
            stmt.setString(1, Codigo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                confi = new ConfigurationSH(rs.getInt("ledCocina"), rs.getInt("ledSala"), rs.getInt("ventilador"), rs.getInt("ledCuarto11"), rs.getInt("ledCuarto12"), rs.getInt("ledCuarto2"), rs.getInt("tono"));
            }
            if (confi != null) {
                resultado = String.valueOf(confi.ledCocina) + ";" + String.valueOf(confi.ledCuarto11) + ";" + String.valueOf(confi.ledCuarto12) + ";" + String.valueOf(confi.ledCuarto2) + ";" + String.valueOf(confi.ledSala) + ";" + String.valueOf(confi.tono) + ";" + String.valueOf(confi.ventilador);
                Formatter fmt = new Formatter();
                Calendar cal = Calendar.getInstance();
                fmt = new Formatter();
                fmt.format("%tl:%tM:%tS", cal, cal, cal);
                System.out.println(fmt);//hora
                long time = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(time);
                stmt = con.prepareStatement("insert into  historial (Codigo,Hora,Fecha,TipodeAcceso) VALUES  (?,?,?,?)");
                stmt.setString(1, Codigo);
                stmt.setString(2, fmt.toString());
                stmt.setString(3, date.toString());
                stmt.setInt(4, 1);
                stmt.executeUpdate();

            } else {
                Formatter fmt = new Formatter();
                Calendar cal = Calendar.getInstance();
                fmt = new Formatter();
                fmt.format("%tl:%tM:%tS", cal, cal, cal);
                //hora
                long time = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(time);
                System.out.println(date); //fecha
                stmt = con.prepareStatement("insert into  historial (Codigo,Hora,Fecha,TipodeAcceso) VALUES  (?,?,?,?)");
                stmt.setString(1, "invitado");
                stmt.setString(2, fmt.toString());
                stmt.setString(3, date.toString());
                stmt.setInt(4, 1);

                stmt.executeUpdate();

                resultado = "@;;;;";
                System.out.println("no encontro el resultado");
            };

            con.close();

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        System.out.println("resultado=" + resultado);
        return resultado;

    }
}
