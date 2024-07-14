
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
import org.josuequezada.bean.Producto;
import org.josuequezada.bean.ProductosHasPlatos;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class ProductosHasPlatosController implements Initializable{
    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO;
    }
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ProductosHasPlatos> listaProductosHasPlatos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TextField txtProductosCodigoProducto;
    @FXML private TableView tblProductosHasPlatos;
    @FXML private TableColumn colProductosCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
            
    
    
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
        cmbCodigoProducto.setItems(getProducto());
        desactivarControles();

    }
    
    public void cargarDatos() {
        tblProductosHasPlatos.setItems(getProductosHasPlatos());
        colProductosCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoProducto"));
    }
    
    public void desactivarControles() {
        txtProductosCodigoProducto.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }
    
    public void activarControles() {
        txtProductosCodigoProducto.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }
    
    public void limpiarControles() {
        txtProductosCodigoProducto.clear();
        cmbCodigoPlato.setValue(null);
        cmbCodigoProducto.setValue(null);
    }
    
    public ObservableList<ProductosHasPlatos> getProductosHasPlatos() {
        ArrayList<ProductosHasPlatos> lista = new ArrayList<ProductosHasPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarProductos_has_Platos ()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ProductosHasPlatos(resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductosHasPlatos = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento() {
        txtProductosCodigoProducto.setText(String.valueOf(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarCodigoPlato(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cmbCodigoProducto.getSelectionModel().select(buscarCodigoProducto(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }
    
    public Plato buscarCodigoPlato(int codigoPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_BuscarPlatos(?)");
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
    
     public Producto buscarCodigoProducto(int codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getInt("codigoProducto"),
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidad"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
    
    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("Call sp_ListarProductos ()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    /////////////////////////////////////////////////////////////////////////////
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
         ProductosHasPlatos registro = new ProductosHasPlatos();
         registro.setProductos_codigoProducto(Integer.parseInt(txtProductosCodigoProducto.getText()));
         registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
         registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        try {
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarProductos_has_Platos(?,?,?)");
             procedimiento.setInt(1, registro.getProductos_codigoProducto());
             procedimiento.setInt(2, registro.getCodigoPlato());
             procedimiento.setInt(3, registro.getCodigoProducto());
             procedimiento.execute();
             listaProductosHasPlatos.add(registro);
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
