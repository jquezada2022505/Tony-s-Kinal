
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
import org.josuequezada.bean.Plato;
import org.josuequezada.bean.Servicio;
import org.josuequezada.bean.ServiciosHasPlatos;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class ServiciosHasPlatosController implements Initializable {
    
    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO;
    }
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosHasPlatos> listaServiciosHasPlatos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Servicio> listaServicio;

    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private TextField txtServiciosCodigoServicio;
    @FXML private TableView tblServiciosHasPlatos;
    @FXML private TableColumn colServiciosCodigoServicios;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicio());
        desactivarControles();
    }
    
    public void cargarDatos() {
        tblServiciosHasPlatos.setItems(getServiciosHasPlatos());
        colServiciosCodigoServicios.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoServicio"));
    }
    
    public ObservableList<ServiciosHasPlatos> getServiciosHasPlatos() {
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarServicios_has_Platos ()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServiciosHasPlatos(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServiciosHasPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarPlatos ()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
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
    
    public void desactivarControles() {
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }
    
    public void activarControles() {
        txtServiciosCodigoServicio.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }
    
    public void limpiarControles() {
        txtServiciosCodigoServicio.clear();
        cmbCodigoPlato.setValue(null);
        cmbCodigoServicio.setValue(null);
    }
    
    public void seleccionarElemento() {
        txtServiciosCodigoServicio.setText(String.valueOf(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarCodigoPlato(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cmbCodigoServicio.getSelectionModel().select(buscarCodigoServicio(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
    }
     public Plato buscarCodigoPlato(int codigoPlato){
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_BuscarPlatos (?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
     
    public Servicio buscarCodigoServicio(int codigoServicio) {
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
        ServiciosHasPlatos registro = new ServiciosHasPlatos();
        registro.setServicios_codigoServicio(Integer.parseInt(txtServiciosCodigoServicio.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarServicios_has_Platos(?, ?, ?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
            listaServiciosHasPlatos.add(registro);
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
}
