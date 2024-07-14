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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.josuequezada.bean.TipoEmpleado;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class TipoEmpleadoController implements Initializable {
    
    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private Principal escenarioPrincipal;

    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcionTipoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcionTipoEmpleado;
    @FXML private TableView tblTipoEmpleado;
    
    //
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos() {
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcionTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("descripcion"));
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
    
    //
    public void desactivarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
    }
    
    public void activarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoTipoEmpleado.clear();
        txtDescripcionTipoEmpleado.clear();
    }
    
    //
    public void guardar(){
        TipoEmpleado registro = new TipoEmpleado();
        // registro.setCodigoTipoEmpleado(Integer.parseInt(txtCodigoTipoEmpleado.getText()));
        registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarTipoEmpleado(?)"); 
           procedimiento.setString(1, registro.getDescripcion());
           procedimiento.execute();
           listaTipoEmpleado.add(registro);
        }catch(Exception e){
           e.printStackTrace(); 
        }
    }
    
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
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar TipoEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EliminarTipoEmpleado(?)");
                                procedimiento.setInt(1, ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                                procedimiento.execute();
                                listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedItem());
                                limpiarControles();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                }else{
                    
                }
        }
    }
    
    public void editar(){
       switch(tipoDeOperacion){
           case NINGUNO:
               if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                 
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
               btnReporte.setText("Cancelar");
               imgEditar.setImage(new Image("/org/josuequezada/image/Actualizar.png"));
               imgReporte.setImage(new Image("/org/josuequezada/image/Reporte.png"));
               cargarDatos();
               tipoDeOperacion = operaciones.NINGUNO;
            break;
        }
    } 
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado (?,?)");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        } catch (Exception e) {
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
    public void seleccionarElemento(){
        txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtDescripcionTipoEmpleado.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
    }
    //
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
}
