package org.josuequezada.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.josuequezada.bean.Empresa;
import org.josuequezada.db.Conexion;
import org.josuequezada.main.Principal;
import org.josuequezada.report.GenerarReporte;

public class EmpresaController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };
    

    private String LogoTKRG = "/org/josuequezada/image/LogoSecundario.png";
    private String logo = "/org/josuequezada/image/LogoSecundario.png";
    private String Empresa = "/org/josuequezada/image/Empresa1.png";
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empresa> listaEmpresa;

    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();

    }

    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("telefono"));
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

    public void generarReporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
            break;
            case ACTUALIZAR:
            
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap ();
        parametros.put("codigoEmpresa", null);
        parametros.put("logo", this.getClass().getResourceAsStream(logo));
        parametros.put("Empresa", this.getClass().getResourceAsStream(Empresa));
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
    
    public void generarReportePrincipal(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
            break;
        }
    }
    
    public void imprimirReportePrincipal(){
        if (tblEmpresas.getSelectionModel().isEmpty()) {
            return;
        }
        Empresa empSelec = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
        int codEmpresa = empSelec.getCodigoEmpresa();
        Map<String, Object> parametros = new HashMap<>();parametros.put("codEmpresa", codEmpresa);
        parametros.put("logo", this.getClass().getResourceAsStream(logo));
        GenerarReporte.mostrarReporte("ReporteGeneralTonnysKinal.jasper", "Reporte General", parametros);
    }
    
    public void desactivarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }

    public void activarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
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
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    
    ///////////////////////////////////////////////////////////////////
    
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro ya que contiene llave foranea?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EliminarEmpresa(?)");
                            procedimiento.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedItem());
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    
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
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Cancelar");
                imgEditar.setImage(new Image("/org/josuequezada/image/Actualizar.png"));
                imgReporte.setImage(new Image("/org/josuequezada/image/Cancelar.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                
            break;
        }
    }
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa (?,?,?,?)");
            Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar() {
        Empresa registro = new Empresa();
//      registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarEmpresa(?, ?, ?)"); 
           procedimiento.setString(1, registro.getNombreEmpresa());
           procedimiento.setString(2, registro.getDireccion());
           procedimiento.setString(3, registro.getTelefono());
           procedimiento.execute();
           listaEmpresa.add(registro);
        }catch(Exception e){
           e.printStackTrace(); 
        }
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
    }
    
}
 
  