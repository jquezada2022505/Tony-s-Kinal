
package org.josuequezada.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.josuequezada.bean.Empleado;
import org.josuequezada.bean.Servicio;
import org.josuequezada.bean.ServiciosHasEmpleados;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class ServiciosHasEmpleadosController implements Initializable  {
    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO;
    }
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosHasEmpleados> listaServiciosHasEmpleados;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<Servicio> listaServicio;
    private DatePicker fecha;
    
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private TextField txtServiciosCodigoServicio;
    @FXML private TextField txtLugarEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private GridPane grpFechaEvento;
    @FXML private GridPane grpHoraEvento;
    @FXML private TableView tblServiciosHasEmpleados;
    @FXML private TableColumn colServiciosCodigoServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private Spinner spHoraEvento;
    @FXML private Spinner spMinutoEvento;
    @FXML private Spinner spSegundoEvento;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/josuequezada/resource/TonysKinal.css");
        grpFechaEvento.add(fecha, 3, 0);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        spHoraEvento.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        spMinutoEvento.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        spSegundoEvento.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServiciosHasEmpleados());
        colServiciosCodigoServicios.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory <ServiciosHasEmpleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory <ServiciosHasEmpleados, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory <ServiciosHasEmpleados, Integer>("lugarEvento"));
        
    }
    
    public ObservableList<ServiciosHasEmpleados> getServiciosHasEmpleados() {
        ArrayList<ServiciosHasEmpleados> lista = new ArrayList<ServiciosHasEmpleados>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarServicios_has_Empleados ()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServiciosHasEmpleados(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoEmpleado"),
                        resultado.getDate("fechaEvento"),
                        resultado.getTime("horaEvento"),
                        resultado.getString("lugarEvento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServiciosHasEmpleados = FXCollections.observableArrayList(lista);
    }
    
    public void desactivarControles(){
        txtServiciosCodigoServicio.setEditable(false);
        txtLugarEvento.setEditable(false);
        fecha.setDisable(true);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        spHoraEvento.setDisable(true);
        spMinutoEvento.setDisable(true);
        spSegundoEvento.setDisable(true);
    }
    
    public void activarControles(){
        txtServiciosCodigoServicio.setEditable(true);
        txtLugarEvento.setEditable(true);
        fecha.setDisable(false);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        fecha.setDisable(false);
        spHoraEvento.setDisable(false);
        spMinutoEvento.setDisable(false);
        spSegundoEvento.setDisable(false);
    }
    
    public void limpiarControles(){
        txtServiciosCodigoServicio.clear();
        txtLugarEvento.clear();
        fecha.selectedDateProperty().set(null);
        cmbCodigoServicio.setValue(null);
        cmbCodigoEmpleado.setValue(null);
        spHoraEvento.getEditor().clear();
        spMinutoEvento.getEditor().clear();
        spSegundoEvento.getEditor().clear();
    }
    
    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("Call sp_ListarServicios ()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("Call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento() {
        txtServiciosCodigoServicio.setText(String.valueOf(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        Time hSer = (((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento());
        LocalTime localTime = hSer.toLocalTime();
        int hora = localTime.getHour();
        int minuto = localTime.getMinute();
        int segundo = localTime.getSecond();
        spHoraEvento.getEditor().setText(String.valueOf(hora));
        spMinutoEvento.getEditor().setText(String.valueOf(minuto));
        spSegundoEvento.getEditor().setText(String.valueOf(segundo));
        txtLugarEvento.setText(String.valueOf(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
    }
    
    public Servicio buscarServicio(int codigoServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("call sp_BuscarServicioS(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoServicio"),
                        registro.getTime("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Empleado buscarEmpleado (int codigoEmpleado){
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleados (?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                                        registro.getInt("numeroEmpleado"),
                                        registro.getString("apellidosEmpleado"),
                                        registro.getString("nombresEmpleado"),
                                        registro.getString("direccionEmpleado"),
                                        registro.getString("telefonoContacto"),
                                        registro.getString("gradoCocinero"),
                                        registro.getInt("codigoTipoEmpleado"));
            }
            
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return resultado;
    }
    
    ////////////////////////////////////////////////////////////////////////////
        public void nuevo (){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgAgregar.setImage(new Image("/org/josuequezada/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/josuequezada/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
            break;  
            
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                imgAgregar.setImage(new Image("/org/josuequezada/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/josuequezada/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        } 
    }
        
    public void guardar() {
        ServiciosHasEmpleados registro = new ServiciosHasEmpleados();
        registro.setServicios_codigoServicio(Integer.parseInt(txtServiciosCodigoServicio.getText()));
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        int hora = Integer.parseInt(spHoraEvento.getEditor().getText());
        int minuto = Integer.parseInt(spMinutoEvento.getEditor().getText());
        int segundo = Integer.parseInt(spSegundoEvento.getEditor().getText());
        Time hSer = new Time (hora,minuto,segundo);
        registro.setHoraEvento(hSer);
        registro.setLugarEvento(txtLugarEvento.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarServicios_has_Empleados(?, ?, ?, ?, ?, ?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(5, new java.sql.Time(registro.getHoraEvento().getTime()));
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
            listaServiciosHasEmpleados.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                imgAgregar.setImage(new Image("/org/josuequezada/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/josuequezada/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaServiciosHasEmpleados(){
        escenarioPrincipal.ventanaServiciosHasEmpleados();
    }
    
}
