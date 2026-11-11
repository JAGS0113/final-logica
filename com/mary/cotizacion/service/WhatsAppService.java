package com.mary.cotizacion.service;

import com.mary.cotizacion.model.CotizacionModel;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Servicio encargado del formato y envío de mensajes hacia WhatsApp.
 */
public class WhatsAppService {
    private final String numeroWhatsapp;

    public WhatsAppService(String numeroWhatsapp) {
        this.numeroWhatsapp = numeroWhatsapp;
    }

    public String armarMensaje(CotizacionModel modelo, String telefonoFormateado) {
        StringBuilder texto = new StringBuilder();
        texto.append("Hola, quiero solicitar una cotización.\n\n");
        texto.append("Nombre: ").append(modelo.getNombre()).append("\n");
        texto.append("Teléfono: ").append(telefonoFormateado).append("\n");
        texto.append("Tipo de cliente: ").append(modelo.getTipoCliente()).append("\n");
        texto.append("Servicio: ").append(modelo.getServicioSeleccionado()).append("\n");
        texto.append("Ciudad: ").append(modelo.getCiudad());
        if (modelo.getMensaje() != null && !modelo.getMensaje().isEmpty()) {
            texto.append("\nMensaje: ").append(modelo.getMensaje());
        }
        return texto.toString();
    }

    public void abrirWhatsApp(String textoMensaje) throws Exception {
        String url = "https://wa.me/" + numeroWhatsapp + "?text="
                + URLEncoder.encode(textoMensaje, StandardCharsets.UTF_8).replace("+", "%20");
        Desktop.getDesktop().browse(new URI(url));
    }
}
