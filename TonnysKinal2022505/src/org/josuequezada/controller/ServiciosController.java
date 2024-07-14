
package org.josuequezada.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import static java.time.temporal.TemporalQueries.localTime;
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
import org.josuequezada.bean.Empresa;
import org.josuequezada.bean.Servicio;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class ServiciosController implements Initializable{
    private Principal escenarioPrincipal;
    
    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO;
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private GridPane grpFechaServicio;
    @FXML private GridPane grpHoraServicio;
    @FXML private TableView tblServicio;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Spinner spHora;
    @FXML private Spinner spMinuto;
    @FXML private Spinner spSegundo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private ComboBox cmbCodigoEmpresa;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/josuequezada/resource/TonysKinal.css");
        grpFechaServicio.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
        spHora.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        spMinuto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        spSegundo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
    }
    
    public void cargarDatos(){
        tblServicio.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory <Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory <Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory <Servicio, Integer>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory <Servicio, Time>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory <Servicio, Integer>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory <Servicio, Integer>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory <Servicio, Integer>("codigoEmpresa"));
    }
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        fecha.setDisable(true);
        spHora.setDisable(true);
        spMinuto.setDisable(true);
        spSegundo.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        fecha.setDisable(false);
        spHora.setDisable(false);
        spMinuto.setDisable(false);
        spSegundo.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoServicio.clear();
        txtTipoServicio.clear();
        txtLugarServicio.clear();
        txtTelefonoContacto.clear();
        fecha.selectedDateProperty().set(null);
        spHora.getEditor().clear();
        spMinuto.getEditor().clear();
        spSegundo.getEditor().clear();
        cmbCodigoEmpresa.setValue(null);
    }
    
    public void seleccionarElemento() {
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtTipoServicio.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTipoServicio()));
        Time hSer = (((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getHoraServicio());
        LocalTime localTime = hSer.toLocalTime();
        int hora = localTime.getHour();
        int minuto = localTime.getMinute();
        int segundo = localTime.getSecond();
        spHora.getEditor().setText(String.valueOf(hora));
        spMinuto.getEditor().setText(String.valueOf(minuto));
        spSegundo.getEditor().setText(String.valueOf(segundo));
        txtLugarServicio.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getLugarServicio()));
        txtTelefonoContacto.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
    
    public Empresa buscarEmpresa (int codigoEmpresa){
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa (?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
            }
            
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return resultado;
    }
    
    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarEmpresas ()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpresa = FXCollections.observableArrayList(lista);
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
    ////////////////////////////////////////////////////////////////////////////
    public void nuevo (){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/josuequezada/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/josuequezada/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        } 
    }
    
     public void guardar(){
       Servicio registro = new Servicio();
       registro.setFechaServicio(fecha.getSelectedDate());
       registro.setTipoServicio(txtTipoServicio.getText());
       int hora = Integer.parseInt(spHora.getEditor().getText());
       int minuto = Integer.parseInt(spMinuto.getEditor().getText());
       int segundo = Integer.parseInt(spSegundo.getEditor().getText());
       Time hSer = new Time (hora,minuto,segundo);
       registro.setHoraServicio(hSer);
       registro.setLugarServicio(txtLugarServicio.getText());
       registro.setTelefonoContacto(txtTelefonoContacto.getText());
       registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
       try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarServicio(?,?,?,?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, new java.sql.Time(registro.getHoraServicio().getTime()));
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/josuequezada/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/josuequezada/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
            default:
                if(tblServicio.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicio.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblServicio.getSelectionModel().getSelectedItem() != null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/josuequezada/image/Actu.png"));
                    imgReporte.setImage(new Image("/org/josuequezada/image/Cancelar.png"));;
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
            break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/josuequezada/image/Actualizar.png"));
                imgReporte.setImage(new Image("/org/josuequezada/image/Reporte.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
            break;   
        }   
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio (?,?,?,?,?,?,?)");
            Servicio registro = (Servicio)tblServicio.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            int hora = Integer.parseInt(spHora.getEditor().getText());
            int minuto = Integer.parseInt(spMinuto.getEditor().getText());
            int segundo = Integer.parseInt(spSegundo.getEditor().getText());
            Time hSer = new Time (hora,minuto,segundo);
            registro.setHoraServicio(hSer);
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setTime(4, new java.sql.Time(registro.getHoraServicio().getTime()));
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setInt(7, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/josuequezada/image/Actualizar.png"));
                imgReporte.setImage(new Image("/org/josuequezada/image/Reporte.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                
            break;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
}
