package com.mary.cotizacion.model;

/**
 * Modelo de datos que representa una solicitud de cotización.
 * Aplica Encapsulamiento con atributos privados y métodos Getters/Setters.
 */
public class CotizacionModel {
    private String nombre;
    private String telefono;
    private boolean empresa;
    private boolean residencial;
    private int posicionServicio;
    private String servicioSeleccionado;
    private String ciudad;
    private String mensaje;

    public CotizacionModel() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.trim() : "";
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono != null ? telefono.trim() : "";
    }

    public boolean isEmpresa() {
        return empresa;
    }

    public void setEmpresa(boolean empresa) {
        this.empresa = empresa;
    }

    public boolean isResidencial() {
        return residencial;
    }

    public void setResidencial(boolean residencial) {
        this.residencial = residencial;
    }

    public int getPosicionServicio() {
        return posicionServicio;
    }

    public void setPosicionServicio(int posicionServicio) {
        this.posicionServicio = posicionServicio;
    }

    public String getServicioSeleccionado() {
        return servicioSeleccionado;
    }

    public void setServicioSeleccionado(String servicioSeleccionado) {
        this.servicioSeleccionado = servicioSeleccionado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad != null ? ciudad.trim() : "";
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje != null ? mensaje.trim().replace("\n", " ") : "";
    }

    public String getTipoCliente() {
        return empresa ? "Empresa" : "Residencial";
    }
}
