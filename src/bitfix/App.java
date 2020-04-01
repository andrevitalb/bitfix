package bitfix;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import net.proteanit.sql.DbUtils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.HashMap;
import java.util.logging.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class App extends javax.swing.JFrame {
    
    int numeroEmpleados = 0, numeroClientes = 0, last_Cliente = 0, last_Reporte = 0, comp = 0, serv = 1, total_Empleados = 0;
    String[] nombreEmpleados = new String[15];
    String[] nombreClientes = new String[4000];
    String servicios = "", anticipo = "";
    boolean s = false;
    Border defaultBorder;
    
      
    public static void main(String args[]) {   
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);      
            }
        }); 
    }
    
    private void cargar(){
        Timer contador = new Timer();
        
        contador.schedule(new TimerTask (){
            
            @Override
            public void run() {
                load_Screen.setVisible(false);
                sidebar.setVisible(true);
                main_Screen1.setVisible(true);
            }
        },6000);
    }
    
    private void setIcon(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("LogoApp.png")));
    }

    public App() {
        initComponents();
        
        setIcon();
        
        defaultBorder = txtfield_Precio.getBorder();
        
        load_Screen.setVisible(true);
        sidebar.setVisible(false);
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
        screen_Servicios6.setVisible(false);
                               
        modelComboEmpleados(combo_Empleados);
        modelComboEmpleados(combo_Empleados3);
        modelComboEmpleados(combo_Empleados7);
        modelComboClientes(combo_Clientes);
        modelComboClientes(combo_Clientes3);
        modelComboClientes(combo_Clientes5);
        modelComboClientes(combo_Clientes6);
        modelComboClientes(combo_Clientes8);
        
        AutoCompleteDecorator.decorate(combo_Empleados);
        AutoCompleteDecorator.decorate(combo_Empleados3);
        AutoCompleteDecorator.decorate(combo_Empleados7);
        AutoCompleteDecorator.decorate(combo_Clientes);
        AutoCompleteDecorator.decorate(combo_Clientes3);
        AutoCompleteDecorator.decorate(combo_Clientes5);
        AutoCompleteDecorator.decorate(combo_Clientes6);
        AutoCompleteDecorator.decorate(combo_Clientes8);
        AutoCompleteDecorator.decorate(combo_Equipo);
        AutoCompleteDecorator.decorate(combo_Tipo3);
        AutoCompleteDecorator.decorate(combo_Tipo5);
        AutoCompleteDecorator.decorate(combo_Ciudad);
        AutoCompleteDecorator.decorate(combo_Servicios);
        AutoCompleteDecorator.decorate(combo_Servicios1_1);
        AutoCompleteDecorator.decorate(combo_Servicios1_2);
        AutoCompleteDecorator.decorate(combo_Servicios1_3);
        AutoCompleteDecorator.decorate(combo_Servicios1_4);
        
        txtfield_Anticipo.setEnabled(false);
        txtfield_Anticipo.setBackground(new Color(204, 204, 204));
        
        combo_Servicios1_3.setSelectedIndex(-1);
        combo_Servicios1_4.setSelectedIndex(-1);
        
        cargar();
        
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
                      
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(ID_Reportes) as ID FROM Reportes");
            
            while(rs.next()){
            
                last_Reporte = rs.getInt("ID");
            
            }
            
            last_Reporte--;
            
        }catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
         
    private void get_Reportes() {
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            String sql = "Select R.ID_Reportes as ID,\n" +
                                            "R.fecha_Reportes as Fecha,\n" +
                                            "C.nombre_Clientes as Cliente,\n" +
                                            "CONCAT(R.tipo_Equipo, ' ', R.marca_Equipo) as Equipo,\n" +
                                            "R.fallas_Equipo as Fallas,\n" +
                                            "R.observaciones_Equipo as Obs,\n" +
                                            "R.servicios_Equipo as Servicios,\n" +
                                            "(R.precio_Reporte + R.precio2_Reporte + R.precio3_Reporte + R.precio4_Reporte) as Precio\n" +
                                            "from Reportes as R\n" + 
                                            "inner join Clientes as C\n" +
                                            "on R.cliente_Reportes = C.ID_Clientes";
            
            
            
            if(!(combo_Empleados3.getSelectedIndex() == -1) || !(combo_Clientes3.getSelectedIndex() == -1) || !(combo_Tipo3.getSelectedIndex() == -1) || !(txtfield_Marca3.getText().isEmpty()) || !(txtfield_ID3.getText().isEmpty())){
                
                int cont = 0;
                
                sql += "\nwhere ";
                
                if(!(combo_Empleados3.getSelectedIndex() == -1)){cont++;}
                if(!(combo_Clientes3.getSelectedIndex() == -1)){cont++;}
                if(!(combo_Tipo3.getSelectedIndex() == -1)){cont++;}
                if(!(txtfield_Marca3.getText().isEmpty())){cont++;}
                if(!(txtfield_ID3.getText().isEmpty())){cont++;}
                
                if(!(combo_Empleados3.getSelectedIndex() == -1)){
                    sql += "empleado_Reporte = " + (combo_Empleados3.getSelectedIndex() + 1);
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(combo_Clientes3.getSelectedIndex() == -1)){                                      
                    sql += "cliente_Reportes = " + get_clienteIndex(combo_Clientes3);
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(combo_Tipo3.getSelectedIndex() == -1)){
                    sql += "tipo_Equipo = '" + combo_Tipo3.getSelectedItem() + "'";
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(txtfield_Marca3.getText().isEmpty())){
                    sql += "marca_Equipo = '" + txtfield_Marca3.getText() + "'";
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(txtfield_ID3.getText().isEmpty())){
                    sql += "ID_Reportes = " + txtfield_ID3.getText();
                }
            }
            
            sql += " order by ID_Reportes desc";
                        
            PreparedStatement stmt = con.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery(sql);
            
            table_Reportes.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void get_Solution() {
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            String sql = "Select R.ID_Reportes as ID,\n" + 
                            "R.fecha_Reportes as Fecha,\n" +
                            "C.nombre_Clientes as Cliente,\n" +
                            "CONCAT(R.tipo_Equipo, ' ', R.marca_Equipo) as Equipo,\n" +
                            "R.solucion_Reporte as Solución\n" +
                            "from Reportes as R\n" + 
                            "inner join Clientes as C\n" +
                            "on R.cliente_Reportes = C.ID_Clientes";
            
            
            
            if(!(combo_Clientes5.getSelectedIndex() == -1) || !(combo_Tipo5.getSelectedIndex() == -1) || !(txtfield_ID1.getText().isEmpty())){
                
                int cont = 0;
                
                sql += "\nwhere ";
                
                if(!(combo_Clientes5.getSelectedIndex() == -1)){cont++;}
                if(!(combo_Tipo5.getSelectedIndex() == -1)){cont++;}
                if(!(txtfield_ID1.getText().isEmpty())){cont++;}
                
                if(!(combo_Clientes5.getSelectedIndex() == -1)){
                    sql += "cliente_Reportes = " + get_clienteIndex(combo_Clientes5);
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(combo_Tipo5.getSelectedIndex() == -1)){
                    sql += "tipo_Equipo = '" + combo_Tipo5.getSelectedItem() + "'";
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(txtfield_ID1.getText().isEmpty())){
                    sql += "ID_Reportes = " + txtfield_ID1.getText();
                }
            }
            
            sql += " order by ID_Reportes desc";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery(sql);
            
            table_Soluciones.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void get_Status() {
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            String sql = "Select R.ID_Reportes as ID,\n" + 
                            "R.fecha_Reportes as Fecha,\n" +
                            "C.nombre_Clientes as Cliente,\n" +
                            "CONCAT(R.tipo_Equipo, ' ', R.marca_Equipo) as Equipo,\n" +
                            "R.status_Reporte as Status\n" +
                            "from Reportes as R\n" + 
                            "inner join Clientes as C\n" +
                            "on R.cliente_Reportes = C.ID_Clientes";
                      
            if(!(combo_Clientes6.getSelectedIndex() == -1) || !(txtfield_ID6.getText().isEmpty()) || !(combo_Status1.getSelectedIndex() == -1)){
                
                int cont = 0;
                
                sql += "\nwhere ";
                
                if(!(combo_Clientes6.getSelectedIndex() == -1)){cont++;}
                if(!(txtfield_ID6.getText().isEmpty())){cont++;}
                if(!(combo_Status1.getSelectedIndex() == -1)){cont++;}
                         
                if(!(combo_Clientes6.getSelectedIndex() == -1)){
                    sql += "cliente_Reportes = " + get_clienteIndex(combo_Clientes6);
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(txtfield_ID6.getText().isEmpty())){
                    sql += "ID_Reportes = '" + txtfield_ID6.getText() + "'";
                    
                    if(--cont > 0){
                        sql += " and ";
                    }
                }
                if(!(combo_Status1.getSelectedIndex() == -1)){
                    sql += "status_Reporte = '" + combo_Status1.getSelectedItem() + "'";              
                }
            }
            
            sql += " order by ID_Reportes desc";
                  
            PreparedStatement stmt = con.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery(sql);
            
            table_Status.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
        
    private void update_Solution() {
        
        String sql = null;
                       
        try {
            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            ResultSet rs;
            Statement stmt = con.createStatement();
        
            sql = "UPDATE Reportes\n" +
                    "set solucion_Reporte = '" + txtArea_Solucion.getText() + "'\n" +
                    "where ID_Reportes = " + txtfield_ID.getText();
            
            
            stmt.execute(sql);
                    
            JOptionPane.showMessageDialog(null, "Solución actualizada");
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void update_Status() {
        
        String sql = null;
             
        try {
            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            ResultSet rs;
            Statement stmt = con.createStatement();
        
            sql = "UPDATE Reportes\n" +
                    "set status_Reporte = '" + combo_Status.getSelectedItem() + "'\n" +
                    "where ID_Reportes = " + txtfield_ID6.getText();
                  
            stmt.execute(sql);
                    
            JOptionPane.showMessageDialog(null, "Solución actualizada");
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void nuevo_Cliente() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
                      
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(ID_Clientes) as ID FROM Clientes");
            
            while(rs.next()){
            
                last_Cliente = rs.getInt("ID");
            
            }
            
            last_Cliente++;
            
            String sql;
            
            String nombre, calle, numCasa, colonia, ciudad, tel, tel2, correo;
            
            nombre = txtfield_Nombre.getText().toUpperCase();
            calle = txtfield_Calle.getText().toUpperCase();
            numCasa = txtfield_numeroCasa.getText();
            colonia = txtfield_Colonia.getText().toUpperCase();
            ciudad = combo_Ciudad.getSelectedItem().toString().toUpperCase();
            tel = txtfield_Telefono.getText();
            tel2 = txtfield_Telefono2.getText();
            correo = txtfield_Correo.getText().toLowerCase();
                       
            sql = "INSERT INTO Clientes Values(" + last_Cliente + ",'" + nombre + "','" + calle
                        + "','" + numCasa + "','" + colonia + "','" + ciudad + "',";
            
            if("null".equals(tel)) sql += "NULL,";
            else sql += "'" + tel + "',";
            
            if("null".equals(tel2)) sql += "NULL,";
            else sql += "'" + tel2 + "',";
            
            if("null".equals(correo)) sql += "NULL)";
            else sql += "'" + correo + "')";

                       
            stmt.execute(sql);
                     
            JOptionPane.showMessageDialog(null, "Cliente agregado");
            
            txtfield_Nombre.setText("");
            txtfield_Calle.setText("");
            txtfield_numeroCasa.setText("");
            txtfield_Colonia.setText("");
            combo_Ciudad.setSelectedIndex(0);
            txtfield_Telefono.setText("");
            txtfield_Telefono2.setText("");
            txtfield_Correo.setText("");
                                   
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void nuevo_Reporte(String fecha){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
                      
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(ID_Reportes) as ID FROM Reportes");
            
            while(rs.next()){
            
                last_Reporte = rs.getInt("ID");
            
            }
            
            int Cliente = 0;
            
            stmt = con.createStatement();
            rs = stmt.executeQuery("Select ID_Clientes as ID from Clientes where nombre_Clientes = '" + combo_Clientes.getSelectedItem() + "'");
            
            while(rs.next()){
            
                Cliente = rs.getInt("ID");
            
            }
            
            String sql;
            
            int Empleado = combo_Empleados.getSelectedIndex() + 1;
             
            if(serv == 1){
                servicios = "'" + combo_Servicios.getSelectedItem() + "','','',''," + txtfield_Precio.getText() + ",0,0,0";
            }                      
                       
            sql = "INSERT INTO Reportes Values(" + (last_Reporte + 1) + ",'" + fecha + "'," + Empleado + "," + Cliente
                    + ",'" + combo_Equipo.getSelectedItem().toString().toUpperCase() + "','" + txtfield_Marca.getText().toUpperCase() + "','" + txtfield_Modelo.getText().toUpperCase() + "','" 
                    + txtfield_nsEquipo.getText().toUpperCase() + "','" + txtfield_nsCargador.getText().toUpperCase() + "','" + txtfield_nsBateria.getText().toUpperCase() + "','" 
                    + txtArea_Fallas.getText().toUpperCase() + "','" + txtfield_Respaldo.getText().toUpperCase() + "','" + txtfield_Prediag.getText().toUpperCase() + "','" + txtArea_Obs.getText() + "'," 
                    + servicios + ",null,'Pendiente'," + anticipo + ")";
            
                   
            stmt.execute(sql);
              
            JOptionPane.showMessageDialog(null, "Reporte agregado");
            
            
            txtfield_Anticipo.setText("");
            txtfield_Precio.setEnabled(true);
            
            combo_Empleados.setSelectedIndex(-1);
            combo_Clientes.setSelectedIndex(-1);
            combo_Equipo.setSelectedIndex(-1);
            txtfield_Marca.setText("");
            txtfield_Modelo.setText("");
            txtfield_nsEquipo.setText("");
            txtfield_nsCargador.setText("");       
            txtfield_nsBateria.setText("INTERNA");
            txtArea_Fallas.setText("");
            txtfield_Respaldo.setText("");
            txtfield_Prediag.setText("");
            txtArea_Obs.setText("");
            combo_Servicios.setSelectedIndex(0);
            txtfield_Precio.setText("180");
            txtfield_Anticipo.setText("");
            
            servicios = "";
            anticipo = "";
            
            serv = 1;
                                   
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void eliminar_Empleado(){      
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            ResultSet rs;
            Statement stmt = con.createStatement();
            
            String sql;
            
            int Empleado = combo_Empleados7.getSelectedIndex() + 1;
        
            sql = "UPDATE Empleados\n" +
                    "set empleado_Eliminado = 1\n" +
                    "where ID_Empleados = " + Empleado;
                  
            stmt.execute(sql);
                    
            JOptionPane.showMessageDialog(null, "Empleado eliminado");
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }                             
    }
    
    private void datos_Clientes(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            Statement stmt = con.createStatement();
            
            String sql;
            String [] clientes = new String[9];
            
            int indiceCliente = get_clienteIndex(combo_Clientes8);
            
            sql = "Select calle_Clientes from Clientes where ID_Clientes = " + indiceCliente;
                  
            ResultSet rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                clientes[0] = rs.getString("calle_Clientes");
            }   
            
            sql = "Select numeroCasa_Clientes from Clientes where ID_Clientes = " + indiceCliente;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                clientes[1] = rs.getString("numeroCasa_Clientes");
            }   
            
            sql = "Select colonia_Clientes from Clientes where ID_Clientes = " + indiceCliente;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                clientes[2] = rs.getString("colonia_Clientes");
            }   
            
            sql = "Select ciudad_Clientes from Clientes where ID_Clientes = " + indiceCliente;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                clientes[3] = rs.getString("ciudad_Clientes");
            }   
            
            sql = "Select telefono_Clientes from Clientes where ID_Clientes = " + indiceCliente;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                clientes[4] = rs.getString("telefono_Clientes");
            }   
            
            sql = "Select telefono2_Clientes from Clientes where ID_Clientes = " + indiceCliente;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                clientes[5] = rs.getString("telefono2_Clientes");
            }   
            
            sql = "Select correo_Clientes from Clientes where ID_Clientes = " + indiceCliente;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                clientes[6] = rs.getString("correo_Clientes");
            }   
            
            txtfield_Calle1.setText(clientes[0]);
            txtfield_numeroCasa1.setText(clientes[1]);
            txtfield_Colonia1.setText(clientes[2]);
            txtfield_Ciudad1.setText(clientes[3]);
            txtfield_Telefono1.setText(clientes[4]);
            txtfield_Telefono4.setText(clientes[5]);
            txtfield_Correo1.setText(clientes[6]);
            
                                                         
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actualizar_Datos(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            ResultSet rs;
            Statement stmt = con.createStatement();
            
            String sql;
                       
            sql = "UPDATE Clientes set nombre_Clientes = '" + combo_Clientes8.getSelectedItem() + "', calle_Clientes = '" + 
                    txtfield_Calle1.getText().toUpperCase() + "', numeroCasa_Clientes = '" + txtfield_numeroCasa1.getText() + 
                    "', colonia_Clientes ='" + txtfield_Colonia1.getText().toUpperCase() + "', ciudad_Clientes ='" + txtfield_Ciudad1.getText().toUpperCase() + 
                    "', telefono_Clientes ='" + txtfield_Telefono1.getText() + "', telefono2_Clientes ='" + txtfield_Telefono4.getText() + 
                    "', correo_Clientes ='" + txtfield_Correo1.getText() + "' where ID_Clientes  = " + get_clienteIndex(combo_Clientes8);
            
            stmt.execute(sql);
            
            JOptionPane.showMessageDialog(null, "Datos actualizados.");
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void servicios_Reportes(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            Statement stmt = con.createStatement();
            
            String sql;
            String servicios[] = new String [5];
            String precios[] = new String [5];
            
            int ID_Rep = Integer.parseInt(txtfield_ID6.getText());
            
            sql = "Select servicios_Equipo from Reportes where ID_Reportes = " + ID_Rep;
                  
            ResultSet rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                servicios[0] = rs.getString("servicios_Equipo");
            }   
            
            sql = "Select servicio2_Equipo from Reportes where ID_Reportes = " + ID_Rep;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                servicios[1] = rs.getString("servicio2_Equipo");
            }
            
            sql = "Select servicio3_Equipo from Reportes where ID_Reportes = " + ID_Rep;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                servicios[2] = rs.getString("servicio3_Equipo");
            }
            
            sql = "Select servicio4_Equipo from Reportes where ID_Reportes = " + ID_Rep;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                servicios[3] = rs.getString("servicio4_Equipo");
            }
            
            sql = "Select precio_Reporte from Reportes where ID_Reportes = " + ID_Rep;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                precios[0] = rs.getString("precio_Reporte");
            }
            
            sql = "Select precio2_Reporte from Reportes where ID_Reportes = " + ID_Rep;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                precios[1] = rs.getString("precio2_Reporte");
            }
            
            sql = "Select precio3_Reporte from Reportes where ID_Reportes = " + ID_Rep;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                precios[2] = rs.getString("precio3_Reporte");
            }
            
            sql = "Select precio4_Reporte from Reportes where ID_Reportes = " + ID_Rep;
                  
            rs = stmt.executeQuery(sql);
                        
            while(rs.next()) {
                
                precios[3] = rs.getString("precio4_Reporte");
            }
            
            txtfield_Servicios6_1.setText(servicios[0]);
            txtfield_Precio6_1.setText(precios[0]);
            
            /*txtfield_Servicios6_2.setVisible(false);
            txtfield_Servicios6_3.setVisible(false);
            txtfield_Servicios6_4.setVisible(false);*/
            
            /*combo_Servicios6_2.setVisible(true);
            combo_Servicios6_3.setVisible(true);
            combo_Servicios6_4.setVisible(true);*/
            
            txtfield_Servicios6_2.setText(servicios[1]);
            txtfield_Servicios6_3.setText(servicios[2]);
            txtfield_Servicios6_4.setText(servicios[3]);
            
            txtfield_Precio6_2.setText(precios[1]);
            txtfield_Precio6_3.setText(precios[2]);
            txtfield_Precio6_4.setText(precios[3]);
            
            if(!(txtfield_Servicios6_2.getText().isEmpty()) && !(txtfield_Servicios6_2.getText().equals("null"))){
                txtfield_Servicios6_2.setVisible(true);
                combo_Servicios6_2.setVisible(false);
                chk_Servicio6_2.setSelected(true);
                chk_Servicio6_2.setEnabled(false);
                txtfield_Precio6_2.setEnabled(true);
                //txtfield_Precio6_2.setText(precios[1]);
                
                if(!(txtfield_Servicios6_3.getText().isEmpty()) && !(txtfield_Servicios6_3.getText().equals("null"))){
                    txtfield_Servicios6_3.setVisible(true);
                    combo_Servicios6_3.setVisible(false);
                    chk_Servicio6_3.setSelected(true);
                    chk_Servicio6_3.setEnabled(false);
                    txtfield_Precio6_3.setEnabled(true);
                    //txtfield_Precio6_3.setText(precios[2]);
                    
                    if(!(txtfield_Servicios6_4.getText().isEmpty()) && !(txtfield_Servicios6_4.getText().equals("null"))){
                        txtfield_Servicios6_4.setVisible(true);
                        combo_Servicios6_4.setVisible(false);
                        chk_Servicio6_4.setSelected(true);
                        chk_Servicio6_4.setEnabled(false);
                        txtfield_Precio6_4.setEnabled(true);
                        //txtfield_Precio6_4.setText(precios[3]);
                    }
                    else{
                        txtfield_Servicios6_4.setVisible(true);
                        txtfield_Servicios6_4.setEditable(false);
                        txtfield_Precio6_4.setEditable(false);
                        txtfield_Precio6_4.setText("");
                        combo_Servicios6_4.setVisible(false);
                        chk_Servicio6_4.setSelected(false);
                        chk_Servicio6_4.setEnabled(true);
                    }
                }
                else{
                    txtfield_Servicios6_3.setVisible(true);
                    txtfield_Servicios6_3.setEditable(false);
                    txtfield_Servicios6_4.setVisible(true);
                    txtfield_Servicios6_4.setEditable(false);
                    txtfield_Precio6_3.setEditable(false);
                    txtfield_Precio6_3.setText("");
                    txtfield_Precio6_4.setEditable(false);
                    txtfield_Precio6_4.setText("");
                    combo_Servicios6_3.setVisible(false);
                    chk_Servicio6_3.setSelected(false);
                    chk_Servicio6_3.setEnabled(true);
                    chk_Servicio6_4.setSelected(false);
                    chk_Servicio6_4.setEnabled(false);
                }
            }
            else{
                txtfield_Servicios6_2.setVisible(true);
                txtfield_Servicios6_2.setEditable(false);
                txtfield_Servicios6_3.setVisible(true);
                txtfield_Servicios6_3.setEditable(false);
                txtfield_Servicios6_4.setVisible(true);
                txtfield_Servicios6_4.setEditable(false);
                txtfield_Precio6_2.setEditable(false);
                txtfield_Precio6_2.setText("");
                txtfield_Precio6_3.setEditable(false);
                txtfield_Precio6_3.setText("");
                txtfield_Precio6_4.setEditable(false);
                txtfield_Precio6_4.setText("");
                combo_Servicios6_2.setVisible(false);
                chk_Servicio6_2.setSelected(false);
                chk_Servicio6_2.setEnabled(true);
                chk_Servicio6_3.setSelected(false);
                chk_Servicio6_3.setEnabled(false);
                chk_Servicio6_4.setSelected(false);
                chk_Servicio6_4.setEnabled(false);
            }
                                                
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actualizar_Servicios(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            ResultSet rs;
            Statement stmt = con.createStatement();
            
            String sql;
                       
            sql = "UPDATE Reportes set servicio2_Equipo = '" + combo_Servicios6_2.getSelectedItem() + "', servicio3_Equipo = '" + 
                    combo_Servicios6_3.getSelectedItem() + "', servicio4_Equipo = '" + combo_Servicios6_4.getSelectedItem() + "', precio2_Reporte = " + 
                    txtfield_Precio6_2.getText() + ", precio3_Reporte = " + txtfield_Precio6_3.getText() + ", precio4_Reporte =  " + 
                    txtfield_Precio6_4.getText() + " where ID_Reportes  = " + Integer.parseInt(txtfield_ID6.getText());
            
            System.out.println(sql);
            //stmt.execute(sql);
            
            JOptionPane.showMessageDialog(null, "Servicios agregados");
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregar_Empleado(){
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(ID_Empleados) as ID FROM Empleados");
            int last_Empleado = 0;
            
            while(rs.next()){
                
                last_Empleado = rs.getInt("ID");
                
            }
            
            last_Empleado++;
                        
            String sql;
            
            sql = "INSERT INTO Empleados Values(" + last_Empleado + ", '" + txtfield_Nombre7.getText() + "', 0);";
           
            stmt.execute(sql);
            
            JOptionPane.showMessageDialog(null, "Empleado agregado");
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void modelComboEmpleados(JComboBox jComboBox){
        for(int i = 0; i < numeroEmpleados; i++){nombreEmpleados[i] = "";}
        
        numeroEmpleados = 0;
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select nombre_Empleado, empleado_Eliminado from Empleados");
            
            String nombre;
            int elim;
            
            while(rs.next()) {                
                nombre = rs.getString("nombre_Empleado");  
                elim = rs.getInt("empleado_Eliminado");
                
                if(elim == 0){
                    nombreEmpleados[numeroEmpleados] = nombre; 
                }
                
                numeroEmpleados++;
            }         
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        DefaultComboBoxModel model = new DefaultComboBoxModel(nombreEmpleados);
        jComboBox.setModel(model);
        jComboBox.setSelectedIndex(-1);
    }
    
    private void modelComboClientes(JComboBox jComboBox){
        for(int i = 0; i < numeroClientes; i++){nombreClientes[i] = "";}
        numeroClientes = 0;
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select nombre_Clientes from Clientes order by nombre_Clientes");
            
            String nombre;
            
            while(rs.next()) {
                
                nombre = rs.getString("nombre_Clientes");
                nombreClientes[numeroClientes++] = nombre.toUpperCase();
            }
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        DefaultComboBoxModel model = new DefaultComboBoxModel(nombreClientes);
        jComboBox.setModel(model);
        jComboBox.setSelectedIndex(-1);
    }
    
    private int get_clienteIndex(JComboBox jComboBox1){
        int Cliente = 0;
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select ID_Clientes as ID from Clientes where nombre_Clientes = '" + jComboBox1.getSelectedItem() + "'");
            
            while(rs.next()) {
                
                Cliente = rs.getInt("ID");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Cliente;
    }
    
    void setColor(JPanel panel) {
        panel.setBackground(new Color(73,73,73));
    }
    
    void resetColor(JPanel panel) {
        panel.setBackground(new Color(45,45,45));
    }
    
    private void reporte_PDF(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");
            
            HashMap parameter = new HashMap();
            
            int ID_R = last_Reporte;
            ID_R++;
            
            parameter.put("ID", ID_R);
        
            //String path = "G:\\BitFix\\BitFix\\src\\Reportes/newReport.jrxml";
            String path = "D:\\Documents\\NetBeansProjects\\BitFix\\src\\Reportes/newReport.jrxml";
            
            JasperReport content = JasperCompileManager.compileReport(path);
            
            JasperPrint print = JasperFillManager.fillReport(content, parameter, con);
            
            //JasperExportManager.exportReportToPdfFile(print, "D:\\Documents\\BitFix-Reports\\reportTemp.pdf");
           
            JasperViewer jv = new JasperViewer(print, false);
            
            jv.viewReport(print, false);
            
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void agregar_Servicios(){ 
        int c = combo_Servicios1_3.getSelectedIndex();
        
        if(serv == 2){
            servicios = "'" + combo_Servicios1_1.getSelectedItem().toString() + "','" + combo_Servicios1_2.getSelectedItem().toString() + "','',''," + txtfield_Precio1_1.getText() + "," + txtfield_Precio1_2.getText() + ",0,0";
        }
        else if(serv == 3){
            if(c != -1){
                servicios = "'" + combo_Servicios1_1.getSelectedItem().toString() + "','" + combo_Servicios1_2.getSelectedItem().toString() + "','" + combo_Servicios1_3.getSelectedItem().toString() + "',''," + txtfield_Precio1_1.getText() + "," + txtfield_Precio1_2.getText() + "," + txtfield_Precio1_3.getText() + ",0";
            }
            else{
                servicios = "'" + combo_Servicios1_1.getSelectedItem().toString() + "','" + combo_Servicios1_2.getSelectedItem().toString() + "','" + combo_Servicios1_4.getSelectedItem().toString() + "',''," + txtfield_Precio1_1.getText() + "," + txtfield_Precio1_2.getText() + "," + txtfield_Precio1_4.getText() + ",0";
            }
        }
        else if(serv == 4){
            servicios = "'" + combo_Servicios1_1.getSelectedItem().toString() + "','" + combo_Servicios1_2.getSelectedItem().toString() + "','" + combo_Servicios1_3.getSelectedItem().toString() + "','" + combo_Servicios1_4.getSelectedItem().toString() + "'," + txtfield_Precio1_1.getText() + "," + txtfield_Precio1_2.getText() + "," + txtfield_Precio1_3.getText() + "," + txtfield_Precio1_4.getText();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        sidebar = new javax.swing.JPanel();
        img_Logo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btn_AgregarReporte = new javax.swing.JPanel();
        space1 = new javax.swing.JLabel();
        logo_AgregarReporte = new javax.swing.JLabel();
        txt_AgregarReporte = new javax.swing.JLabel();
        btn_AgregarCliente = new javax.swing.JPanel();
        space7 = new javax.swing.JLabel();
        logo_AgregarCliente1 = new javax.swing.JLabel();
        txt_AgregarCliente1 = new javax.swing.JLabel();
        btn_VerReporte = new javax.swing.JPanel();
        space3 = new javax.swing.JLabel();
        logo_VerReporte = new javax.swing.JLabel();
        txt_VerReporte = new javax.swing.JLabel();
        btn_AgregarSolucion = new javax.swing.JPanel();
        space4 = new javax.swing.JLabel();
        logo_AgregarSolucion = new javax.swing.JLabel();
        txt_AgregarSolucion = new javax.swing.JLabel();
        btn_VerSoluciones = new javax.swing.JPanel();
        space5 = new javax.swing.JLabel();
        logo_VerSoluciones = new javax.swing.JLabel();
        txt_VerSoluciones = new javax.swing.JLabel();
        btn_ActualizarStatus = new javax.swing.JPanel();
        space6 = new javax.swing.JLabel();
        logo_ActualizarStatus = new javax.swing.JLabel();
        txt_ActualizarStatus = new javax.swing.JLabel();
        btn_AdministrarEmpleados = new javax.swing.JPanel();
        space = new javax.swing.JLabel();
        logo_AdministrarEmpleados = new javax.swing.JLabel();
        txt_AdministrarEmpleados = new javax.swing.JLabel();
        btn_ActualizarCliente = new javax.swing.JPanel();
        space2 = new javax.swing.JLabel();
        logo_AgregarCliente = new javax.swing.JLabel();
        txt_AgregarCliente = new javax.swing.JLabel();
        main_Screen1 = new javax.swing.JPanel();
        title_bar = new javax.swing.JPanel();
        txt_breadcrumbs = new javax.swing.JLabel();
        txt_Titulo = new javax.swing.JLabel();
        txt_Empleado = new javax.swing.JLabel();
        combo_Empleados = new javax.swing.JComboBox<>();
        txt_Cliente = new javax.swing.JLabel();
        combo_Clientes = new javax.swing.JComboBox<>();
        txt_Equipo = new javax.swing.JLabel();
        combo_Equipo = new javax.swing.JComboBox<>();
        txt_Marca = new javax.swing.JLabel();
        txtfield_Marca = new javax.swing.JTextField();
        txt_Modelo = new javax.swing.JLabel();
        txtfield_Modelo = new javax.swing.JTextField();
        txt_nsEquipo = new javax.swing.JLabel();
        screen_Anticipo = new javax.swing.JPanel();
        txt_Anticipo = new javax.swing.JLabel();
        radio_Si = new javax.swing.JRadioButton();
        txt_Peso = new javax.swing.JLabel();
        txtfield_Anticipo = new javax.swing.JTextField();
        radio_No = new javax.swing.JRadioButton();
        btn_OK = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        screen_Servicios = new javax.swing.JPanel();
        txt_Titulo1_1 = new javax.swing.JLabel();
        chk_Servicio1 = new javax.swing.JCheckBox();
        combo_Servicios1_1 = new javax.swing.JComboBox<>();
        txt_Simbolo1_1 = new javax.swing.JLabel();
        txtfield_Precio1_1 = new javax.swing.JTextField();
        chk_Servicio1_2 = new javax.swing.JCheckBox();
        combo_Servicios1_2 = new javax.swing.JComboBox<>();
        txt_Simbolo1_2 = new javax.swing.JLabel();
        txtfield_Precio1_2 = new javax.swing.JTextField();
        chk_Servicio1_3 = new javax.swing.JCheckBox();
        combo_Servicios1_3 = new javax.swing.JComboBox<>();
        txt_Simbolo1_3 = new javax.swing.JLabel();
        txtfield_Precio1_3 = new javax.swing.JTextField();
        chk_Servicio1_4 = new javax.swing.JCheckBox();
        combo_Servicios1_4 = new javax.swing.JComboBox<>();
        txt_Simbolo1_4 = new javax.swing.JLabel();
        txtfield_Precio1_4 = new javax.swing.JTextField();
        btn_Agregar1_1 = new javax.swing.JPanel();
        txt_Agregar4 = new javax.swing.JLabel();
        txtfield_nsEquipo = new javax.swing.JTextField();
        txt_nsCargador = new javax.swing.JLabel();
        txtfield_nsCargador = new javax.swing.JTextField();
        txt_nsBateria = new javax.swing.JLabel();
        txtfield_nsBateria = new javax.swing.JTextField();
        txt_Fallas = new javax.swing.JLabel();
        scroll_Fallas = new javax.swing.JScrollPane();
        txtArea_Fallas = new javax.swing.JTextArea();
        txt_Respaldo = new javax.swing.JLabel();
        txtfield_Respaldo = new javax.swing.JTextField();
        txt_Prediag = new javax.swing.JLabel();
        txtfield_Prediag = new javax.swing.JTextField();
        txt_Obs = new javax.swing.JLabel();
        scroll_Obs = new javax.swing.JScrollPane();
        txtArea_Obs = new javax.swing.JTextArea();
        txt_Servicios = new javax.swing.JLabel();
        combo_Servicios = new javax.swing.JComboBox<>();
        txt_Precio = new javax.swing.JLabel();
        txt_Simbolo = new javax.swing.JLabel();
        txtfield_Precio = new javax.swing.JTextField();
        btn_Agregar = new javax.swing.JPanel();
        txt_Agregar = new javax.swing.JLabel();
        btn_Imprimir = new javax.swing.JPanel();
        txt_Agregar1 = new javax.swing.JLabel();
        main_Screen2 = new javax.swing.JPanel();
        title_bar2 = new javax.swing.JPanel();
        txt_breadcrumbs2 = new javax.swing.JLabel();
        txt_Titulo2 = new javax.swing.JLabel();
        txt_Nombre = new javax.swing.JLabel();
        txtfield_Nombre = new javax.swing.JTextField();
        txt_Calle = new javax.swing.JLabel();
        txtfield_Calle = new javax.swing.JTextField();
        txt_numeroCasa = new javax.swing.JLabel();
        txtfield_numeroCasa = new javax.swing.JTextField();
        txt_Colonia = new javax.swing.JLabel();
        txtfield_Colonia = new javax.swing.JTextField();
        txt_Ciudad = new javax.swing.JLabel();
        combo_Ciudad = new javax.swing.JComboBox<>();
        txt_telefono = new javax.swing.JLabel();
        txtfield_Telefono = new javax.swing.JTextField();
        txt_telefono2 = new javax.swing.JLabel();
        txtfield_Telefono2 = new javax.swing.JTextField();
        txt_Correo = new javax.swing.JLabel();
        txtfield_Correo = new javax.swing.JTextField();
        btn_Agregar2 = new javax.swing.JPanel();
        txt_Agregar2 = new javax.swing.JLabel();
        main_Screen3 = new javax.swing.JPanel();
        title_bar3 = new javax.swing.JPanel();
        txt_breadcrumbs3 = new javax.swing.JLabel();
        txt_Titulo3 = new javax.swing.JLabel();
        txt_Empleado3 = new javax.swing.JLabel();
        combo_Empleados3 = new javax.swing.JComboBox<>();
        txt_Cliente3 = new javax.swing.JLabel();
        combo_Clientes3 = new javax.swing.JComboBox<>();
        txt_Tipo3 = new javax.swing.JLabel();
        combo_Tipo3 = new javax.swing.JComboBox<>();
        txt_Marca3 = new javax.swing.JLabel();
        txtfield_Marca3 = new javax.swing.JTextField();
        btn_Buscar3 = new javax.swing.JPanel();
        txt_Buscar = new javax.swing.JLabel();
        btn_Resetear3 = new javax.swing.JPanel();
        txt_Buscar7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_Reportes = new javax.swing.JTable();
        btn_Imprimir3 = new javax.swing.JPanel();
        txt_Buscar10 = new javax.swing.JLabel();
        txt_Marca4 = new javax.swing.JLabel();
        txtfield_ID3 = new javax.swing.JTextField();
        main_Screen4 = new javax.swing.JPanel();
        title_bar4 = new javax.swing.JPanel();
        txt_breadcrumbs4 = new javax.swing.JLabel();
        txt_Titulo4 = new javax.swing.JLabel();
        txt_ID = new javax.swing.JLabel();
        txtfield_ID = new javax.swing.JTextField();
        txt_Solucion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea_Solucion = new javax.swing.JTextArea();
        btn_Agregar4 = new javax.swing.JPanel();
        txt_Buscar1 = new javax.swing.JLabel();
        main_Screen5 = new javax.swing.JPanel();
        title_bar5 = new javax.swing.JPanel();
        txt_breadcrumbs5 = new javax.swing.JLabel();
        txt_Titulo5 = new javax.swing.JLabel();
        txt_Cliente5 = new javax.swing.JLabel();
        combo_Clientes5 = new javax.swing.JComboBox<>();
        txt_ID1 = new javax.swing.JLabel();
        txtfield_ID1 = new javax.swing.JTextField();
        txt_Tipo5 = new javax.swing.JLabel();
        combo_Tipo5 = new javax.swing.JComboBox<>();
        btn_Buscar5 = new javax.swing.JPanel();
        txt_Buscar2 = new javax.swing.JLabel();
        btn_Resetear5 = new javax.swing.JPanel();
        txt_Buscar8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_Soluciones = new javax.swing.JTable();
        main_Screen6 = new javax.swing.JPanel();
        screen_Servicios6 = new javax.swing.JPanel();
        txt_Titulo1_2 = new javax.swing.JLabel();
        chk_Servicio6_1 = new javax.swing.JCheckBox();
        txtfield_Servicios6_1 = new javax.swing.JTextField();
        txt_Simbolo1_5 = new javax.swing.JLabel();
        txtfield_Precio6_1 = new javax.swing.JTextField();
        chk_Servicio6_2 = new javax.swing.JCheckBox();
        txtfield_Servicios6_2 = new javax.swing.JTextField();
        combo_Servicios6_2 = new javax.swing.JComboBox<>();
        txt_Simbolo1_6 = new javax.swing.JLabel();
        txtfield_Precio6_2 = new javax.swing.JTextField();
        chk_Servicio6_3 = new javax.swing.JCheckBox();
        txtfield_Servicios6_3 = new javax.swing.JTextField();
        combo_Servicios6_3 = new javax.swing.JComboBox<>();
        txt_Simbolo1_7 = new javax.swing.JLabel();
        txtfield_Precio6_3 = new javax.swing.JTextField();
        chk_Servicio6_4 = new javax.swing.JCheckBox();
        txtfield_Servicios6_4 = new javax.swing.JTextField();
        combo_Servicios6_4 = new javax.swing.JComboBox<>();
        txt_Simbolo1_8 = new javax.swing.JLabel();
        txtfield_Precio6_4 = new javax.swing.JTextField();
        btn_Agregar6_1 = new javax.swing.JPanel();
        txt_Agregar5 = new javax.swing.JLabel();
        title_bar6 = new javax.swing.JPanel();
        txt_breadcrumbs6 = new javax.swing.JLabel();
        txt_Titulo6 = new javax.swing.JLabel();
        txt_Cliente6 = new javax.swing.JLabel();
        combo_Clientes6 = new javax.swing.JComboBox<>();
        txt_ID2 = new javax.swing.JLabel();
        txtfield_ID6 = new javax.swing.JTextField();
        txt_Status1 = new javax.swing.JLabel();
        combo_Status1 = new javax.swing.JComboBox<>();
        btn_Buscar6 = new javax.swing.JPanel();
        txt_Buscar3 = new javax.swing.JLabel();
        btn_Resetear6 = new javax.swing.JPanel();
        txt_Buscar9 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        table_Status = new javax.swing.JTable();
        txt_Status = new javax.swing.JLabel();
        combo_Status = new javax.swing.JComboBox<>();
        btn_Actualizar = new javax.swing.JPanel();
        txt_Buscar4 = new javax.swing.JLabel();
        btn_AgregarServicios = new javax.swing.JPanel();
        txt_Buscar11 = new javax.swing.JLabel();
        txt_Buscar12 = new javax.swing.JLabel();
        main_Screen7 = new javax.swing.JPanel();
        title_bar7 = new javax.swing.JPanel();
        txt_breadcrumbs7 = new javax.swing.JLabel();
        txt_Titulo7 = new javax.swing.JLabel();
        txt_Empleado7 = new javax.swing.JLabel();
        combo_Empleados7 = new javax.swing.JComboBox<>();
        btn_Eliminar7 = new javax.swing.JPanel();
        txt_Buscar5 = new javax.swing.JLabel();
        txtfield_Nombre7 = new javax.swing.JTextField();
        txt_Nombre7 = new javax.swing.JLabel();
        btn_Agregar7 = new javax.swing.JPanel();
        txt_Buscar6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        main_Screen8 = new javax.swing.JPanel();
        title_bar8 = new javax.swing.JPanel();
        txt_breadcrumbs8 = new javax.swing.JLabel();
        txt_Titulo8 = new javax.swing.JLabel();
        txt_Nombre1 = new javax.swing.JLabel();
        combo_Clientes8 = new javax.swing.JComboBox<>();
        txt_Calle1 = new javax.swing.JLabel();
        txtfield_Calle1 = new javax.swing.JTextField();
        txt_numeroCasa1 = new javax.swing.JLabel();
        txtfield_numeroCasa1 = new javax.swing.JTextField();
        txt_Colonia1 = new javax.swing.JLabel();
        txtfield_Colonia1 = new javax.swing.JTextField();
        txt_Ciudad1 = new javax.swing.JLabel();
        txtfield_Ciudad1 = new javax.swing.JTextField();
        txt_telefono1 = new javax.swing.JLabel();
        txtfield_Telefono1 = new javax.swing.JTextField();
        txt_telefono3 = new javax.swing.JLabel();
        txtfield_Telefono4 = new javax.swing.JTextField();
        txt_Correo1 = new javax.swing.JLabel();
        txtfield_Correo1 = new javax.swing.JTextField();
        btn_Actualizar8 = new javax.swing.JPanel();
        txt_Agregar3 = new javax.swing.JLabel();
        load_Screen = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BitFix");
        setBounds(new java.awt.Rectangle(45, 10, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidebar.setBackground(new java.awt.Color(0, 0, 0));
        sidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        img_Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Logo_Main.png"))); // NOI18N
        img_Logo.setMaximumSize(new java.awt.Dimension(240, 240));
        sidebar.add(img_Logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, 140));
        sidebar.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 320, 10));

        btn_AgregarReporte.setBackground(new java.awt.Color(73, 73, 73));
        btn_AgregarReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AgregarReporteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AgregarReporteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_AgregarReporteMousePressed(evt);
            }
        });
        btn_AgregarReporte.setLayout(new javax.swing.BoxLayout(btn_AgregarReporte, javax.swing.BoxLayout.LINE_AXIS));

        space1.setText("             ");
        btn_AgregarReporte.add(space1);

        logo_AgregarReporte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_AgregarReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/NuevoReporte_Logo.png"))); // NOI18N
        logo_AgregarReporte.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_AgregarReporte.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_AgregarReporte.add(logo_AgregarReporte);

        txt_AgregarReporte.setFont(new java.awt.Font("Eras Demi ITC", 0, 22)); // NOI18N
        txt_AgregarReporte.setForeground(new java.awt.Color(255, 255, 255));
        txt_AgregarReporte.setText("Agregar Reporte");
        btn_AgregarReporte.add(txt_AgregarReporte);

        sidebar.add(btn_AgregarReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 380, 60));

        btn_AgregarCliente.setBackground(new java.awt.Color(45, 45, 45));
        btn_AgregarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AgregarClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AgregarClienteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_AgregarClienteMousePressed(evt);
            }
        });
        btn_AgregarCliente.setLayout(new javax.swing.BoxLayout(btn_AgregarCliente, javax.swing.BoxLayout.LINE_AXIS));

        space7.setText("             ");
        btn_AgregarCliente.add(space7);

        logo_AgregarCliente1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_AgregarCliente1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/NuevoUsuario.png"))); // NOI18N
        logo_AgregarCliente1.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_AgregarCliente1.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_AgregarCliente.add(logo_AgregarCliente1);

        txt_AgregarCliente1.setFont(new java.awt.Font("Eras Demi ITC", 0, 22)); // NOI18N
        txt_AgregarCliente1.setForeground(new java.awt.Color(255, 255, 255));
        txt_AgregarCliente1.setText("Agregar Cliente");
        btn_AgregarCliente.add(txt_AgregarCliente1);

        sidebar.add(btn_AgregarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 380, 60));

        btn_VerReporte.setBackground(new java.awt.Color(45, 45, 45));
        btn_VerReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_VerReporteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_VerReporteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_VerReporteMousePressed(evt);
            }
        });
        btn_VerReporte.setLayout(new javax.swing.BoxLayout(btn_VerReporte, javax.swing.BoxLayout.LINE_AXIS));

        space3.setText("             ");
        btn_VerReporte.add(space3);

        logo_VerReporte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_VerReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/DisplayReportes.png"))); // NOI18N
        logo_VerReporte.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_VerReporte.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_VerReporte.add(logo_VerReporte);

        txt_VerReporte.setFont(new java.awt.Font("Eras Demi ITC", 0, 22)); // NOI18N
        txt_VerReporte.setForeground(new java.awt.Color(255, 255, 255));
        txt_VerReporte.setText("Ver Reportes");
        btn_VerReporte.add(txt_VerReporte);

        sidebar.add(btn_VerReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 380, 60));

        btn_AgregarSolucion.setBackground(new java.awt.Color(45, 45, 45));
        btn_AgregarSolucion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AgregarSolucionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AgregarSolucionMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_AgregarSolucionMousePressed(evt);
            }
        });
        btn_AgregarSolucion.setLayout(new javax.swing.BoxLayout(btn_AgregarSolucion, javax.swing.BoxLayout.LINE_AXIS));

        space4.setText("             ");
        btn_AgregarSolucion.add(space4);

        logo_AgregarSolucion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_AgregarSolucion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Check.png"))); // NOI18N
        logo_AgregarSolucion.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_AgregarSolucion.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_AgregarSolucion.add(logo_AgregarSolucion);

        txt_AgregarSolucion.setFont(new java.awt.Font("Eras Demi ITC", 0, 22)); // NOI18N
        txt_AgregarSolucion.setForeground(new java.awt.Color(255, 255, 255));
        txt_AgregarSolucion.setText("Agregar Solución");
        btn_AgregarSolucion.add(txt_AgregarSolucion);

        sidebar.add(btn_AgregarSolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 380, 60));

        btn_VerSoluciones.setBackground(new java.awt.Color(45, 45, 45));
        btn_VerSoluciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_VerSolucionesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_VerSolucionesMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_VerSolucionesMousePressed(evt);
            }
        });
        btn_VerSoluciones.setLayout(new javax.swing.BoxLayout(btn_VerSoluciones, javax.swing.BoxLayout.LINE_AXIS));

        space5.setText("             ");
        btn_VerSoluciones.add(space5);

        logo_VerSoluciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_VerSoluciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Check_Document.png"))); // NOI18N
        logo_VerSoluciones.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_VerSoluciones.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_VerSoluciones.add(logo_VerSoluciones);

        txt_VerSoluciones.setFont(new java.awt.Font("Eras Demi ITC", 0, 22)); // NOI18N
        txt_VerSoluciones.setForeground(new java.awt.Color(255, 255, 255));
        txt_VerSoluciones.setText("Ver Soluciones");
        btn_VerSoluciones.add(txt_VerSoluciones);

        sidebar.add(btn_VerSoluciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 380, 60));

        btn_ActualizarStatus.setBackground(new java.awt.Color(45, 45, 45));
        btn_ActualizarStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ActualizarStatusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ActualizarStatusMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_ActualizarStatusMousePressed(evt);
            }
        });
        btn_ActualizarStatus.setLayout(new javax.swing.BoxLayout(btn_ActualizarStatus, javax.swing.BoxLayout.LINE_AXIS));

        space6.setText("             ");
        btn_ActualizarStatus.add(space6);

        logo_ActualizarStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_ActualizarStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Status.png"))); // NOI18N
        logo_ActualizarStatus.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_ActualizarStatus.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_ActualizarStatus.add(logo_ActualizarStatus);

        txt_ActualizarStatus.setFont(new java.awt.Font("Eras Demi ITC", 0, 22)); // NOI18N
        txt_ActualizarStatus.setForeground(new java.awt.Color(255, 255, 255));
        txt_ActualizarStatus.setText("Ver / Actualizar Status");
        btn_ActualizarStatus.add(txt_ActualizarStatus);

        sidebar.add(btn_ActualizarStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 380, 60));

        btn_AdministrarEmpleados.setBackground(new java.awt.Color(33, 33, 33));
        btn_AdministrarEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AdministrarEmpleadosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AdministrarEmpleadosMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_AdministrarEmpleadosMousePressed(evt);
            }
        });
        btn_AdministrarEmpleados.setLayout(new javax.swing.BoxLayout(btn_AdministrarEmpleados, javax.swing.BoxLayout.LINE_AXIS));

        space.setText("             ");
        btn_AdministrarEmpleados.add(space);

        logo_AdministrarEmpleados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_AdministrarEmpleados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Empleados.png"))); // NOI18N
        logo_AdministrarEmpleados.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_AdministrarEmpleados.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_AdministrarEmpleados.add(logo_AdministrarEmpleados);

        txt_AdministrarEmpleados.setFont(new java.awt.Font("Eras Demi ITC", 0, 20)); // NOI18N
        txt_AdministrarEmpleados.setForeground(new java.awt.Color(255, 255, 255));
        txt_AdministrarEmpleados.setText("Administrar Empleados");
        btn_AdministrarEmpleados.add(txt_AdministrarEmpleados);

        sidebar.add(btn_AdministrarEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 380, 60));

        btn_ActualizarCliente.setBackground(new java.awt.Color(45, 45, 45));
        btn_ActualizarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ActualizarClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ActualizarClienteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_ActualizarClienteMousePressed(evt);
            }
        });
        btn_ActualizarCliente.setLayout(new javax.swing.BoxLayout(btn_ActualizarCliente, javax.swing.BoxLayout.LINE_AXIS));

        space2.setText("             ");
        btn_ActualizarCliente.add(space2);

        logo_AgregarCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_AgregarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ActualizarCliente.png"))); // NOI18N
        logo_AgregarCliente.setMaximumSize(new java.awt.Dimension(75, 60));
        logo_AgregarCliente.setPreferredSize(new java.awt.Dimension(75, 60));
        btn_ActualizarCliente.add(logo_AgregarCliente);

        txt_AgregarCliente.setFont(new java.awt.Font("Eras Demi ITC", 0, 22)); // NOI18N
        txt_AgregarCliente.setForeground(new java.awt.Color(255, 255, 255));
        txt_AgregarCliente.setText("Actualizar Cliente");
        btn_ActualizarCliente.add(txt_AgregarCliente);

        sidebar.add(btn_ActualizarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 380, 60));

        background.add(sidebar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 815));

        main_Screen1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_bar.setBackground(new java.awt.Color(132, 189, 0));
        title_bar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs.setText("BitFix / Reportes");
        title_bar.add(txt_breadcrumbs, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo.setText("Agregar Reporte");
        title_bar.add(txt_Titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen1.add(title_bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_Empleado.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Empleado.setText("Empleado:");
        main_Screen1.add(txt_Empleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, -1, -1));

        combo_Empleados.setEditable(true);
        combo_Empleados.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Empleados.setFocusCycleRoot(true);
        combo_Empleados.setNextFocusableComponent(combo_Clientes);
        combo_Empleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                combo_EmpleadosMousePressed(evt);
            }
        });
        combo_Empleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_EmpleadosActionPerformed(evt);
            }
        });
        main_Screen1.add(combo_Empleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 240, 35));

        txt_Cliente.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Cliente.setText("Cliente:");
        main_Screen1.add(txt_Cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        combo_Clientes.setEditable(true);
        combo_Clientes.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Clientes.setNextFocusableComponent(combo_Equipo);
        main_Screen1.add(combo_Clientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, 240, 35));

        txt_Equipo.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Equipo.setText("Equipo:");
        main_Screen1.add(txt_Equipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, -1, -1));

        combo_Equipo.setEditable(true);
        combo_Equipo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Equipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laptop", "Celular", "Tablet", "PC", "Consola de Videojuegos", "Disco duro / Memoria", "Impresora", "Multifuncional", "Proyector", "Cámara", "All-in-One", " " }));
        combo_Equipo.setSelectedIndex(-1);
        combo_Equipo.setNextFocusableComponent(txtfield_Marca);
        main_Screen1.add(combo_Equipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 240, 35));

        txt_Marca.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Marca.setText("Marca:");
        main_Screen1.add(txt_Marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 450, -1, -1));

        txtfield_Marca.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Marca.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtfield_Marca.setNextFocusableComponent(txtfield_Modelo);
        main_Screen1.add(txtfield_Marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 450, 240, 35));

        txt_Modelo.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Modelo.setText("Modelo:");
        main_Screen1.add(txt_Modelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 510, -1, -1));

        txtfield_Modelo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Modelo.setNextFocusableComponent(txtfield_nsEquipo);
        main_Screen1.add(txtfield_Modelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 510, 240, 35));

        txt_nsEquipo.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_nsEquipo.setText("N.S. Equipo:");
        main_Screen1.add(txt_nsEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 570, -1, -1));

        screen_Anticipo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        screen_Anticipo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Anticipo.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Anticipo.setText("¿Anticipo?");
        screen_Anticipo.add(txt_Anticipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 25, -1, -1));

        radio_Si.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        radio_Si.setText("Si");
        radio_Si.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_SiActionPerformed(evt);
            }
        });
        screen_Anticipo.add(radio_Si, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 82, -1, -1));

        txt_Peso.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        txt_Peso.setText("$");
        screen_Anticipo.add(txt_Peso, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 136, -1, -1));

        txtfield_Anticipo.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        txtfield_Anticipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtfield_AnticipoActionPerformed(evt);
            }
        });
        screen_Anticipo.add(txtfield_Anticipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 128, 216, 39));

        radio_No.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        radio_No.setText("No");
        radio_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_NoActionPerformed(evt);
            }
        });
        screen_Anticipo.add(radio_No, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 185, -1, -1));

        btn_OK.setBackground(new java.awt.Color(45, 45, 45));
        btn_OK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_OKMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_OKMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_OKMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_OKMouseReleased(evt);
            }
        });
        btn_OK.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("OK");
        btn_OK.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 11, -1, -1));

        screen_Anticipo.add(btn_OK, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 219, 100, 50));

        main_Screen1.add(screen_Anticipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 470, 290));

        screen_Servicios.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        screen_Servicios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Titulo1_1.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Titulo1_1.setText("Selecciona los servicios");
        screen_Servicios.add(txt_Titulo1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 18, 225, -1));

        chk_Servicio1.setSelected(true);
        chk_Servicio1.setEnabled(false);
        screen_Servicios.add(chk_Servicio1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 69, -1, 18));

        combo_Servicios1_1.setEditable(true);
        combo_Servicios1_1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios1_1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga" }));
        combo_Servicios1_1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios1_1.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios1_1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Servicios1_1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        screen_Servicios.add(combo_Servicios1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 290, 36));

        txt_Simbolo1_1.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_1.setText("$");
        screen_Servicios.add(txt_Simbolo1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 62, -1, -1));

        txtfield_Precio1_1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio1_1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio1_1.setText("180");
        txtfield_Precio1_1.setNextFocusableComponent(btn_Agregar);
        screen_Servicios.add(txtfield_Precio1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 170, 36));

        chk_Servicio1_2.setSelected(true);
        chk_Servicio1_2.setEnabled(false);
        screen_Servicios.add(chk_Servicio1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 119, -1, 18));

        combo_Servicios1_2.setEditable(true);
        combo_Servicios1_2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios1_2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga" }));
        combo_Servicios1_2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios1_2.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios1_2.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Servicios1_2PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        screen_Servicios.add(combo_Servicios1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 290, 36));

        txt_Simbolo1_2.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_2.setText("$");
        screen_Servicios.add(txt_Simbolo1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 112, -1, -1));

        txtfield_Precio1_2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio1_2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio1_2.setText("490");
        txtfield_Precio1_2.setNextFocusableComponent(btn_Agregar);
        screen_Servicios.add(txtfield_Precio1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 170, 36));

        chk_Servicio1_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_Servicio1_3ActionPerformed(evt);
            }
        });
        screen_Servicios.add(chk_Servicio1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 169, -1, 18));

        combo_Servicios1_3.setEditable(true);
        combo_Servicios1_3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios1_3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga" }));
        combo_Servicios1_3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios1_3.setEnabled(false);
        combo_Servicios1_3.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios1_3.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Servicios1_3PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        screen_Servicios.add(combo_Servicios1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 290, 36));

        txt_Simbolo1_3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_3.setText("$");
        screen_Servicios.add(txt_Simbolo1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 162, -1, -1));

        txtfield_Precio1_3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio1_3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio1_3.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtfield_Precio1_3.setEnabled(false);
        txtfield_Precio1_3.setNextFocusableComponent(btn_Agregar);
        screen_Servicios.add(txtfield_Precio1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 170, 36));

        chk_Servicio1_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_Servicio1_4ActionPerformed(evt);
            }
        });
        screen_Servicios.add(chk_Servicio1_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 219, -1, 18));

        combo_Servicios1_4.setEditable(true);
        combo_Servicios1_4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios1_4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga" }));
        combo_Servicios1_4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios1_4.setEnabled(false);
        combo_Servicios1_4.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios1_4.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Servicios1_4PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        screen_Servicios.add(combo_Servicios1_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 290, 36));

        txt_Simbolo1_4.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_4.setText("$");
        screen_Servicios.add(txt_Simbolo1_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 212, -1, -1));

        txtfield_Precio1_4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio1_4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio1_4.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtfield_Precio1_4.setEnabled(false);
        txtfield_Precio1_4.setNextFocusableComponent(btn_Agregar);
        screen_Servicios.add(txtfield_Precio1_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 210, 170, 36));

        btn_Agregar1_1.setBackground(new java.awt.Color(45, 45, 45));
        btn_Agregar1_1.setForeground(new java.awt.Color(255, 255, 255));
        btn_Agregar1_1.setToolTipText("");
        btn_Agregar1_1.setName(""); // NOI18N
        btn_Agregar1_1.setNextFocusableComponent(btn_Imprimir);
        btn_Agregar1_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Agregar1_1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Agregar1_1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Agregar1_1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Agregar1_1MouseReleased(evt);
            }
        });
        btn_Agregar1_1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Agregar4.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Agregar4.setForeground(new java.awt.Color(255, 255, 255));
        txt_Agregar4.setText("Agregar");
        btn_Agregar1_1.add(txt_Agregar4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        screen_Servicios.add(btn_Agregar1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 270, 160, 50));

        main_Screen1.add(screen_Servicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 640, 350));

        txtfield_nsEquipo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_nsEquipo.setNextFocusableComponent(txtfield_nsCargador);
        main_Screen1.add(txtfield_nsEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 570, 240, 35));

        txt_nsCargador.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_nsCargador.setText("N.S. Cargador:");
        main_Screen1.add(txt_nsCargador, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 630, -1, -1));

        txtfield_nsCargador.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_nsCargador.setNextFocusableComponent(txtfield_nsBateria);
        main_Screen1.add(txtfield_nsCargador, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 630, 240, 35));

        txt_nsBateria.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_nsBateria.setText("N.S. Batería:");
        main_Screen1.add(txt_nsBateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 690, -1, -1));

        txtfield_nsBateria.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_nsBateria.setText("INTERNA");
        txtfield_nsBateria.setNextFocusableComponent(txtArea_Fallas);
        main_Screen1.add(txtfield_nsBateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 690, 240, 35));

        txt_Fallas.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Fallas.setText("Fallas:");
        main_Screen1.add(txt_Fallas, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 270, -1, -1));

        txtArea_Fallas.setColumns(20);
        txtArea_Fallas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtArea_Fallas.setRows(5);
        txtArea_Fallas.setNextFocusableComponent(txtfield_Respaldo);
        scroll_Fallas.setViewportView(txtArea_Fallas);

        main_Screen1.add(scroll_Fallas, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 270, 250, -1));

        txt_Respaldo.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Respaldo.setText("Respaldo:");
        main_Screen1.add(txt_Respaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 390, -1, -1));

        txtfield_Respaldo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Respaldo.setEnabled(false);
        txtfield_Respaldo.setNextFocusableComponent(txtfield_Prediag);
        main_Screen1.add(txtfield_Respaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 390, 250, 35));

        txt_Prediag.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Prediag.setText("Prediag:");
        main_Screen1.add(txt_Prediag, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 450, -1, -1));

        txtfield_Prediag.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Prediag.setEnabled(false);
        txtfield_Prediag.setNextFocusableComponent(txtArea_Obs);
        txtfield_Prediag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtfield_PrediagMousePressed(evt);
            }
        });
        main_Screen1.add(txtfield_Prediag, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 450, 250, 35));

        txt_Obs.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Obs.setText("Obs:");
        main_Screen1.add(txt_Obs, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 510, -1, -1));

        txtArea_Obs.setColumns(20);
        txtArea_Obs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtArea_Obs.setRows(5);
        txtArea_Obs.setNextFocusableComponent(combo_Servicios);
        scroll_Obs.setViewportView(txtArea_Obs);

        main_Screen1.add(scroll_Obs, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, 250, -1));

        txt_Servicios.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Servicios.setText("Servicios:");
        main_Screen1.add(txt_Servicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 630, -1, -1));

        combo_Servicios.setEditable(true);
        combo_Servicios.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga", "Múltiples Entradas" }));
        combo_Servicios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_ServiciosPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        main_Screen1.add(combo_Servicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 630, 250, 35));

        txt_Precio.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Precio.setText("Precio:");
        main_Screen1.add(txt_Precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 690, -1, -1));

        txt_Simbolo.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo.setText("$");
        main_Screen1.add(txt_Simbolo, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 690, -1, -1));

        txtfield_Precio.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio.setText("180");
        txtfield_Precio.setNextFocusableComponent(btn_Agregar);
        main_Screen1.add(txtfield_Precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 690, 230, 35));

        btn_Agregar.setBackground(new java.awt.Color(45, 45, 45));
        btn_Agregar.setForeground(new java.awt.Color(255, 255, 255));
        btn_Agregar.setToolTipText("");
        btn_Agregar.setName(""); // NOI18N
        btn_Agregar.setNextFocusableComponent(btn_Imprimir);
        btn_Agregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AgregarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AgregarMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_AgregarMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_AgregarMouseReleased(evt);
            }
        });
        btn_Agregar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Agregar.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Agregar.setForeground(new java.awt.Color(255, 255, 255));
        txt_Agregar.setText("Agregar");
        btn_Agregar.add(txt_Agregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        main_Screen1.add(btn_Agregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 740, 160, 50));

        btn_Imprimir.setBackground(new java.awt.Color(45, 45, 45));
        btn_Imprimir.setForeground(new java.awt.Color(255, 255, 255));
        btn_Imprimir.setToolTipText("");
        btn_Imprimir.setName(""); // NOI18N
        btn_Imprimir.setNextFocusableComponent(combo_Empleados);
        btn_Imprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ImprimirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ImprimirMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_ImprimirMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_ImprimirMouseReleased(evt);
            }
        });
        btn_Imprimir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Agregar1.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Agregar1.setForeground(new java.awt.Color(255, 255, 255));
        txt_Agregar1.setText("Imprimir");
        btn_Imprimir.add(txt_Agregar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 10, -1, -1));

        main_Screen1.add(btn_Imprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 740, 160, 50));

        background.add(main_Screen1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        main_Screen2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_bar2.setBackground(new java.awt.Color(136, 189, 0));
        title_bar2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs2.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs2.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs2.setText("BitFix / Clientes");
        title_bar2.add(txt_breadcrumbs2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo2.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo2.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo2.setText("Agregar Cliente");
        title_bar2.add(txt_Titulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen2.add(title_bar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_Nombre.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Nombre.setText("Nombre:");
        main_Screen2.add(txt_Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, -1, -1));

        txtfield_Nombre.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Nombre.setFocusCycleRoot(true);
        txtfield_Nombre.setNextFocusableComponent(txtfield_Calle);
        main_Screen2.add(txtfield_Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 300, 240, 40));

        txt_Calle.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Calle.setText("Calle:");
        main_Screen2.add(txt_Calle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, -1));

        txtfield_Calle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Calle.setNextFocusableComponent(txtfield_numeroCasa);
        main_Screen2.add(txtfield_Calle, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, 240, 40));

        txt_numeroCasa.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_numeroCasa.setText("Número:");
        main_Screen2.add(txt_numeroCasa, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, -1, -1));

        txtfield_numeroCasa.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_numeroCasa.setNextFocusableComponent(txtfield_Colonia);
        main_Screen2.add(txtfield_numeroCasa, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 470, 240, 40));

        txt_Colonia.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Colonia.setText("Colonia:");
        main_Screen2.add(txt_Colonia, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 560, -1, -1));

        txtfield_Colonia.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Colonia.setNextFocusableComponent(combo_Ciudad);
        main_Screen2.add(txtfield_Colonia, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 560, 240, 40));

        txt_Ciudad.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Ciudad.setText("Ciudad:");
        main_Screen2.add(txt_Ciudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, -1, -1));

        combo_Ciudad.setEditable(true);
        combo_Ciudad.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        combo_Ciudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AGS", "ASIENTOS", "CDMX", "DELICIAS", "ENCARNACION DE DIAZ", "GUADALUPE", "JESUS MARIA", "NOCHISTLAN", "PABELLON DE ARTEAGA", "RINCON DE ROMOS", "SAN FRANCISCO DE LOS ROMO", "SAN JOSE DE GRACIA", "TLALNEPANTLA", "ZACATECAS", "ZAPOPAN", " ", " " }));
        combo_Ciudad.setNextFocusableComponent(txtfield_Telefono);
        main_Screen2.add(combo_Ciudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 300, 240, 40));

        txt_telefono.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_telefono.setText("Teléfono:");
        main_Screen2.add(txt_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 380, -1, -1));

        txtfield_Telefono.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Telefono.setNextFocusableComponent(txtfield_Telefono2);
        main_Screen2.add(txtfield_Telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 380, 240, 40));

        txt_telefono2.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_telefono2.setText("Teléfono 2:");
        main_Screen2.add(txt_telefono2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 470, -1, -1));

        txtfield_Telefono2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Telefono2.setNextFocusableComponent(txtfield_Correo);
        main_Screen2.add(txtfield_Telefono2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 470, 240, 40));

        txt_Correo.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Correo.setText("Correo:");
        main_Screen2.add(txt_Correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 560, -1, -1));

        txtfield_Correo.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Correo.setNextFocusableComponent(txtfield_Nombre);
        main_Screen2.add(txtfield_Correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 560, 240, 40));

        btn_Agregar2.setBackground(new java.awt.Color(45, 45, 45));
        btn_Agregar2.setForeground(new java.awt.Color(255, 255, 255));
        btn_Agregar2.setToolTipText("");
        btn_Agregar2.setName(""); // NOI18N
        btn_Agregar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Agregar2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Agregar2MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Agregar2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Agregar2MouseReleased(evt);
            }
        });
        btn_Agregar2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Agregar2.setFont(new java.awt.Font("Berlin Sans FB", 0, 36)); // NOI18N
        txt_Agregar2.setForeground(new java.awt.Color(255, 255, 255));
        txt_Agregar2.setText("Agregar");
        btn_Agregar2.add(txt_Agregar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        main_Screen2.add(btn_Agregar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 680, 180, 70));

        background.add(main_Screen2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        main_Screen3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_bar3.setBackground(new java.awt.Color(136, 189, 0));
        title_bar3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs3.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs3.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs3.setText("BitFix / Reportes");
        title_bar3.add(txt_breadcrumbs3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo3.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo3.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo3.setText("Ver Reportes");
        title_bar3.add(txt_Titulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen3.add(title_bar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_Empleado3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Empleado3.setText("Empleado:");
        main_Screen3.add(txt_Empleado3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, -1, -1));

        combo_Empleados3.setEditable(true);
        combo_Empleados3.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Empleados3.setNextFocusableComponent(combo_Clientes3);
        main_Screen3.add(combo_Empleados3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 240, 35));

        txt_Cliente3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Cliente3.setText("Cliente:");
        main_Screen3.add(txt_Cliente3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, -1, -1));

        combo_Clientes3.setEditable(true);
        combo_Clientes3.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Clientes3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_Clientes3ActionPerformed(evt);
            }
        });
        main_Screen3.add(combo_Clientes3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 240, 35));

        txt_Tipo3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Tipo3.setText("Tipo:");
        main_Screen3.add(txt_Tipo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 250, -1, -1));

        combo_Tipo3.setEditable(true);
        combo_Tipo3.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Tipo3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laptop", "Celular", "Tablet", "PC", "Consola de Videojuegos", "Disco duro / Memoria", "Impresora", "Multifuncional", "Proyector", "Cámara", "All-in-One" }));
        combo_Tipo3.setSelectedIndex(-1);
        main_Screen3.add(combo_Tipo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, 240, 35));

        txt_Marca3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Marca3.setText("Marca:");
        main_Screen3.add(txt_Marca3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, -1, -1));

        txtfield_Marca3.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        main_Screen3.add(txtfield_Marca3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 310, 240, 35));

        btn_Buscar3.setBackground(new java.awt.Color(45, 45, 45));
        btn_Buscar3.setForeground(new java.awt.Color(255, 255, 255));
        btn_Buscar3.setToolTipText("");
        btn_Buscar3.setName(""); // NOI18N
        btn_Buscar3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Buscar3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Buscar3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Buscar3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Buscar3MouseReleased(evt);
            }
        });
        btn_Buscar3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Buscar.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar.setText("Buscar");
        btn_Buscar3.add(txt_Buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        main_Screen3.add(btn_Buscar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 370, 110, 50));

        btn_Resetear3.setBackground(new java.awt.Color(45, 45, 45));
        btn_Resetear3.setForeground(new java.awt.Color(255, 255, 255));
        btn_Resetear3.setToolTipText("");
        btn_Resetear3.setName(""); // NOI18N
        btn_Resetear3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Resetear3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Resetear3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Resetear3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Resetear3MouseReleased(evt);
            }
        });
        btn_Resetear3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar7.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Buscar7.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar7.setText("Resetear");
        btn_Resetear3.add(txt_Buscar7, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 10, -1, -1));

        main_Screen3.add(btn_Resetear3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 370, 110, 50));

        table_Reportes.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        table_Reportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "Cliente", "Equipo", "Fallas", "Obs", "Servicios", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Reportes.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table_Reportes);
        if (table_Reportes.getColumnModel().getColumnCount() > 0) {
            table_Reportes.getColumnModel().getColumn(0).setResizable(false);
            table_Reportes.getColumnModel().getColumn(0).setPreferredWidth(1);
            table_Reportes.getColumnModel().getColumn(7).setResizable(false);
            table_Reportes.getColumnModel().getColumn(7).setPreferredWidth(4);
        }

        main_Screen3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 910, 380));

        btn_Imprimir3.setBackground(new java.awt.Color(45, 45, 45));
        btn_Imprimir3.setForeground(new java.awt.Color(255, 255, 255));
        btn_Imprimir3.setToolTipText("");
        btn_Imprimir3.setName(""); // NOI18N
        btn_Imprimir3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Imprimir3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Imprimir3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Imprimir3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Imprimir3MouseReleased(evt);
            }
        });
        btn_Imprimir3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar10.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Buscar10.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar10.setText("Imprimir");
        btn_Imprimir3.add(txt_Buscar10, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 10, -1, -1));

        main_Screen3.add(btn_Imprimir3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 370, 110, 50));

        txt_Marca4.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Marca4.setText("ID:");
        main_Screen3.add(txt_Marca4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 370, -1, -1));

        txtfield_ID3.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        main_Screen3.add(txtfield_ID3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 370, 240, 35));

        background.add(main_Screen3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        main_Screen4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_bar4.setBackground(new java.awt.Color(136, 189, 0));
        title_bar4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs4.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs4.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs4.setText("BitFix / Reportes");
        title_bar4.add(txt_breadcrumbs4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo4.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo4.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo4.setText("Agregar Solución");
        title_bar4.add(txt_Titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen4.add(title_bar4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_ID.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_ID.setText("ID:");
        main_Screen4.add(txt_ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 310, -1, -1));

        txtfield_ID.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_ID.setNextFocusableComponent(txtArea_Solucion);
        main_Screen4.add(txtfield_ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 310, 240, 40));

        txt_Solucion.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Solucion.setText("Solución:");
        main_Screen4.add(txt_Solucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 410, -1, -1));

        txtArea_Solucion.setColumns(20);
        txtArea_Solucion.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtArea_Solucion.setRows(5);
        jScrollPane2.setViewportView(txtArea_Solucion);

        main_Screen4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 410, 470, 200));

        btn_Agregar4.setBackground(new java.awt.Color(45, 45, 45));
        btn_Agregar4.setForeground(new java.awt.Color(255, 255, 255));
        btn_Agregar4.setToolTipText("");
        btn_Agregar4.setName(""); // NOI18N
        btn_Agregar4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Agregar4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Agregar4MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Agregar4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Agregar4MouseReleased(evt);
            }
        });
        btn_Agregar4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Buscar1.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar1.setText("Agregar");
        btn_Agregar4.add(txt_Buscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        main_Screen4.add(btn_Agregar4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 630, 150, 60));

        background.add(main_Screen4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        main_Screen5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_bar5.setBackground(new java.awt.Color(136, 189, 0));
        title_bar5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs5.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs5.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs5.setText("BitFix / Reportes");
        title_bar5.add(txt_breadcrumbs5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo5.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo5.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo5.setText("Ver Soluciones");
        title_bar5.add(txt_Titulo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen5.add(title_bar5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_Cliente5.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Cliente5.setText("Cliente:");
        main_Screen5.add(txt_Cliente5, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 300, -1, -1));

        combo_Clientes5.setEditable(true);
        combo_Clientes5.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Clientes5.setNextFocusableComponent(txtfield_ID1);
        main_Screen5.add(combo_Clientes5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 300, 240, 40));

        txt_ID1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_ID1.setText("ID:");
        main_Screen5.add(txt_ID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 300, -1, -1));

        txtfield_ID1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        main_Screen5.add(txtfield_ID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 240, 40));

        txt_Tipo5.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Tipo5.setText("Tipo:");
        main_Screen5.add(txt_Tipo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, -1, -1));

        combo_Tipo5.setEditable(true);
        combo_Tipo5.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Tipo5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laptop", "Celular", "Tablet", "PC", "Consola de Videojuegos", "Disco duro / Memoria", "Impresora", "Multifuncional", "Proyector", "Cámara", "All-in-One" }));
        combo_Tipo5.setSelectedIndex(-1);
        combo_Tipo5.setNextFocusableComponent(btn_Buscar5);
        main_Screen5.add(combo_Tipo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, 240, 40));

        btn_Buscar5.setBackground(new java.awt.Color(45, 45, 45));
        btn_Buscar5.setForeground(new java.awt.Color(255, 255, 255));
        btn_Buscar5.setToolTipText("");
        btn_Buscar5.setName(""); // NOI18N
        btn_Buscar5.setNextFocusableComponent(txtfield_ID1);
        btn_Buscar5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Buscar5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Buscar5MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Buscar5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Buscar5MouseReleased(evt);
            }
        });
        btn_Buscar5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar2.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        txt_Buscar2.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar2.setText("Buscar");
        btn_Buscar5.add(txt_Buscar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 8, -1, -1));

        main_Screen5.add(btn_Buscar5, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 400, 120, 50));

        btn_Resetear5.setBackground(new java.awt.Color(45, 45, 45));
        btn_Resetear5.setForeground(new java.awt.Color(255, 255, 255));
        btn_Resetear5.setToolTipText("");
        btn_Resetear5.setName(""); // NOI18N
        btn_Resetear5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Resetear5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Resetear5MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Resetear5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Resetear5MouseReleased(evt);
            }
        });
        btn_Resetear5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar8.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Buscar8.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar8.setText("Resetear");
        btn_Resetear5.add(txt_Buscar8, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 10, -1, -1));

        main_Screen5.add(btn_Resetear5, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 400, 110, 50));

        table_Soluciones.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        table_Soluciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "Cliente", "Equipo", "Solución"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Soluciones.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(table_Soluciones);
        if (table_Soluciones.getColumnModel().getColumnCount() > 0) {
            table_Soluciones.getColumnModel().getColumn(3).setResizable(false);
        }

        main_Screen5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 890, 300));

        background.add(main_Screen5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        main_Screen6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        screen_Servicios6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        screen_Servicios6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Titulo1_2.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Titulo1_2.setText("Agrega los servicios");
        screen_Servicios6.add(txt_Titulo1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 200, -1));

        chk_Servicio6_1.setSelected(true);
        chk_Servicio6_1.setEnabled(false);
        screen_Servicios6.add(chk_Servicio6_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 69, -1, 18));

        txtfield_Servicios6_1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Servicios6_1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtfield_Servicios6_1.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Servicios6_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 290, 36));

        txt_Simbolo1_5.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_5.setText("$");
        screen_Servicios6.add(txt_Simbolo1_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 62, -1, -1));

        txtfield_Precio6_1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio6_1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio6_1.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Precio6_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 170, 36));

        chk_Servicio6_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_Servicio6_2ActionPerformed(evt);
            }
        });
        screen_Servicios6.add(chk_Servicio6_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 119, -1, 18));

        txtfield_Servicios6_2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Servicios6_2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtfield_Servicios6_2.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Servicios6_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 290, 36));

        combo_Servicios6_2.setEditable(true);
        combo_Servicios6_2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios6_2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga" }));
        combo_Servicios6_2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios6_2.setEnabled(false);
        combo_Servicios6_2.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios6_2.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Servicios6_2PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        screen_Servicios6.add(combo_Servicios6_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 290, 36));

        txt_Simbolo1_6.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_6.setText("$");
        screen_Servicios6.add(txt_Simbolo1_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 112, -1, -1));

        txtfield_Precio6_2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio6_2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio6_2.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Precio6_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 170, 36));

        chk_Servicio6_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_Servicio6_3ActionPerformed(evt);
            }
        });
        screen_Servicios6.add(chk_Servicio6_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 169, -1, 18));

        txtfield_Servicios6_3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Servicios6_3.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtfield_Servicios6_3.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Servicios6_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 290, 36));

        combo_Servicios6_3.setEditable(true);
        combo_Servicios6_3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios6_3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga" }));
        combo_Servicios6_3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios6_3.setEnabled(false);
        combo_Servicios6_3.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios6_3.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Servicios6_3PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        screen_Servicios6.add(combo_Servicios6_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 290, 36));

        txt_Simbolo1_7.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_7.setText("$");
        screen_Servicios6.add(txt_Simbolo1_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 162, -1, -1));

        txtfield_Precio6_3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio6_3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio6_3.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtfield_Precio6_3.setEnabled(false);
        txtfield_Precio6_3.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Precio6_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 170, 36));

        chk_Servicio6_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_Servicio6_4ActionPerformed(evt);
            }
        });
        screen_Servicios6.add(chk_Servicio6_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 219, -1, 18));

        txtfield_Servicios6_4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Servicios6_4.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtfield_Servicios6_4.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Servicios6_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 290, 36));

        combo_Servicios6_4.setEditable(true);
        combo_Servicios6_4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Servicios6_4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Servicio de Diagnóstico", "Carga de Software del Cliente", "Carga de Sistema Windows", "Carga de Office", "Carga de Sistema Mac", "Carga de Sistema Android", "Carga de Sistema iOS", "Mantenimiento General", "Cambio de Pantalla", "Cambio de Bateria", "Cambio de Teclado", "Cambio de Centro de Carga" }));
        combo_Servicios6_4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        combo_Servicios6_4.setEnabled(false);
        combo_Servicios6_4.setNextFocusableComponent(txtfield_Precio);
        combo_Servicios6_4.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Servicios6_4PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        screen_Servicios6.add(combo_Servicios6_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 290, 36));

        txt_Simbolo1_8.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Simbolo1_8.setText("$");
        screen_Servicios6.add(txt_Simbolo1_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 212, -1, -1));

        txtfield_Precio6_4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtfield_Precio6_4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtfield_Precio6_4.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtfield_Precio6_4.setEnabled(false);
        txtfield_Precio6_4.setNextFocusableComponent(btn_Agregar);
        screen_Servicios6.add(txtfield_Precio6_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 210, 170, 36));

        btn_Agregar6_1.setBackground(new java.awt.Color(45, 45, 45));
        btn_Agregar6_1.setForeground(new java.awt.Color(255, 255, 255));
        btn_Agregar6_1.setToolTipText("");
        btn_Agregar6_1.setName(""); // NOI18N
        btn_Agregar6_1.setNextFocusableComponent(btn_Imprimir);
        btn_Agregar6_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Agregar6_1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Agregar6_1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Agregar6_1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Agregar6_1MouseReleased(evt);
            }
        });
        btn_Agregar6_1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Agregar5.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Agregar5.setForeground(new java.awt.Color(255, 255, 255));
        txt_Agregar5.setText("Agregar");
        btn_Agregar6_1.add(txt_Agregar5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        screen_Servicios6.add(btn_Agregar6_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 270, 160, 50));

        main_Screen6.add(screen_Servicios6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 640, 350));

        title_bar6.setBackground(new java.awt.Color(136, 189, 0));
        title_bar6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs6.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs6.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs6.setText("BitFix / Reportes");
        title_bar6.add(txt_breadcrumbs6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo6.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo6.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo6.setText("Ver / Actualizar Status");
        title_bar6.add(txt_Titulo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen6.add(title_bar6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_Cliente6.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Cliente6.setText("Cliente:");
        main_Screen6.add(txt_Cliente6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, -1, -1));

        combo_Clientes6.setEditable(true);
        combo_Clientes6.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Clientes6.setNextFocusableComponent(txtfield_ID6);
        main_Screen6.add(combo_Clientes6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 240, 40));

        txt_ID2.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_ID2.setText("ID:");
        main_Screen6.add(txt_ID2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 290, -1, -1));

        txtfield_ID6.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_ID6.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtfield_ID6.setNextFocusableComponent(combo_Status1);
        main_Screen6.add(txtfield_ID6, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 290, 240, 40));

        txt_Status1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Status1.setText("Status:");
        main_Screen6.add(txt_Status1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, -1, -1));

        combo_Status1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Status1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entregado", "Pendiente", "Terminado", "No Entregado" }));
        combo_Status1.setSelectedIndex(-1);
        combo_Status1.setNextFocusableComponent(btn_Buscar6);
        main_Screen6.add(combo_Status1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 240, 40));

        btn_Buscar6.setBackground(new java.awt.Color(45, 45, 45));
        btn_Buscar6.setForeground(new java.awt.Color(255, 255, 255));
        btn_Buscar6.setToolTipText("");
        btn_Buscar6.setName(""); // NOI18N
        btn_Buscar6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Buscar6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Buscar6MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Buscar6MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Buscar6MouseReleased(evt);
            }
        });
        btn_Buscar6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Buscar3.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar3.setText("Buscar");
        btn_Buscar6.add(txt_Buscar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 10, -1, -1));

        main_Screen6.add(btn_Buscar6, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 120, 50));

        btn_Resetear6.setBackground(new java.awt.Color(45, 45, 45));
        btn_Resetear6.setForeground(new java.awt.Color(255, 255, 255));
        btn_Resetear6.setToolTipText("");
        btn_Resetear6.setName(""); // NOI18N
        btn_Resetear6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Resetear6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Resetear6MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Resetear6MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Resetear6MouseReleased(evt);
            }
        });
        btn_Resetear6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar9.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        txt_Buscar9.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar9.setText("Resetear");
        btn_Resetear6.add(txt_Buscar9, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 10, -1, -1));

        main_Screen6.add(btn_Resetear6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 350, 110, 50));

        table_Status.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        table_Status.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "Cliente", "Equipo", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Status.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(table_Status);
        if (table_Status.getColumnModel().getColumnCount() > 0) {
            table_Status.getColumnModel().getColumn(3).setResizable(false);
        }

        main_Screen6.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 890, 190));

        txt_Status.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Status.setText("Status:");
        main_Screen6.add(txt_Status, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 670, 100, -1));

        combo_Status.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entregado", "Pendiente", "Terminado", "No Entregado" }));
        combo_Status.setNextFocusableComponent(btn_Actualizar);
        main_Screen6.add(combo_Status, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 670, 250, 40));

        btn_Actualizar.setBackground(new java.awt.Color(45, 45, 45));
        btn_Actualizar.setForeground(new java.awt.Color(255, 255, 255));
        btn_Actualizar.setToolTipText("");
        btn_Actualizar.setName(""); // NOI18N
        btn_Actualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ActualizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ActualizarMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_ActualizarMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_ActualizarMouseReleased(evt);
            }
        });
        btn_Actualizar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar4.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        txt_Buscar4.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar4.setText("Actualizar");
        btn_Actualizar.add(txt_Buscar4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        main_Screen6.add(btn_Actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 660, 140, 70));

        btn_AgregarServicios.setBackground(new java.awt.Color(45, 45, 45));
        btn_AgregarServicios.setForeground(new java.awt.Color(255, 255, 255));
        btn_AgregarServicios.setToolTipText("");
        btn_AgregarServicios.setName(""); // NOI18N
        btn_AgregarServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AgregarServiciosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AgregarServiciosMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_AgregarServiciosMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_AgregarServiciosMouseReleased(evt);
            }
        });
        btn_AgregarServicios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar11.setFont(new java.awt.Font("Berlin Sans FB", 0, 25)); // NOI18N
        txt_Buscar11.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar11.setText("Servicios");
        btn_AgregarServicios.add(txt_Buscar11, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 33, -1, -1));

        txt_Buscar12.setFont(new java.awt.Font("Berlin Sans FB", 0, 25)); // NOI18N
        txt_Buscar12.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar12.setText("Agregar");
        btn_AgregarServicios.add(txt_Buscar12, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 6, -1, -1));

        main_Screen6.add(btn_AgregarServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 660, 140, 70));

        background.add(main_Screen6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        main_Screen7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_bar7.setBackground(new java.awt.Color(136, 189, 0));
        title_bar7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs7.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs7.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs7.setText("BitFix / Empleados");
        title_bar7.add(txt_breadcrumbs7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo7.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo7.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo7.setText("Administrar Empleados");
        title_bar7.add(txt_Titulo7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen7.add(title_bar7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_Empleado7.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Empleado7.setText("Empleado:");
        main_Screen7.add(txt_Empleado7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 350, -1, -1));

        combo_Empleados7.setEditable(true);
        combo_Empleados7.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        combo_Empleados7.setNextFocusableComponent(btn_Eliminar7);
        main_Screen7.add(combo_Empleados7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 350, 240, 40));

        btn_Eliminar7.setBackground(new java.awt.Color(45, 45, 45));
        btn_Eliminar7.setForeground(new java.awt.Color(255, 255, 255));
        btn_Eliminar7.setToolTipText("");
        btn_Eliminar7.setName(""); // NOI18N
        btn_Eliminar7.setNextFocusableComponent(txtfield_Nombre7);
        btn_Eliminar7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Eliminar7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Eliminar7MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Eliminar7MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Eliminar7MouseReleased(evt);
            }
        });
        btn_Eliminar7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar5.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        txt_Buscar5.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar5.setText("Eliminar");
        btn_Eliminar7.add(txt_Buscar5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, -1));

        main_Screen7.add(btn_Eliminar7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 340, 140, 60));

        txtfield_Nombre7.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Nombre7.setNextFocusableComponent(btn_Agregar7);
        main_Screen7.add(txtfield_Nombre7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 620, 240, 40));

        txt_Nombre7.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Nombre7.setText("Nombre:");
        main_Screen7.add(txt_Nombre7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 620, -1, -1));

        btn_Agregar7.setBackground(new java.awt.Color(45, 45, 45));
        btn_Agregar7.setForeground(new java.awt.Color(255, 255, 255));
        btn_Agregar7.setToolTipText("");
        btn_Agregar7.setName(""); // NOI18N
        btn_Agregar7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Agregar7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Agregar7MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Agregar7MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Agregar7MouseReleased(evt);
            }
        });
        btn_Agregar7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Buscar6.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        txt_Buscar6.setForeground(new java.awt.Color(255, 255, 255));
        txt_Buscar6.setText("Agregar");
        btn_Agregar7.add(txt_Buscar6, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 13, -1, -1));

        main_Screen7.add(btn_Agregar7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 610, 150, 60));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("______________________________________________________________________________");
        main_Screen7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 460, -1, -1));

        background.add(main_Screen7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        main_Screen8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title_bar8.setBackground(new java.awt.Color(136, 189, 0));
        title_bar8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_breadcrumbs8.setFont(new java.awt.Font("Eras Demi ITC", 2, 20)); // NOI18N
        txt_breadcrumbs8.setForeground(new java.awt.Color(255, 255, 255));
        txt_breadcrumbs8.setText("BitFix / Clientes");
        title_bar8.add(txt_breadcrumbs8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        txt_Titulo8.setFont(new java.awt.Font("Berlin Sans FB", 0, 60)); // NOI18N
        txt_Titulo8.setForeground(new java.awt.Color(255, 255, 255));
        txt_Titulo8.setText("Actualizar Cliente");
        title_bar8.add(txt_Titulo8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        main_Screen8.add(title_bar8, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 1300, 170));

        txt_Nombre1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Nombre1.setText("Nombre:");
        main_Screen8.add(txt_Nombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, -1, -1));

        combo_Clientes8.setEditable(true);
        combo_Clientes8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_Clientes8.setNextFocusableComponent(txtfield_Calle1);
        combo_Clientes8.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combo_Clientes8PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        main_Screen8.add(combo_Clientes8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 300, 240, 40));

        txt_Calle1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Calle1.setText("Calle:");
        main_Screen8.add(txt_Calle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, -1));

        txtfield_Calle1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Calle1.setNextFocusableComponent(txtfield_numeroCasa1);
        main_Screen8.add(txtfield_Calle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, 240, 40));

        txt_numeroCasa1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_numeroCasa1.setText("Número:");
        main_Screen8.add(txt_numeroCasa1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, -1, -1));

        txtfield_numeroCasa1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_numeroCasa1.setNextFocusableComponent(txtfield_Colonia1);
        main_Screen8.add(txtfield_numeroCasa1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 470, 240, 40));

        txt_Colonia1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Colonia1.setText("Colonia:");
        main_Screen8.add(txt_Colonia1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 560, -1, -1));

        txtfield_Colonia1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Colonia1.setNextFocusableComponent(txtfield_Ciudad1);
        main_Screen8.add(txtfield_Colonia1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 560, 240, 40));

        txt_Ciudad1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Ciudad1.setText("Ciudad:");
        main_Screen8.add(txt_Ciudad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, -1, -1));

        txtfield_Ciudad1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Ciudad1.setNextFocusableComponent(txtfield_Telefono1);
        main_Screen8.add(txtfield_Ciudad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 300, 240, 40));

        txt_telefono1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_telefono1.setText("Teléfono:");
        main_Screen8.add(txt_telefono1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 380, -1, -1));

        txtfield_Telefono1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Telefono1.setNextFocusableComponent(txtfield_Telefono4);
        main_Screen8.add(txtfield_Telefono1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 380, 240, 40));

        txt_telefono3.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_telefono3.setText("Teléfono 2:");
        main_Screen8.add(txt_telefono3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 470, -1, -1));

        txtfield_Telefono4.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Telefono4.setNextFocusableComponent(txtfield_Correo1);
        main_Screen8.add(txtfield_Telefono4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 470, 240, 40));

        txt_Correo1.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Correo1.setText("Correo:");
        main_Screen8.add(txt_Correo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 560, -1, -1));

        txtfield_Correo1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtfield_Correo1.setNextFocusableComponent(btn_Actualizar8);
        main_Screen8.add(txtfield_Correo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 560, 240, 40));

        btn_Actualizar8.setBackground(new java.awt.Color(45, 45, 45));
        btn_Actualizar8.setForeground(new java.awt.Color(255, 255, 255));
        btn_Actualizar8.setToolTipText("");
        btn_Actualizar8.setName(""); // NOI18N
        btn_Actualizar8.setNextFocusableComponent(combo_Clientes8);
        btn_Actualizar8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_Actualizar8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_Actualizar8MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_Actualizar8MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_Actualizar8MouseReleased(evt);
            }
        });
        btn_Actualizar8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_Agregar3.setFont(new java.awt.Font("Berlin Sans FB", 0, 30)); // NOI18N
        txt_Agregar3.setForeground(new java.awt.Color(255, 255, 255));
        txt_Agregar3.setText("Actualizar");
        btn_Actualizar8.add(txt_Agregar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 18, -1, -1));

        main_Screen8.add(btn_Actualizar8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 680, 180, 70));

        background.add(main_Screen8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 910, 815));

        load_Screen.setBackground(new java.awt.Color(0, 0, 0));
        load_Screen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/BitFix_Load.png"))); // NOI18N
        load_Screen.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 670, 480));

        background.add(load_Screen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1290, 820));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void bgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bgFocusLost
    }//GEN-LAST:event_bgFocusLost

    private void btn_ActualizarClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarClienteMousePressed
        resetColor(btn_AgregarReporte);
        resetColor(btn_AgregarCliente);
        resetColor(btn_VerReporte);
        resetColor(btn_AgregarSolucion);
        resetColor(btn_VerSoluciones);
        resetColor(btn_ActualizarStatus);
        resetColor(btn_AdministrarEmpleados);
        setColor(btn_ActualizarCliente);
        
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(true);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
           
        modelComboEmpleados(combo_Empleados);
        modelComboEmpleados(combo_Empleados3);
        modelComboEmpleados(combo_Empleados7);
        modelComboClientes(combo_Clientes);
        modelComboClientes(combo_Clientes3);
        modelComboClientes(combo_Clientes5);
        modelComboClientes(combo_Clientes6);
        modelComboClientes(combo_Clientes8);
    }//GEN-LAST:event_btn_ActualizarClienteMousePressed

    private void btn_AgregarReporteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarReporteMousePressed
        setColor(btn_AgregarReporte);
        resetColor(btn_ActualizarCliente);
        resetColor(btn_VerReporte);
        resetColor(btn_AgregarSolucion);
        resetColor(btn_VerSoluciones);
        resetColor(btn_ActualizarStatus);
        resetColor(btn_AdministrarEmpleados);
        resetColor(btn_AgregarCliente);
        
        main_Screen1.setVisible(true);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
        
        modelComboEmpleados(combo_Empleados);
        modelComboEmpleados(combo_Empleados3);
        modelComboEmpleados(combo_Empleados7);
        modelComboClientes(combo_Clientes);
        modelComboClientes(combo_Clientes3);
        modelComboClientes(combo_Clientes5);
        modelComboClientes(combo_Clientes6);
        modelComboClientes(combo_Clientes8);
        
        combo_Empleados.setSelectedIndex(-1);
        combo_Clientes.setSelectedIndex(-1);
        combo_Equipo.setSelectedIndex(-1);
    }//GEN-LAST:event_btn_AgregarReporteMousePressed

    private void btn_VerReporteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_VerReporteMousePressed
        resetColor(btn_AgregarReporte);
        resetColor(btn_ActualizarCliente);
        setColor(btn_VerReporte);
        resetColor(btn_AgregarSolucion);
        resetColor(btn_VerSoluciones);
        resetColor(btn_ActualizarStatus);
        resetColor(btn_AdministrarEmpleados);
        resetColor(btn_AgregarCliente);
        
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(true);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
        
        modelComboEmpleados(combo_Empleados);
        modelComboEmpleados(combo_Empleados3);
        modelComboEmpleados(combo_Empleados7);
        modelComboClientes(combo_Clientes);
        modelComboClientes(combo_Clientes3);
        modelComboClientes(combo_Clientes5);
        modelComboClientes(combo_Clientes6);
        modelComboClientes(combo_Clientes8);
        
        combo_Empleados3.setSelectedIndex(-1);
        combo_Clientes3.setSelectedIndex(-1);
        combo_Tipo3.setSelectedIndex(-1);
        txtfield_Marca3.setText("");
    }//GEN-LAST:event_btn_VerReporteMousePressed

    private void btn_AgregarSolucionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarSolucionMousePressed
        resetColor(btn_AgregarReporte);
        resetColor(btn_ActualizarCliente);
        resetColor(btn_VerReporte);
        setColor(btn_AgregarSolucion);
        resetColor(btn_VerSoluciones);
        resetColor(btn_ActualizarStatus);
        resetColor(btn_AdministrarEmpleados);
        resetColor(btn_AgregarCliente);
        
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(true);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
    }//GEN-LAST:event_btn_AgregarSolucionMousePressed

    private void btn_VerSolucionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_VerSolucionesMousePressed
        resetColor(btn_AgregarReporte);
        resetColor(btn_ActualizarCliente);
        resetColor(btn_VerReporte);
        resetColor(btn_AgregarSolucion);
        setColor(btn_VerSoluciones);
        resetColor(btn_ActualizarStatus);
        resetColor(btn_AdministrarEmpleados);
        resetColor(btn_AgregarCliente);
        
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(true);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
        
        modelComboClientes(combo_Clientes5);
        
        combo_Clientes5.setSelectedIndex(-1);
        combo_Tipo5.setSelectedIndex(-1);
    }//GEN-LAST:event_btn_VerSolucionesMousePressed

    private void btn_AgregarReporteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarReporteMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_AgregarReporteMouseEntered

    private void btn_ActualizarClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarClienteMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_ActualizarClienteMouseEntered

    private void btn_VerReporteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_VerReporteMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_VerReporteMouseEntered

    private void btn_AgregarSolucionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarSolucionMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_AgregarSolucionMouseEntered

    private void btn_VerSolucionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_VerSolucionesMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_VerSolucionesMouseEntered

    private void btn_AdministrarEmpleadosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AdministrarEmpleadosMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_AdministrarEmpleadosMouseEntered

    private void btn_AgregarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarMousePressed
        btn_Agregar.setBackground(new Color(33,33,33)); 
        
        comp = 1;        
               
        if(combo_Empleados.getSelectedIndex() == -1 || combo_Clientes.getSelectedIndex() == -1 || combo_Equipo.getSelectedItem().toString().isEmpty() || txtArea_Fallas.getText().isEmpty() || (combo_Servicios.getSelectedIndex() != 12 && txtfield_Precio.getText().isEmpty())){
            
            JOptionPane.showMessageDialog(null, "Por favor llena los campos obligatorios");
            
            if(combo_Empleados.getSelectedIndex() == -1){
                combo_Empleados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }

            if(combo_Clientes.getSelectedIndex() == -1){
                combo_Clientes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }

            if(combo_Equipo.getSelectedIndex() == -1){
                combo_Equipo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }

            if(txtArea_Fallas.getText().isEmpty()){
                txtArea_Fallas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }

            if(txtfield_Precio.getText().isEmpty()){
                txtfield_Precio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }
        }
        else if(combo_Servicios.getSelectedIndex() == (12)){
            combo_Empleados.setBorder(defaultBorder);
            combo_Clientes.setBorder(defaultBorder);
            combo_Equipo.setBorder(defaultBorder);
            txtArea_Fallas.setBorder(defaultBorder);
            txtfield_Precio.setBorder(defaultBorder);
            
            screen_Servicios.setVisible(true);
            combo_Empleados.setVisible(false);
            combo_Clientes.setVisible(false);
            combo_Equipo.setVisible(false);
            txtfield_Marca.setVisible(false);
            txtfield_Modelo.setVisible(false);
            txtfield_nsEquipo.setVisible(false);
            
            combo_Servicios1_1.setSelectedIndex(0);
            combo_Servicios1_2.setSelectedIndex(1);
            
            txtfield_Precio1_1.setText("180");
            txtfield_Precio1_2.setText("350");
        }
        else {
            combo_Empleados.setBorder(defaultBorder);
            combo_Clientes.setBorder(defaultBorder);
            combo_Equipo.setBorder(defaultBorder);
            txtArea_Fallas.setBorder(defaultBorder);
            txtfield_Precio.setBorder(defaultBorder);
            
            screen_Anticipo.setVisible(true);
            combo_Clientes.setVisible(false);
            combo_Equipo.setVisible(false);
            txtfield_Marca.setVisible(false);
            txtfield_Modelo.setVisible(false);
            txtfield_nsEquipo.setVisible(false);   
            
            combo_Servicios1_1.setSelectedIndex(-1);
            combo_Servicios1_2.setSelectedIndex(-1);
        }   
        
        
    }//GEN-LAST:event_btn_AgregarMousePressed

    private void btn_AgregarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarMouseReleased
        btn_Agregar.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_AgregarMouseReleased

    private void btn_AgregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_AgregarMouseEntered

    private void btn_AgregarReporteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarReporteMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_AgregarReporteMouseExited

    private void btn_ActualizarClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarClienteMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_ActualizarClienteMouseExited

    private void btn_VerReporteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_VerReporteMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_VerReporteMouseExited

    private void btn_AgregarSolucionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarSolucionMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_AgregarSolucionMouseExited

    private void btn_VerSolucionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_VerSolucionesMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_VerSolucionesMouseExited

    private void btn_AdministrarEmpleadosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AdministrarEmpleadosMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_AdministrarEmpleadosMouseExited

    private void btn_AgregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_AgregarMouseExited

    private void combo_EmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_EmpleadosActionPerformed
        
    }//GEN-LAST:event_combo_EmpleadosActionPerformed

    private void btn_Agregar2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar2MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Agregar2MouseEntered

    private void btn_Agregar2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar2MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Agregar2MouseExited

    private void btn_Agregar2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar2MousePressed
        btn_Agregar2.setBackground(new Color(33,33,33));
                
        if(txtfield_Nombre.getText().isEmpty() || (txtfield_Telefono.getText().isEmpty() && txtfield_Correo.getText().isEmpty())){
            JOptionPane.showMessageDialog(null, "Por favor llena los campos obligatorios");
            
            if(txtfield_Nombre.getText().isEmpty()){
                txtfield_Nombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }

            if(txtfield_Telefono.getText().isEmpty()){
                txtfield_Telefono.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }
            
            if(txtfield_Correo.getText().isEmpty()){
                txtfield_Correo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            }
        }
        else {          
            txtfield_Nombre.setBorder(defaultBorder);
            
            txtfield_Telefono.setBorder(defaultBorder);
            txtfield_Correo.setBorder(defaultBorder);
            
            if(txtfield_Telefono.getText().isEmpty())txtfield_Telefono.setText("null");
            if(txtfield_Telefono2.getText().isEmpty())txtfield_Telefono2.setText("null");
            if(txtfield_Correo.getText().isEmpty())txtfield_Correo.setText("null");
            
            nuevo_Cliente();
        }
        
        txtfield_Nombre.setText("");
        txtfield_Calle.setText("");
        txtfield_numeroCasa.setText("");
        txtfield_Colonia.setText("");
        combo_Ciudad.setSelectedIndex(0);
        txtfield_Telefono.setText("");
        txtfield_Telefono2.setText("");
        txtfield_Correo.setText("");
    }//GEN-LAST:event_btn_Agregar2MousePressed

    private void btn_Agregar2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar2MouseReleased
        btn_Agregar2.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Agregar2MouseReleased

    private void btn_Buscar3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar3MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Buscar3MouseEntered

    private void btn_Buscar3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar3MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));       
    }//GEN-LAST:event_btn_Buscar3MouseExited

    private void btn_Buscar3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar3MousePressed
        btn_Buscar3.setBackground(new Color(33,33,33));
        
        get_Reportes();
             
        table_Reportes.getColumnModel().getColumn(0).setResizable(true);
        table_Reportes.getColumnModel().getColumn(0).setWidth(2);
        table_Reportes.getColumnModel().getColumn(7).setResizable(true);
        table_Reportes.getColumnModel().getColumn(7).setWidth(3);
    }//GEN-LAST:event_btn_Buscar3MousePressed

    private void btn_Buscar3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar3MouseReleased
        btn_Buscar3.setBackground(new Color(45,45,45));     
    }//GEN-LAST:event_btn_Buscar3MouseReleased

    private void btn_Agregar4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar4MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Agregar4MouseEntered

    private void btn_Agregar4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar4MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Agregar4MouseExited

    private void btn_Agregar4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar4MousePressed
        btn_Agregar4.setBackground(new Color(33,33,33));

        if(txtfield_ID.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el ID");
            txtfield_ID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));           
        }
        else if(txtArea_Solucion.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor, ingrese la solución");
            txtArea_Solucion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
        }
        else {
            txtfield_ID.setBorder(defaultBorder);
            txtArea_Solucion.setBorder(defaultBorder);
            update_Solution();
        }
        
    }//GEN-LAST:event_btn_Agregar4MousePressed

    private void btn_Agregar4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar4MouseReleased
        btn_Agregar4.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Agregar4MouseReleased

    private void btn_Buscar5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar5MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Buscar5MouseEntered

    private void btn_Buscar5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar5MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Buscar5MouseExited

    private void btn_Buscar5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar5MousePressed
        btn_Buscar5.setBackground(new Color(33,33,33));
        
        get_Solution();
        
    }//GEN-LAST:event_btn_Buscar5MousePressed

    private void btn_Buscar5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar5MouseReleased
        btn_Buscar5.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Buscar5MouseReleased

    private void btn_ActualizarStatusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarStatusMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_ActualizarStatusMouseEntered

    private void btn_ActualizarStatusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarStatusMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_ActualizarStatusMouseExited

    private void btn_ActualizarStatusMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarStatusMousePressed
        resetColor(btn_AgregarReporte);
        resetColor(btn_ActualizarCliente);
        resetColor(btn_VerReporte);
        resetColor(btn_AgregarSolucion);
        resetColor(btn_VerSoluciones);
        setColor(btn_ActualizarStatus);
        resetColor(btn_AdministrarEmpleados);
        resetColor(btn_AgregarCliente);
        
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(true);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
        
        modelComboEmpleados(combo_Empleados);
        modelComboEmpleados(combo_Empleados3);
        modelComboEmpleados(combo_Empleados7);
        modelComboClientes(combo_Clientes);
        modelComboClientes(combo_Clientes3);
        modelComboClientes(combo_Clientes5);
        modelComboClientes(combo_Clientes6);
        modelComboClientes(combo_Clientes8);     
       
        combo_Clientes6.setSelectedIndex(-1);
        combo_Status1.setSelectedIndex(-1);
    }//GEN-LAST:event_btn_ActualizarStatusMousePressed

    private void btn_Buscar6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar6MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Buscar6MouseEntered

    private void btn_Buscar6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar6MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Buscar6MouseExited

    private void btn_Buscar6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar6MousePressed
        btn_Buscar6.setBackground(new Color(33,33,33));
        
        get_Status();
    }//GEN-LAST:event_btn_Buscar6MousePressed

    private void btn_Buscar6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Buscar6MouseReleased
        btn_Buscar6.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Buscar6MouseReleased

    private void btn_ActualizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_ActualizarMouseEntered

    private void btn_ActualizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_ActualizarMouseExited

    private void btn_ActualizarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarMousePressed
        btn_Actualizar.setBackground(new Color(33,33,33));
        
        if(txtfield_ID6.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el ID");
            txtfield_ID6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
        }
        else{
            txtfield_ID6.setBorder(defaultBorder);
            update_Status();
            get_Status();
        }
    }//GEN-LAST:event_btn_ActualizarMousePressed

    private void btn_ActualizarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ActualizarMouseReleased
        btn_Actualizar.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_ActualizarMouseReleased

    private void combo_Clientes3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_Clientes3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_Clientes3ActionPerformed

    private void btn_Agregar7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar7MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Agregar7MouseEntered

    private void btn_Agregar7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar7MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Agregar7MouseExited

    private void btn_Agregar7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar7MousePressed
        btn_Agregar7.setBackground(new Color(33,33,33));
        
        agregar_Empleado();
        
        modelComboEmpleados(combo_Empleados);
        modelComboEmpleados(combo_Empleados3);
        modelComboEmpleados(combo_Empleados7);
        modelComboClientes(combo_Clientes);
        modelComboClientes(combo_Clientes3);
        modelComboClientes(combo_Clientes5);
        modelComboClientes(combo_Clientes6);
        modelComboClientes(combo_Clientes8);
    }//GEN-LAST:event_btn_Agregar7MousePressed

    private void btn_Agregar7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar7MouseReleased
        btn_Agregar7.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Agregar7MouseReleased

    private void btn_AdministrarEmpleadosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AdministrarEmpleadosMousePressed
        resetColor(btn_AgregarReporte);
        resetColor(btn_ActualizarCliente);
        resetColor(btn_VerReporte);
        resetColor(btn_AgregarSolucion);
        resetColor(btn_VerSoluciones);
        resetColor(btn_ActualizarStatus);
        setColor(btn_AdministrarEmpleados);
        resetColor(btn_AgregarCliente);
        
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(false);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(true);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);
    }//GEN-LAST:event_btn_AdministrarEmpleadosMousePressed

    private void combo_EmpleadosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_combo_EmpleadosMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_EmpleadosMousePressed

    private void txtfield_PrediagMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtfield_PrediagMousePressed
        
    }//GEN-LAST:event_txtfield_PrediagMousePressed

    private void btn_AgregarClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarClienteMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_AgregarClienteMouseEntered

    private void btn_AgregarClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarClienteMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_AgregarClienteMouseExited

    private void btn_AgregarClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarClienteMousePressed
        resetColor(btn_AgregarReporte);
        resetColor(btn_ActualizarCliente);
        resetColor(btn_VerReporte);
        resetColor(btn_AgregarSolucion);
        resetColor(btn_VerSoluciones);
        resetColor(btn_ActualizarStatus);
        resetColor(btn_AdministrarEmpleados);
        setColor(btn_AgregarCliente);
        
        main_Screen1.setVisible(false);
        main_Screen2.setVisible(true);
        main_Screen3.setVisible(false);
        main_Screen4.setVisible(false);
        main_Screen5.setVisible(false);
        main_Screen6.setVisible(false);
        main_Screen7.setVisible(false);
        main_Screen8.setVisible(false);
        screen_Anticipo.setVisible(false);
        screen_Servicios.setVisible(false);

        combo_Ciudad.setSelectedIndex(-1);
        
        txtfield_Nombre.setText("");
        txtfield_Telefono.setText("");
        txtfield_Telefono2.setText("");
        txtfield_Correo.setText("");
    }//GEN-LAST:event_btn_AgregarClienteMousePressed

    private void btn_Actualizar8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Actualizar8MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Actualizar8MouseEntered

    private void btn_Actualizar8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Actualizar8MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Actualizar8MouseExited

    private void btn_Actualizar8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Actualizar8MousePressed
        btn_Actualizar8.setBackground(new Color(33,33,33));
        
        actualizar_Datos();
    }//GEN-LAST:event_btn_Actualizar8MousePressed

    private void btn_Actualizar8MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Actualizar8MouseReleased
        btn_Actualizar8.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Actualizar8MouseReleased

    private void combo_ServiciosPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_ServiciosPopupMenuWillBecomeInvisible
        switch(combo_Servicios.getSelectedIndex()){
            case 0:
                txtfield_Precio.setText("180");
                txtfield_Precio.setEnabled(true);
                break;
            case 1:
                txtfield_Precio.setText("350");
                txtfield_Precio.setEnabled(true);
                break;
            case 2:
                txtfield_Precio.setText("490");
                txtfield_Precio.setEnabled(true);
                break;
            case 3:
                txtfield_Precio.setText("290");
                txtfield_Precio.setEnabled(true);
                break;
            case 4:
                txtfield_Precio.setText("690");
                txtfield_Precio.setEnabled(true);
                break;
            case 5:
                txtfield_Precio.setText("390");
                txtfield_Precio.setEnabled(true);
                break;
            case 6:
                txtfield_Precio.setText("390");
                txtfield_Precio.setEnabled(true);
                break;
            case 7:
                txtfield_Precio.setText("950");
                txtfield_Precio.setEnabled(true);
                break;
            case 12:
                serv++;
                txtfield_Precio.setText("");
                txtfield_Precio.setEnabled(false);
                break;
            default:
                txtfield_Precio.setText("");
                txtfield_Precio.setEnabled(true);
                break;
        }
    }//GEN-LAST:event_combo_ServiciosPopupMenuWillBecomeInvisible

    private void combo_Clientes8PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Clientes8PopupMenuWillBecomeInvisible
        datos_Clientes();
    }//GEN-LAST:event_combo_Clientes8PopupMenuWillBecomeInvisible

    private void btn_Resetear3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear3MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Resetear3MouseEntered

    private void btn_Resetear3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear3MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Resetear3MouseExited

    private void btn_Resetear3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear3MousePressed
        btn_Resetear3.setBackground(new Color(33,33,33));
        
        combo_Empleados3.setSelectedIndex(-1);
        combo_Clientes3.setSelectedIndex(-1);
        combo_Tipo3.setSelectedIndex(-1);
        txtfield_Marca3.setText("");
        txtfield_ID3.setText("");
    }//GEN-LAST:event_btn_Resetear3MousePressed

    private void btn_Resetear3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear3MouseReleased
        btn_Resetear3.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Resetear3MouseReleased

    private void btn_Resetear5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear5MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Resetear5MouseEntered

    private void btn_Resetear5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear5MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Resetear5MouseExited

    private void btn_Resetear5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear5MousePressed
        btn_Resetear5.setBackground(new Color(33,33,33));
        
        txtfield_ID1.setText("");
        combo_Clientes5.setSelectedIndex(-1);
        combo_Tipo5.setSelectedIndex(-1);
    }//GEN-LAST:event_btn_Resetear5MousePressed

    private void btn_Resetear5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear5MouseReleased
        btn_Resetear5.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Resetear5MouseReleased

    private void btn_Resetear6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear6MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Resetear6MouseEntered

    private void btn_Resetear6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear6MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Resetear6MouseExited

    private void btn_Resetear6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear6MousePressed
        btn_Resetear6.setBackground(new Color(33,33,33));
        
        combo_Clientes6.setSelectedIndex(-1);
        combo_Status1.setSelectedIndex(-1);
        txtfield_ID6.setText("");
    }//GEN-LAST:event_btn_Resetear6MousePressed

    private void btn_Resetear6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Resetear6MouseReleased
        btn_Resetear6.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Resetear6MouseReleased

    private void btn_ImprimirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ImprimirMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_ImprimirMouseEntered

    private void btn_ImprimirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ImprimirMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_ImprimirMouseExited

    private void btn_ImprimirMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ImprimirMousePressed
        btn_Imprimir.setBackground(new Color(33,33,33));
        
        reporte_PDF();
    }//GEN-LAST:event_btn_ImprimirMousePressed

    private void btn_ImprimirMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ImprimirMouseReleased
        btn_Imprimir.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_ImprimirMouseReleased

    
    
    private void radio_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_NoActionPerformed
        if(radio_No.isSelected()){
            radio_Si.setSelected(false);
            txtfield_Anticipo.setBackground(new Color(204, 204, 204));
            txtfield_Anticipo.setEnabled(false);
        }
    }//GEN-LAST:event_radio_NoActionPerformed

    private void txtfield_AnticipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfield_AnticipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtfield_AnticipoActionPerformed

    private void btn_OKMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_OKMousePressed
        btn_OK.setBackground(new Color(33,33,33));
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Date date = new Date();
	String fecha = dateFormat.format(date);
        
        if(radio_Si.isSelected()){
            anticipo = txtfield_Anticipo.getText();
        }
        else {
            anticipo = "0";
        }
        
        combo_Clientes.setVisible(true);
        combo_Equipo.setVisible(true);
        txtfield_Marca.setVisible(true);
        txtfield_Modelo.setVisible(true);
        txtfield_nsEquipo.setVisible(true);
        screen_Anticipo.setVisible(false);
        
        radio_Si.setSelected(false);
        radio_No.setSelected(false);
 
        nuevo_Reporte(fecha);
    }//GEN-LAST:event_btn_OKMousePressed

    private void btn_OKMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_OKMouseReleased
        btn_OK.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_OKMouseReleased

    private void btn_OKMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_OKMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_OKMouseEntered

    private void btn_OKMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_OKMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_OKMouseExited

    private void radio_SiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_SiActionPerformed
        if(radio_Si.isSelected()){
            radio_No.setSelected(false);
            txtfield_Anticipo.setEnabled(true);
            txtfield_Anticipo.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_radio_SiActionPerformed

    private void combo_Servicios1_1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Servicios1_1PopupMenuWillBecomeInvisible
        combo_Servicios1_1.setBorder(new EmptyBorder(0,0,0,0));
        
        switch(combo_Servicios1_1.getSelectedIndex()){
            case 0:
                txtfield_Precio1_1.setText("180");
                break;
            case 1:
                txtfield_Precio1_1.setText("350");
                break;
            case 2:
                txtfield_Precio1_1.setText("490");
                break;
            case 3:
                txtfield_Precio1_1.setText("290");
                break;
            case 4:
                txtfield_Precio1_1.setText("690");
                break;
            case 5:
                txtfield_Precio1_1.setText("390");
                break;
            case 6:
                txtfield_Precio1_1.setText("390");
                break;
            case 7:
                txtfield_Precio1_1.setText("950");
                break;
            default:
                txtfield_Precio1_1.setText("");
                break;
        }
    }//GEN-LAST:event_combo_Servicios1_1PopupMenuWillBecomeInvisible

    private void combo_Servicios1_2PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Servicios1_2PopupMenuWillBecomeInvisible
        combo_Servicios1_2.setBorder(new EmptyBorder(0, 0,0,0));
        
        switch(combo_Servicios1_2.getSelectedIndex()){
            case 0:
                txtfield_Precio1_2.setText("180");
                break;
            case 1:
                txtfield_Precio1_2.setText("350");
                break;
            case 2:
                txtfield_Precio1_2.setText("490");
                break;
            case 3:
                txtfield_Precio1_2.setText("290");
                break;
            case 4:
                txtfield_Precio1_2.setText("690");
                break;
            case 5:
                txtfield_Precio1_2.setText("390");
                break;
            case 6:
                txtfield_Precio1_2.setText("390");
                break;
            case 7:
                txtfield_Precio1_2.setText("950");
                break;
            default:
                txtfield_Precio1_2.setText("");
                break;
        }
    }//GEN-LAST:event_combo_Servicios1_2PopupMenuWillBecomeInvisible

    private void combo_Servicios1_3PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Servicios1_3PopupMenuWillBecomeInvisible
        combo_Servicios1_3.setBorder(new EmptyBorder(0, 0,0,0));
        
        switch(combo_Servicios1_3.getSelectedIndex()){
            case 0:
                txtfield_Precio1_3.setText("180");
                break;
            case 1:
                txtfield_Precio1_3.setText("350");
                break;
            case 2:
                txtfield_Precio1_3.setText("490");
                break;
            case 3:
                txtfield_Precio1_3.setText("290");
                break;
            case 4:
                txtfield_Precio1_3.setText("690");
                break;
            case 5:
                txtfield_Precio1_3.setText("390");
                break;
            case 6:
                txtfield_Precio1_3.setText("390");
                break;
            case 7:
                txtfield_Precio1_3.setText("950");
                break;
            default:
                txtfield_Precio1_3.setText("");
                break;
        }
    }//GEN-LAST:event_combo_Servicios1_3PopupMenuWillBecomeInvisible

    private void combo_Servicios1_4PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Servicios1_4PopupMenuWillBecomeInvisible
        combo_Servicios1_4.setBorder(new EmptyBorder(0, 0,0,0));
        
        switch(combo_Servicios1_4.getSelectedIndex()){
            case 0:
                txtfield_Precio1_4.setText("180");
                break;
            case 1:
                txtfield_Precio1_4.setText("350");
                break;
            case 2:
                txtfield_Precio1_4.setText("490");
                break;
            case 3:
                txtfield_Precio1_4.setText("290");
                break;
            case 4:
                txtfield_Precio1_4.setText("690");
                break;
            case 5:
                txtfield_Precio1_4.setText("390");
                break;
            case 6:
                txtfield_Precio1_4.setText("390");
                break;
            case 7:
                txtfield_Precio1_4.setText("950");
                break;
            default:
                txtfield_Precio1_4.setText("");
                break;
        }
    }//GEN-LAST:event_combo_Servicios1_4PopupMenuWillBecomeInvisible

    private void btn_Agregar1_1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar1_1MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Agregar1_1MouseEntered

    private void btn_Agregar1_1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar1_1MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Agregar1_1MouseExited

    private void btn_Agregar1_1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar1_1MousePressed
        btn_Agregar1_1.setBackground(new Color(33,33,33));
        
        String a,b,c,d;
        
        a = combo_Servicios1_1.getSelectedItem().toString();
        b = combo_Servicios1_2.getSelectedItem().toString();
        c = combo_Servicios1_3.getSelectedItem().toString();
        d = combo_Servicios1_4.getSelectedItem().toString();
        
        if(a.equals(b)){
            combo_Servicios1_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            combo_Servicios1_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            JOptionPane.showMessageDialog(null, "No puede haber servicios iguales");
        }
        else if(a.equals(c)){
            combo_Servicios1_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            combo_Servicios1_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            JOptionPane.showMessageDialog(null, "No puede haber servicios iguales");
        }
        else if(a.equals(d)){
            combo_Servicios1_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            combo_Servicios1_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            JOptionPane.showMessageDialog(null, "No puede haber servicios iguales");
        }
        else if(b.equals(c)){
            combo_Servicios1_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            combo_Servicios1_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            JOptionPane.showMessageDialog(null, "No puede haber servicios iguales");
        }
        else if(b.equals(d)){
            combo_Servicios1_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            combo_Servicios1_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            JOptionPane.showMessageDialog(null, "No puede haber servicios iguales");
        }
        else if((chk_Servicio1_3.isSelected() && chk_Servicio1_4.isSelected()) && c.equals(d)){
            combo_Servicios1_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            combo_Servicios1_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
            JOptionPane.showMessageDialog(null, "No puede haber servicios iguales");
        }
        else {
            combo_Servicios1_1.setBorder(defaultBorder);
            combo_Servicios1_2.setBorder(defaultBorder);
            combo_Servicios1_3.setBorder(defaultBorder);
            combo_Servicios1_4.setBorder(defaultBorder);
            
            agregar_Servicios();
            
            String t3 = txtfield_Precio1_3.getText();
            String t4 = txtfield_Precio1_4.getText();
            
            int p1 = Integer.parseInt(txtfield_Precio1_1.getText());
            int p2 = Integer.parseInt(txtfield_Precio1_2.getText());
            int p3 = 0, p4 = 0;
            
            if(t3.isEmpty()){
                p3 = 0;
            }
            else {
                p3 = Integer.parseInt(txtfield_Precio1_3.getText());
            }
            
            if(t4.isEmpty()){
                p4 = 0;
            }
            else {
                p4 = Integer.parseInt(txtfield_Precio1_4.getText());
            }
                        
            int suma =  p1 + p2 + p3 + p4; 
            
            JOptionPane.showMessageDialog(null, "Servicios agregados.\n El total es de: $" + suma + ".00");
            
            screen_Servicios.setVisible(false);
            screen_Anticipo.setVisible(true);
            combo_Empleados.setVisible(true);
            combo_Clientes.setVisible(false);
            combo_Equipo.setVisible(false);
            txtfield_Marca.setVisible(false);
            txtfield_Modelo.setVisible(false);
            txtfield_nsEquipo.setVisible(false);
            
            combo_Servicios1_1.setSelectedIndex(0);
            combo_Servicios1_2.setSelectedIndex(1);
            combo_Servicios1_3.setSelectedIndex(-1);
            combo_Servicios1_4.setSelectedIndex(-1);
            
            txtfield_Precio1_1.setText("");
            txtfield_Precio1_2.setText("");
            txtfield_Precio1_3.setText("");
            txtfield_Precio1_4.setText("");
            
        }
    }//GEN-LAST:event_btn_Agregar1_1MousePressed

    private void btn_Agregar1_1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar1_1MouseReleased
        btn_Agregar1_1.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Agregar1_1MouseReleased

    private void chk_Servicio1_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_Servicio1_3ActionPerformed
        if(chk_Servicio1_3.isSelected()){
            serv++;
            combo_Servicios1_3.setEnabled(true);
            combo_Servicios1_3.setSelectedIndex(2);
            txtfield_Precio1_3.setEnabled(true);
            txtfield_Precio1_3.setText("490");
        }
        
        if(!(chk_Servicio1_3.isSelected())){
            serv--;
            combo_Servicios1_3.setEnabled(false);
            combo_Servicios1_3.setSelectedIndex(-1);
            txtfield_Precio1_3.setEnabled(false);
            txtfield_Precio1_3.setText("");
        }
        
    }//GEN-LAST:event_chk_Servicio1_3ActionPerformed

    private void chk_Servicio1_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_Servicio1_4ActionPerformed
        if(chk_Servicio1_4.isSelected()){
            serv++;
            combo_Servicios1_4.setEnabled(true);
            combo_Servicios1_4.setSelectedIndex(3);
            txtfield_Precio1_4.setEnabled(true);
            txtfield_Precio1_4.setText("290");
        }
        
        if(!(chk_Servicio1_4.isSelected())){
            serv--;
            combo_Servicios1_4.setEnabled(false);
            combo_Servicios1_3.setSelectedIndex(-1);
            txtfield_Precio1_4.setEnabled(false);
            txtfield_Precio1_4.setText("");
        }
        
    }//GEN-LAST:event_chk_Servicio1_4ActionPerformed

    private void btn_Imprimir3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Imprimir3MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  
    }//GEN-LAST:event_btn_Imprimir3MouseEntered

    private void btn_Imprimir3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Imprimir3MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Imprimir3MouseExited

    private void btn_Imprimir3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Imprimir3MousePressed
        btn_Imprimir3.setBackground(new Color(33,33,33));
        
        if(txtfield_ID3.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor ingresa el ID");
            txtfield_ID3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
        }
        else{
            
            txtfield_ID3.setBorder(defaultBorder);
            
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Laptown?autoReconnect=true&useSSL=false", "root", "root");

                HashMap parameter = new HashMap();

                int ID_R = Integer.parseInt(txtfield_ID3.getText());

                parameter.put("ID", ID_R);

                //String path = "G:\\BitFix\\BitFix\\src\\Reportes/newReport.jrxml";
                String path = "D:\\Documents\\NetBeansProjects\\BitFix\\src\\Reportes/newReport.jrxml";

                JasperReport content = JasperCompileManager.compileReport(path);

                JasperPrint print = JasperFillManager.fillReport(content, parameter, con);
            
                //JasperExportManager.exportReportToPdfFile(print, "D:\\Documents\\BitFix-Reports\\reportTemp.pdf");

                JasperViewer jv = new JasperViewer(print, false);
                
                jv.viewReport(print, false);
                
            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_Imprimir3MousePressed

    private void btn_Imprimir3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Imprimir3MouseReleased
        btn_Imprimir3.setBackground(new Color(45,45,45));  
    }//GEN-LAST:event_btn_Imprimir3MouseReleased

    private void combo_Servicios6_2PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Servicios6_2PopupMenuWillBecomeInvisible
        switch(combo_Servicios6_2.getSelectedIndex()){
                case 0:
                    txtfield_Precio6_2.setText("180");
                    break;
                case 1:
                    txtfield_Precio6_2.setText("350");
                    break;
                case 2:
                    txtfield_Precio6_2.setText("490");
                    break;
                case 3:
                    txtfield_Precio6_2.setText("290");
                    break;
                case 4:
                    txtfield_Precio6_2.setText("690");
                    break;
                case 5:
                    txtfield_Precio6_2.setText("390");
                    break;
                case 6:
                    txtfield_Precio6_2.setText("390");
                    break;
                case 7:
                    txtfield_Precio6_2.setText("950");
                    break;
                default:
                    txtfield_Precio6_2.setText("");
                    break;
                }
    }//GEN-LAST:event_combo_Servicios6_2PopupMenuWillBecomeInvisible

    private void chk_Servicio6_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_Servicio6_3ActionPerformed
        if(chk_Servicio6_3.isSelected() && chk_Servicio6_3.isEnabled()){
            combo_Servicios6_3.setVisible(true);
            combo_Servicios6_3.setEnabled(true);
            combo_Servicios6_3.setSelectedIndex(2);
            txtfield_Servicios6_3.setVisible(false);
            txtfield_Precio6_3.setEnabled(true);
            txtfield_Precio6_3.setEditable(true);
            txtfield_Precio6_3.setText("490");
            chk_Servicio6_4.setEnabled(true);
        }
        
        if(!(chk_Servicio6_3.isSelected()) && chk_Servicio6_3.isEnabled()){
            combo_Servicios6_3.setVisible(false);
            combo_Servicios6_3.setEnabled(false);
            combo_Servicios6_3.setSelectedIndex(-1);
            txtfield_Servicios6_3.setVisible(true);
            txtfield_Precio6_3.setEnabled(false);
            txtfield_Precio6_3.setText("");
            chk_Servicio6_4.setEnabled(false);
        }
    }//GEN-LAST:event_chk_Servicio6_3ActionPerformed

    private void combo_Servicios6_3PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Servicios6_3PopupMenuWillBecomeInvisible
        switch(combo_Servicios6_3.getSelectedIndex()){
                case 0:
                    txtfield_Precio6_3.setText("180");
                    break;
                case 1:
                    txtfield_Precio6_3.setText("350");
                    break;
                case 2:
                    txtfield_Precio6_3.setText("490");
                    break;
                case 3:
                    txtfield_Precio6_3.setText("290");
                    break;
                case 4:
                    txtfield_Precio6_3.setText("690");
                    break;
                case 5:
                    txtfield_Precio6_3.setText("390");
                    break;
                case 6:
                    txtfield_Precio6_3.setText("390");
                    break;
                case 7:
                    txtfield_Precio6_3.setText("950");
                    break;
                default:
                    txtfield_Precio6_3.setText("");
                    break;
                }
    }//GEN-LAST:event_combo_Servicios6_3PopupMenuWillBecomeInvisible

    private void chk_Servicio6_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_Servicio6_4ActionPerformed
        if(chk_Servicio6_4.isSelected() && chk_Servicio6_4.isEnabled()){
            combo_Servicios6_4.setVisible(true);
            combo_Servicios6_4.setEnabled(true);
            combo_Servicios6_4.setSelectedIndex(3);
            txtfield_Servicios6_4.setVisible(false);
            txtfield_Precio6_4.setEnabled(true);
            txtfield_Precio6_4.setEditable(true);
            txtfield_Precio6_4.setText("290");
        }
        
        if(!(chk_Servicio6_4.isSelected()) && chk_Servicio6_4.isEnabled()){
            combo_Servicios6_4.setVisible(false);
            combo_Servicios6_4.setEnabled(false);
            combo_Servicios6_4.setSelectedIndex(-1);
            txtfield_Servicios6_4.setVisible(true);
            txtfield_Precio6_4.setEnabled(false);
            txtfield_Precio6_4.setText("");
        }
    }//GEN-LAST:event_chk_Servicio6_4ActionPerformed

    private void combo_Servicios6_4PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combo_Servicios6_4PopupMenuWillBecomeInvisible
        switch(combo_Servicios6_4.getSelectedIndex()){
                case 0:
                    txtfield_Precio6_4.setText("180");
                    break;
                case 1:
                    txtfield_Precio6_4.setText("350");
                    break;
                case 2:
                    txtfield_Precio6_4.setText("490");
                    break;
                case 3:
                    txtfield_Precio6_4.setText("290");
                    break;
                case 4:
                    txtfield_Precio6_4.setText("690");
                    break;
                case 5:
                    txtfield_Precio6_4.setText("390");
                    break;
                case 6:
                    txtfield_Precio6_4.setText("390");
                    break;
                case 7:
                    txtfield_Precio6_4.setText("950");
                    break;
                default:
                    txtfield_Precio6_4.setText("");
                    break;
                }
    }//GEN-LAST:event_combo_Servicios6_4PopupMenuWillBecomeInvisible

    private void btn_Agregar6_1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar6_1MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Agregar6_1MouseEntered

    private void btn_Agregar6_1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar6_1MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Agregar6_1MouseExited

    private void btn_Agregar6_1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar6_1MousePressed
        btn_Agregar6_1.setBackground(new Color(33,33,33));
        
//        if(combo_Servicios6_2.getSelectedIndex() == -1){combo_Servicios6_2.setSelectedIndex()
        
        actualizar_Servicios();
        
        screen_Servicios6.setVisible(false);
        
        combo_Clientes6.setEnabled(true);
        combo_Status1.setEnabled(true);
        txtfield_ID6.setEnabled(true);
    }//GEN-LAST:event_btn_Agregar6_1MousePressed

    private void btn_Agregar6_1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Agregar6_1MouseReleased
        btn_Agregar6_1.setBackground(new Color(45,45,45));  
    }//GEN-LAST:event_btn_Agregar6_1MouseReleased

    private void chk_Servicio6_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_Servicio6_2ActionPerformed
        if(chk_Servicio6_2.isSelected() && chk_Servicio6_2.isEnabled()){
            combo_Servicios6_2.setVisible(true);
            combo_Servicios6_2.setEnabled(true);
            combo_Servicios6_2.setSelectedIndex(1);
            txtfield_Servicios6_2.setVisible(false);
            txtfield_Precio6_2.setEditable(true);
            txtfield_Precio6_2.setEnabled(true);
            txtfield_Precio6_2.setText("350");
            chk_Servicio6_3.setEnabled(true);
        }
        
        if(!(chk_Servicio6_2.isSelected() && chk_Servicio6_2.isEnabled())){
            combo_Servicios6_2.setVisible(false);
            combo_Servicios6_2.setEnabled(false);
            combo_Servicios6_2.setSelectedIndex(-1);           
            txtfield_Servicios6_2.setVisible(true);
            txtfield_Precio6_2.setEnabled(false);
            txtfield_Precio6_2.setText("");
            chk_Servicio6_3.setEnabled(false);
        }
    }//GEN-LAST:event_chk_Servicio6_2ActionPerformed

    private void btn_AgregarServiciosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarServiciosMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_AgregarServiciosMouseEntered

    private void btn_AgregarServiciosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarServiciosMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_AgregarServiciosMouseExited

    private void btn_AgregarServiciosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarServiciosMousePressed
        btn_AgregarServicios.setBackground(new Color(33,33,33));
        
        if(txtfield_ID6.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor ingresa el ID");
            txtfield_ID6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
        }
        else
        {  
            txtfield_ID6.setBorder(defaultBorder);
            screen_Servicios6.setVisible(true);
            servicios_Reportes();

            combo_Clientes6.setEnabled(false);
            combo_Status1.setEnabled(false);
            txtfield_ID6.setEnabled(false);
        }
    }//GEN-LAST:event_btn_AgregarServiciosMousePressed

    private void btn_AgregarServiciosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarServiciosMouseReleased
        btn_AgregarServicios.setBackground(new Color(451,45,45));  
    }//GEN-LAST:event_btn_AgregarServiciosMouseReleased

    private void btn_Eliminar7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Eliminar7MouseReleased
        btn_Eliminar7.setBackground(new Color(45,45,45));
    }//GEN-LAST:event_btn_Eliminar7MouseReleased

    private void btn_Eliminar7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Eliminar7MousePressed
        btn_Eliminar7.setBackground(new Color(33,33,33));

        if(combo_Empleados7.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un empleado");
            combo_Empleados7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51), 2));
        }
        else {
            combo_Empleados.setBorder(defaultBorder);
            eliminar_Empleado();
        }

        modelComboEmpleados(combo_Empleados7);
    }//GEN-LAST:event_btn_Eliminar7MousePressed

    private void btn_Eliminar7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Eliminar7MouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_Eliminar7MouseExited

    private void btn_Eliminar7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Eliminar7MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btn_Eliminar7MouseEntered

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel btn_Actualizar;
    private javax.swing.JPanel btn_Actualizar8;
    private javax.swing.JPanel btn_ActualizarCliente;
    private javax.swing.JPanel btn_ActualizarStatus;
    private javax.swing.JPanel btn_AdministrarEmpleados;
    private javax.swing.JPanel btn_Agregar;
    private javax.swing.JPanel btn_Agregar1_1;
    private javax.swing.JPanel btn_Agregar2;
    private javax.swing.JPanel btn_Agregar4;
    private javax.swing.JPanel btn_Agregar6_1;
    private javax.swing.JPanel btn_Agregar7;
    private javax.swing.JPanel btn_AgregarCliente;
    private javax.swing.JPanel btn_AgregarReporte;
    private javax.swing.JPanel btn_AgregarServicios;
    private javax.swing.JPanel btn_AgregarSolucion;
    private javax.swing.JPanel btn_Buscar3;
    private javax.swing.JPanel btn_Buscar5;
    private javax.swing.JPanel btn_Buscar6;
    private javax.swing.JPanel btn_Eliminar7;
    private javax.swing.JPanel btn_Imprimir;
    private javax.swing.JPanel btn_Imprimir3;
    private javax.swing.JPanel btn_OK;
    private javax.swing.JPanel btn_Resetear3;
    private javax.swing.JPanel btn_Resetear5;
    private javax.swing.JPanel btn_Resetear6;
    private javax.swing.JPanel btn_VerReporte;
    private javax.swing.JPanel btn_VerSoluciones;
    private javax.swing.JCheckBox chk_Servicio1;
    private javax.swing.JCheckBox chk_Servicio1_2;
    private javax.swing.JCheckBox chk_Servicio1_3;
    private javax.swing.JCheckBox chk_Servicio1_4;
    private javax.swing.JCheckBox chk_Servicio6_1;
    private javax.swing.JCheckBox chk_Servicio6_2;
    private javax.swing.JCheckBox chk_Servicio6_3;
    private javax.swing.JCheckBox chk_Servicio6_4;
    private javax.swing.JComboBox<String> combo_Ciudad;
    private javax.swing.JComboBox<String> combo_Clientes;
    private javax.swing.JComboBox<String> combo_Clientes3;
    private javax.swing.JComboBox<String> combo_Clientes5;
    private javax.swing.JComboBox<String> combo_Clientes6;
    private javax.swing.JComboBox<String> combo_Clientes8;
    private javax.swing.JComboBox<String> combo_Empleados;
    private javax.swing.JComboBox<String> combo_Empleados3;
    private javax.swing.JComboBox<String> combo_Empleados7;
    private javax.swing.JComboBox<String> combo_Equipo;
    private javax.swing.JComboBox<String> combo_Servicios;
    private javax.swing.JComboBox<String> combo_Servicios1_1;
    private javax.swing.JComboBox<String> combo_Servicios1_2;
    private javax.swing.JComboBox<String> combo_Servicios1_3;
    private javax.swing.JComboBox<String> combo_Servicios1_4;
    private javax.swing.JComboBox<String> combo_Servicios6_2;
    private javax.swing.JComboBox<String> combo_Servicios6_3;
    private javax.swing.JComboBox<String> combo_Servicios6_4;
    private javax.swing.JComboBox<String> combo_Status;
    private javax.swing.JComboBox<String> combo_Status1;
    private javax.swing.JComboBox<String> combo_Tipo3;
    private javax.swing.JComboBox<String> combo_Tipo5;
    private javax.swing.JLabel img_Logo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel load_Screen;
    private javax.swing.JLabel logo_ActualizarStatus;
    private javax.swing.JLabel logo_AdministrarEmpleados;
    private javax.swing.JLabel logo_AgregarCliente;
    private javax.swing.JLabel logo_AgregarCliente1;
    private javax.swing.JLabel logo_AgregarReporte;
    private javax.swing.JLabel logo_AgregarSolucion;
    private javax.swing.JLabel logo_VerReporte;
    private javax.swing.JLabel logo_VerSoluciones;
    private javax.swing.JPanel main_Screen1;
    private javax.swing.JPanel main_Screen2;
    private javax.swing.JPanel main_Screen3;
    private javax.swing.JPanel main_Screen4;
    private javax.swing.JPanel main_Screen5;
    private javax.swing.JPanel main_Screen6;
    private javax.swing.JPanel main_Screen7;
    private javax.swing.JPanel main_Screen8;
    private javax.swing.JRadioButton radio_No;
    private javax.swing.JRadioButton radio_Si;
    private javax.swing.JPanel screen_Anticipo;
    private javax.swing.JPanel screen_Servicios;
    private javax.swing.JPanel screen_Servicios6;
    private javax.swing.JScrollPane scroll_Fallas;
    private javax.swing.JScrollPane scroll_Obs;
    private javax.swing.JPanel sidebar;
    private javax.swing.JLabel space;
    private javax.swing.JLabel space1;
    private javax.swing.JLabel space2;
    private javax.swing.JLabel space3;
    private javax.swing.JLabel space4;
    private javax.swing.JLabel space5;
    private javax.swing.JLabel space6;
    private javax.swing.JLabel space7;
    private javax.swing.JTable table_Reportes;
    private javax.swing.JTable table_Soluciones;
    private javax.swing.JTable table_Status;
    private javax.swing.JPanel title_bar;
    private javax.swing.JPanel title_bar2;
    private javax.swing.JPanel title_bar3;
    private javax.swing.JPanel title_bar4;
    private javax.swing.JPanel title_bar5;
    private javax.swing.JPanel title_bar6;
    private javax.swing.JPanel title_bar7;
    private javax.swing.JPanel title_bar8;
    private javax.swing.JTextArea txtArea_Fallas;
    private javax.swing.JTextArea txtArea_Obs;
    private javax.swing.JTextArea txtArea_Solucion;
    private javax.swing.JLabel txt_ActualizarStatus;
    private javax.swing.JLabel txt_AdministrarEmpleados;
    private javax.swing.JLabel txt_Agregar;
    private javax.swing.JLabel txt_Agregar1;
    private javax.swing.JLabel txt_Agregar2;
    private javax.swing.JLabel txt_Agregar3;
    private javax.swing.JLabel txt_Agregar4;
    private javax.swing.JLabel txt_Agregar5;
    private javax.swing.JLabel txt_AgregarCliente;
    private javax.swing.JLabel txt_AgregarCliente1;
    private javax.swing.JLabel txt_AgregarReporte;
    private javax.swing.JLabel txt_AgregarSolucion;
    private javax.swing.JLabel txt_Anticipo;
    private javax.swing.JLabel txt_Buscar;
    private javax.swing.JLabel txt_Buscar1;
    private javax.swing.JLabel txt_Buscar10;
    private javax.swing.JLabel txt_Buscar11;
    private javax.swing.JLabel txt_Buscar12;
    private javax.swing.JLabel txt_Buscar2;
    private javax.swing.JLabel txt_Buscar3;
    private javax.swing.JLabel txt_Buscar4;
    private javax.swing.JLabel txt_Buscar5;
    private javax.swing.JLabel txt_Buscar6;
    private javax.swing.JLabel txt_Buscar7;
    private javax.swing.JLabel txt_Buscar8;
    private javax.swing.JLabel txt_Buscar9;
    private javax.swing.JLabel txt_Calle;
    private javax.swing.JLabel txt_Calle1;
    private javax.swing.JLabel txt_Ciudad;
    private javax.swing.JLabel txt_Ciudad1;
    private javax.swing.JLabel txt_Cliente;
    private javax.swing.JLabel txt_Cliente3;
    private javax.swing.JLabel txt_Cliente5;
    private javax.swing.JLabel txt_Cliente6;
    private javax.swing.JLabel txt_Colonia;
    private javax.swing.JLabel txt_Colonia1;
    private javax.swing.JLabel txt_Correo;
    private javax.swing.JLabel txt_Correo1;
    private javax.swing.JLabel txt_Empleado;
    private javax.swing.JLabel txt_Empleado3;
    private javax.swing.JLabel txt_Empleado7;
    private javax.swing.JLabel txt_Equipo;
    private javax.swing.JLabel txt_Fallas;
    private javax.swing.JLabel txt_ID;
    private javax.swing.JLabel txt_ID1;
    private javax.swing.JLabel txt_ID2;
    private javax.swing.JLabel txt_Marca;
    private javax.swing.JLabel txt_Marca3;
    private javax.swing.JLabel txt_Marca4;
    private javax.swing.JLabel txt_Modelo;
    private javax.swing.JLabel txt_Nombre;
    private javax.swing.JLabel txt_Nombre1;
    private javax.swing.JLabel txt_Nombre7;
    private javax.swing.JLabel txt_Obs;
    private javax.swing.JLabel txt_Peso;
    private javax.swing.JLabel txt_Precio;
    private javax.swing.JLabel txt_Prediag;
    private javax.swing.JLabel txt_Respaldo;
    private javax.swing.JLabel txt_Servicios;
    private javax.swing.JLabel txt_Simbolo;
    private javax.swing.JLabel txt_Simbolo1_1;
    private javax.swing.JLabel txt_Simbolo1_2;
    private javax.swing.JLabel txt_Simbolo1_3;
    private javax.swing.JLabel txt_Simbolo1_4;
    private javax.swing.JLabel txt_Simbolo1_5;
    private javax.swing.JLabel txt_Simbolo1_6;
    private javax.swing.JLabel txt_Simbolo1_7;
    private javax.swing.JLabel txt_Simbolo1_8;
    private javax.swing.JLabel txt_Solucion;
    private javax.swing.JLabel txt_Status;
    private javax.swing.JLabel txt_Status1;
    private javax.swing.JLabel txt_Tipo3;
    private javax.swing.JLabel txt_Tipo5;
    private javax.swing.JLabel txt_Titulo;
    private javax.swing.JLabel txt_Titulo1_1;
    private javax.swing.JLabel txt_Titulo1_2;
    private javax.swing.JLabel txt_Titulo2;
    private javax.swing.JLabel txt_Titulo3;
    private javax.swing.JLabel txt_Titulo4;
    private javax.swing.JLabel txt_Titulo5;
    private javax.swing.JLabel txt_Titulo6;
    private javax.swing.JLabel txt_Titulo7;
    private javax.swing.JLabel txt_Titulo8;
    private javax.swing.JLabel txt_VerReporte;
    private javax.swing.JLabel txt_VerSoluciones;
    private javax.swing.JLabel txt_breadcrumbs;
    private javax.swing.JLabel txt_breadcrumbs2;
    private javax.swing.JLabel txt_breadcrumbs3;
    private javax.swing.JLabel txt_breadcrumbs4;
    private javax.swing.JLabel txt_breadcrumbs5;
    private javax.swing.JLabel txt_breadcrumbs6;
    private javax.swing.JLabel txt_breadcrumbs7;
    private javax.swing.JLabel txt_breadcrumbs8;
    private javax.swing.JLabel txt_nsBateria;
    private javax.swing.JLabel txt_nsCargador;
    private javax.swing.JLabel txt_nsEquipo;
    private javax.swing.JLabel txt_numeroCasa;
    private javax.swing.JLabel txt_numeroCasa1;
    private javax.swing.JLabel txt_telefono;
    private javax.swing.JLabel txt_telefono1;
    private javax.swing.JLabel txt_telefono2;
    private javax.swing.JLabel txt_telefono3;
    private javax.swing.JTextField txtfield_Anticipo;
    private javax.swing.JTextField txtfield_Calle;
    private javax.swing.JTextField txtfield_Calle1;
    private javax.swing.JTextField txtfield_Ciudad1;
    private javax.swing.JTextField txtfield_Colonia;
    private javax.swing.JTextField txtfield_Colonia1;
    private javax.swing.JTextField txtfield_Correo;
    private javax.swing.JTextField txtfield_Correo1;
    private javax.swing.JTextField txtfield_ID;
    private javax.swing.JTextField txtfield_ID1;
    private javax.swing.JTextField txtfield_ID3;
    private javax.swing.JTextField txtfield_ID6;
    private javax.swing.JTextField txtfield_Marca;
    private javax.swing.JTextField txtfield_Marca3;
    private javax.swing.JTextField txtfield_Modelo;
    private javax.swing.JTextField txtfield_Nombre;
    private javax.swing.JTextField txtfield_Nombre7;
    private javax.swing.JTextField txtfield_Precio;
    private javax.swing.JTextField txtfield_Precio1_1;
    private javax.swing.JTextField txtfield_Precio1_2;
    private javax.swing.JTextField txtfield_Precio1_3;
    private javax.swing.JTextField txtfield_Precio1_4;
    private javax.swing.JTextField txtfield_Precio6_1;
    private javax.swing.JTextField txtfield_Precio6_2;
    private javax.swing.JTextField txtfield_Precio6_3;
    private javax.swing.JTextField txtfield_Precio6_4;
    private javax.swing.JTextField txtfield_Prediag;
    private javax.swing.JTextField txtfield_Respaldo;
    private javax.swing.JTextField txtfield_Servicios6_1;
    private javax.swing.JTextField txtfield_Servicios6_2;
    private javax.swing.JTextField txtfield_Servicios6_3;
    private javax.swing.JTextField txtfield_Servicios6_4;
    private javax.swing.JTextField txtfield_Telefono;
    private javax.swing.JTextField txtfield_Telefono1;
    private javax.swing.JTextField txtfield_Telefono2;
    private javax.swing.JTextField txtfield_Telefono4;
    private javax.swing.JTextField txtfield_nsBateria;
    private javax.swing.JTextField txtfield_nsCargador;
    private javax.swing.JTextField txtfield_nsEquipo;
    private javax.swing.JTextField txtfield_numeroCasa;
    private javax.swing.JTextField txtfield_numeroCasa1;
    // End of variables declaration//GEN-END:variables
}
