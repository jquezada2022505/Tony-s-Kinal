package org.josuequezada.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.josuequezada.main.Principal;

public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaTipoPlato() {
        escenarioPrincipal.ventanaTipoPlato();
    }

    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

    public void ventanaProducto() {
        escenarioPrincipal.ventanaProducto();
    }

    public void ventanaProgramador() {
        escenarioPrincipal.ventanaProgramador();
    }

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void ventanaProductosHasPlato() {
        escenarioPrincipal.ventanaProductosHasPlato();
    }

    public void ventanaServiciosHasPlato() {
        escenarioPrincipal.ventanaServiciosHasPlato();
    }

    public void ventanaServiciosHasEmpleados() {
        escenarioPrincipal.ventanaServiciosHasEmpleados();
    }

    public void ventanaServicios() {
        escenarioPrincipal.ventanaServicios();
    }

    public void login() {
        escenarioPrincipal.login();
    }

    public void ventanaUsuario() {
        escenarioPrincipal.ventanaUsuario();
    }

    public void generarReporte() {
        escenarioPrincipal.generarReporte();
    }

}
