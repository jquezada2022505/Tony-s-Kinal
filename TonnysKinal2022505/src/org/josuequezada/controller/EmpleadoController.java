
package org.josuequezada.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.josuequezada.bean.Empleado;
import org.josuequezada.bean.TipoEmpleado;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class EmpleadoController implements Initializable {
    
    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO;
    }
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtCodigoEmpleado; 
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colApellidosEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos(); 
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
        desactivarControles();
    }
    
    public void cargarDatos(){
       tblEmpleado.setItems(getEmpleado());
       colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("codigoEmpleado"));
       colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("numeroEmpleado"));
       colNombresEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("nombresEmpleado"));
       colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("apellidosEmpleado"));
       colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("direccionEmpleado"));
       colTelefonoContacto.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("telefonoContacto"));
       colGradoCocinero.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("gradoCocinero"));;
       colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("codigoTipoEmpleado"));;
    }
    
    public void seleccionarElemento(){
       txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
       txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
       txtApellidosEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
       txtNombresEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
       txtDireccionEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getDireccionEmpleado()));
       txtTelefonoContacto.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
       txtGradoCocinero.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getGradoCocinero()));
       cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
    }
    
    public void desactivarControles(){
       txtCodigoEmpleado.setEditable(false);
       txtNumeroEmpleado.setEditable(false);
       txtApellidosEmpleado.setEditable(false);
       txtNombresEmpleado.setEditable(false);
       txtDireccionEmpleado.setEditable(false);
       txtTelefonoContacto.setEditable(false);
       txtGradoCocinero.setEditable(false);
       cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void activarControles(){
       txtCodigoEmpleado.setEditable(false);
       txtNumeroEmpleado.setEditable(true);
       txtApellidosEmpleado.setEditable(true);
       txtNombresEmpleado.setEditable(true);
       txtDireccionEmpleado.setEditable(true);
       txtTelefonoContacto.setEditable(true);
       txtGradoCocinero.setEditable(true);
       cmbCodigoTipoEmpleado.setDisable(false);
    }
    
    public void limpiarControles(){
       txtCodigoEmpleado.clear();
       txtNumeroEmpleado.clear();
       txtApellidosEmpleado.clear();
       txtNombresEmpleado.clear();
       txtDireccionEmpleado.clear();
       txtTelefonoContacto.clear();
       txtGradoCocinero.clear();
       cmbCodigoTipoEmpleado.setValue(null);
    }
    
    public TipoEmpleado buscarTipoEmpleado (int codigoTipoEmpleado){
        TipoEmpleado resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado (?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado (registro.getInt("codigoTipoEmpleado"),
                                            registro.getString("descripcion"));
            }
            
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return resultado;
    }
    
    
    public ObservableList <Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
         try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarEmpleados ()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
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
    
     public ObservableList<TipoEmpleado> getTipoEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        
            try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarTipoEmpleado()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    ///////////////////////////////////////////
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
       Empleado registro = new Empleado();
       registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
       registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
       registro.setNombresEmpleado(txtNombresEmpleado.getText());
       registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
       registro.setTelefonoContacto(txtTelefonoContacto.getText());
       registro.setGradoCocinero(txtGradoCocinero.getText());
       registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
       try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
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
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EliminarEmpleados(?)");
                            procedimiento.setInt(1, ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleado.getSelectionModel().getSelectedItem());
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
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado (?,?,?,?,?,?,?,?)");
            Empleado registro = (Empleado)tblEmpleado.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.setInt(8, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
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
    ///////////////////////////////////////////
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
