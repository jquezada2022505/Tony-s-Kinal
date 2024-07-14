
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
import org.josuequezada.bean.TipoPlato;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class TipoPlatoController implements Initializable {
    
    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;
    private Principal escenarioPrincipal;

    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private TableColumn colDescripcionTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipoPlato;
    @FXML private TableView tblTipoPlato;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public ObservableList<TipoPlato> getTipoPlato() {
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        
            try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarTipoPlatos ()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos() {
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("descripcionTipo"));
    }
    
    //
    public void desactivarControles() {
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);
    }
    
    public void activarControles() {
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoTipoPlato.clear();
        txtDescripcionTipoPlato.clear();
    }
    
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        // registro.setCodigoTipoEmpleado(Integer.parseInt(txtCodigoTipoEmpleado.getText()));
        registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarTipoPlato(?)"); 
           procedimiento.setString(1, registro.getDescripcionTipo());
           procedimiento.execute();
           listaTipoPlato.add(registro);
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
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Tipo Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EliminarTipoPlato(?)");
                            procedimiento.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedItem());
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
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
            procedimiento.execute();
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
    
    public void seleccionarElemento(){
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcionTipoPlato.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());
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
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
}
