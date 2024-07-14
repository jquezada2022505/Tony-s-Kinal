/*
Quinto perito en Informatica
IN5BV
Nombre: Josué Antonio
Apellido: Quezada Arriaga
Carnet: 2022505
Fecha de Creacion: 14/04/2023
Fecha de Modificacion: 14/04/2023 22:20 
         Finalizacion: 14/04/2023 23:02
         Modificacion: 26/04/2023 19:30 - 10:27
         Modificacion: 27/04/2023 13:15 - 17:30
                       27/04/2023 23:01 - 28/04/2023 01:28
         Modificacion: 28/04/2023 13:04 - 28/04/2023 17:22 
                       28/04/2023 19:47 - 28/04/2023 22:39
        
 */
package org.josuequezada.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.josuequezada.controller.EmpleadoController;
import org.josuequezada.controller.EmpresaController;
import org.josuequezada.controller.LoginController;
import org.josuequezada.controller.MenuPrincipalController;
import org.josuequezada.controller.PlatoController;
import org.josuequezada.controller.PresupuestoController;
import org.josuequezada.controller.ProductoController;
import org.josuequezada.controller.ProductosHasPlatosController;
import org.josuequezada.controller.ProgramadorController;
import org.josuequezada.controller.ServiciosController;
import org.josuequezada.controller.ServiciosHasEmpleadosController;
import org.josuequezada.controller.ServiciosHasPlatosController;
import org.josuequezada.controller.TipoEmpleadoController;
import org.josuequezada.controller.TipoPlatoController;
import org.josuequezada.controller.UsuarioController;
import org.josuequezada.report.GenerarReporte;

public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/josuequezada/view/";
    private String logo = "/org/josuequezada/image/LogoSecundario.png";
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Stage escenarioPrincipal;
    private Scene escena;
    
    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO;
    }

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony´s Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/josuequezada/image/LogoSecundario.png"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/josuequezada/view/EmpresaView.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/josuequezada/view/MenuPrincipalView.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/josuequezada/view/Programador1View.fxml"));
//        Scene escena = new Scene(root);
//        escenarioPrincipal.setScene(escena);
        login();
        escenarioPrincipal.show();
    }

    
    
    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 600, 500);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController tipoPlat = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 950, 515);
            tipoPlat.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoEmple = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 950, 515);
            tipoEmple.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProducto() {
        try {
            ProductoController produ = (ProductoController) cambiarEscena("ProductoView.fxml", 950, 500);
            produ.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProgramador() {
        try {
            ProgramadorController progra = (ProgramadorController) cambiarEscena("Programador1View.fxml", 500, 300);
            progra.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresa() {
        try {
            EmpresaController empre = (EmpresaController) cambiarEscena("EmpresaView.fxml", 950, 500);
            empre.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try {
            PresupuestoController presu = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 950, 500);
            presu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            }
    }
    
    public void ventanaEmpleado() {
        try {
            EmpleadoController EmpleCon = (EmpleadoController) cambiarEscena("EmpleadoView.fxml", 1300, 515);
            EmpleCon.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato() {
        try {
            PlatoController PlatoCon = (PlatoController) cambiarEscena("PlatoView.fxml", 1300, 515);
            PlatoCon.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
    public void ventanaProductosHasPlato() {
        try {
            ProductosHasPlatosController ProduHasPla = (ProductosHasPlatosController) cambiarEscena("ProductosHasPlatosView.fxml", 950, 515);
            ProduHasPla.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosHasPlato() {
        try {
            ServiciosHasPlatosController SerHasPla = (ServiciosHasPlatosController) cambiarEscena("ServiciosHasPlatosView.fxml", 950, 515);
            SerHasPla.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosHasEmpleados() {
        try {
            ServiciosHasEmpleadosController SerHasEm = (ServiciosHasEmpleadosController) cambiarEscena("ServiciosHasEmpleadosView.fxml", 1050, 515);
            SerHasEm.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicios() {
        try {
            ServiciosController Ser = (ServiciosController) cambiarEscena("ServicioView.fxml", 1300, 515);
            Ser.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void login(){
        try {
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml",500,300);
            login.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try {
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml", 600, 500);
            usuario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
   
    public static void main(String[] args) {
        launch(args);
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }

}
