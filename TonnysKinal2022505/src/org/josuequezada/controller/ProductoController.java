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
import org.josuequezada.bean.Producto;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;

public class ProductoController implements Initializable {

    private Principal escenarioPrincipal;
    
    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Producto> listaProducto;
    
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableView tblProductos;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCodigoProducto;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
    }
    
     public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<Producto>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarProductos ()");
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
     
    public void desactivarControles() {
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }

    public void activarControles() {
        txtCodigoProducto.setEditable(true);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidad.clear();
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
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EliminarProducto(?)");
                            procedimiento.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
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
          PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?,?,?)");
          Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
          registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
          registro.setNombreProducto(txtNombreProducto.getText());
          registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
          procedimiento.setInt(1, registro.getCodigoProducto());
          procedimiento.setString(2, registro.getNombreProducto());
          procedimiento.setInt(3, registro.getCantidad());
          procedimiento.execute();
        } catch (Exception e) {
        }
    }
    
    public void seleccionarElemento(){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
    }
    
    public void reporte (){
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
        }
    }
    
    public void guardar() {
        Producto registro = new Producto();
        registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarProducto(?, ?, ?)"); 
           procedimiento.setInt(1, registro.getCodigoProducto());
           procedimiento.setString(2, registro.getNombreProducto());
           procedimiento.setInt(3, registro.getCantidad());
           procedimiento.execute();
           listaProducto.add(registro);
        }catch(Exception e){
           e.printStackTrace(); 
        }
    }
}
