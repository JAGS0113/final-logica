package com.mary.cotizacion;

import com.mary.cotizacion.ui.FormularioCotizacionUI;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormularioCotizacionUI ventana = new FormularioCotizacionUI();
            ventana.setVisible(true);
        });
    }
}
