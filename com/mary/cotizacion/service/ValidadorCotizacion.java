package com.mary.cotizacion.service;

import com.mary.cotizacion.model.CotizacionModel;

/**
 * Clase encargada exclusivamente de la lógica de validación de negocio.
 * Aplica el principio de Responsabilidad Única (SRP).
 */
public class ValidadorCotizacion {

    public String dejarSoloNumeros(String texto) {
        if (texto == null) return "";
        StringBuilder numeros = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (caracter >= '0' && caracter <= '9') {
                numeros.append(caracter);
            }
        }
        return numeros.toString();
    }

    public boolean tieneNumero(String texto) {
        return !dejarSoloNumeros(texto).isEmpty();
    }

    public String validarNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return "Escribe tu nombre.";
        }
        if (nombre.length() < 2) {
            return "Escribe tu nombre con al menos 2 letras.";
        }
        if (tieneNumero(nombre)) {
            return "Escribe tu nombre con letras, sin números.";
        }
        return "";
    }

    public String validarTelefono(String escrito) {
        if (escrito == null || escrito.isEmpty()) {
            return "Escribe tu número de celular.";
        }
        String digitos = dejarSoloNumeros(escrito);
        if (digitos.length() == 12 && digitos.startsWith("57")) {
            digitos = digitos.substring(2);
        }
        if (digitos.length() == 10 && digitos.charAt(0) == '3') {
            return "";
        }
        return "Debe ser un celular de 10 dígitos, como 321 782 3068.";
    }

    public String formatearTelefono(String escrito) {
        String digitos = dejarSoloNumeros(escrito);
        if (digitos.length() == 12) {
            digitos = digitos.substring(2);
        }
        if (digitos.length() == 10) {
            return "+57 " + digitos.substring(0, 3) + " " + digitos.substring(3, 6) + " " + digitos.substring(6);
        }
        return escrito;
    }

    public String validarTipo(boolean empresa, boolean residencial) {
        if (empresa || residencial) {
            return "";
        }
        return "Indica si es para una empresa o para un hogar.";
    }

    public String validarServicio(int posicion) {
        if (posicion == 0) {
            return "Elige el servicio que quieres cotizar.";
        }
        return "";
    }

    public String validarCiudad(String ciudad) {
        if (ciudad == null || ciudad.isEmpty()) {
            return "Escribe la ciudad.";
        }
        if (ciudad.length() < 2 || tieneNumero(ciudad)) {
            return "Escribe la ciudad con letras, por ejemplo Medellín.";
        }
        return "";
    }

    public String validarMensaje(String mensaje) {
        if (mensaje != null && mensaje.length() > 300) {
            return "Máximo 300 caracteres.";
        }
        return "";
    }
}
