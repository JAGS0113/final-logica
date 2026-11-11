package com.mary.cotizacion.ui;

import com.mary.cotizacion.model.CotizacionModel;
import com.mary.cotizacion.service.ValidadorCotizacion;
import com.mary.cotizacion.service.WhatsAppService;

import javax.swing.*;
import java.awt.*;

/**
 * Capa de Interfaz Gráfica (Vistas) construida con Swing.
 */
public class FormularioCotizacionUI extends JFrame {

    private static final String NUMERO_WHATSAPP = "573217823068";

    // Colores corporativos
    private static final Color AZUL_OSCURO = new Color(0x02, 0x06, 0x13);
    private static final Color AZUL = new Color(0x19, 0x57, 0xA1);
    private static final Color AZUL_CLARO = new Color(0x31, 0x90, 0xDD);
    private static final Color ROJO = new Color(0xC6, 0x28, 0x28);

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JRadioButton rbEmpresa = new JRadioButton("Empresa");
    private final JRadioButton rbResidencial = new JRadioButton("Residencial");
    
    private final String[] servicios = {
        "Selecciona un servicio", "Cámaras de seguridad (CCTV)", "Redes de datos",
        "Puntos POS", "Recuperación de datos", "Instalación de software",
        "Mantenimiento y reparación de PC", "Soporte preventivo"
    };
    private final JComboBox<String> cbServicio = new JComboBox<>(servicios);
    private final JTextField txtCiudad = new JTextField("Medellín");
    private final JTextArea txtMensaje = new JTextArea(3, 20);

    private final JLabel errNombre = crearError();
    private final JLabel errTelefono = crearError();
    private final JLabel errTipo = crearError();
    private final JLabel errServicio = crearError();
    private final JLabel errCiudad = crearError();
    private final JLabel errMensaje = crearError();
    private final JLabel lblEstado = new JLabel(" ");

    // Dependencias de servicios
    private final ValidadorCotizacion validador = new ValidadorCotizacion();
    private final WhatsAppService whatsAppService = new WhatsAppService(NUMERO_WHATSAPP);

    public FormularioCotizacionUI() {
        setTitle("Jags Technology · Solicita tu cotización");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 720);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        // Encabezado
        JPanel encabezado = new JPanel(new GridLayout(2, 1));
        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        encabezado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        encabezado.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel titulo = new JLabel("Solicita tu cotización");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel sub = new JLabel("Te respondemos por WhatsApp · +57 321 7823068");
        sub.setForeground(AZUL_CLARO);
        encabezado.add(titulo);
        encabezado.add(sub);
        panel.add(encabezado);

        // Agrupar Radio Buttons
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbEmpresa);
        grupoTipo.add(rbResidencial);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTipo.setBackground(Color.WHITE);
        rbEmpresa.setBackground(Color.WHITE);
        rbResidencial.setBackground(Color.WHITE);
        panelTipo.add(rbEmpresa);
        panelTipo.add(Box.createHorizontalStrut(16));
        panelTipo.add(rbResidencial);

        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        JScrollPane scrollMensaje = new JScrollPane(txtMensaje);

        agregarCampo(panel, "Nombre *", txtNombre, errNombre);
        agregarCampo(panel, "Teléfono *", txtTelefono, errTelefono);
        agregarCampo(panel, "Tipo de cliente *", panelTipo, errTipo);
        agregarCampo(panel, "Servicio *", cbServicio, errServicio);
        agregarCampo(panel, "Ciudad *", txtCiudad, errCiudad);
        agregarCampo(panel, "Mensaje (opcional)", scrollMensaje, errMensaje);

        JButton btnEnviar = new JButton("Solicitar cotización");
        btnEnviar.setBackground(AZUL);
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setOpaque(true);
        btnEnviar.setBorderPainted(false);
        btnEnviar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnEnviar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEnviar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JPanel zonaBoton = new JPanel();
        zonaBoton.setLayout(new BoxLayout(zonaBoton, BoxLayout.Y_AXIS));
        zonaBoton.setBackground(Color.WHITE);
        zonaBoton.setBorder(BorderFactory.createEmptyBorder(8, 20, 0, 20));
        zonaBoton.setAlignmentX(Component.LEFT_ALIGNMENT);
        zonaBoton.add(btnEnviar);
        zonaBoton.add(Box.createVerticalStrut(8));
        zonaBoton.add(lblEstado);
        panel.add(zonaBoton);

        btnEnviar.addActionListener(e -> procesarFormulario());

        add(new JScrollPane(panel));
    }

    private void procesarFormulario() {
        CotizacionModel modelo = new CotizacionModel();
        modelo.setNombre(txtNombre.getText());
        modelo.setTelefono(txtTelefono.getText());
        modelo.setEmpresa(rbEmpresa.isSelected());
        modelo.setResidencial(rbResidencial.isSelected());
        modelo.setPosicionServicio(cbServicio.getSelectedIndex());
        if (modelo.getPosicionServicio() >= 0 && modelo.getPosicionServicio() < servicios.length) {
            modelo.setServicioSeleccionado(servicios[modelo.getPosicionServicio()]);
        }
        modelo.setCiudad(txtCiudad.getText());
        modelo.setMensaje(txtMensaje.getText());

        boolean formularioValido = true;
        formularioValido = revisar(errNombre, validador.validarNombre(modelo.getNombre()), formularioValido);
        formularioValido = revisar(errTelefono, validador.validarTelefono(modelo.getTelefono()), formularioValido);
        formularioValido = revisar(errTipo, validador.validarTipo(modelo.isEmpresa(), modelo.isResidencial()), formularioValido);
        formularioValido = revisar(errServicio, validador.validarServicio(modelo.getPosicionServicio()), formularioValido);
        formularioValido = revisar(errCiudad, validador.validarCiudad(modelo.getCiudad()), formularioValido);
        formularioValido = revisar(errMensaje, validador.validarMensaje(modelo.getMensaje()), formularioValido);

        if (!formularioValido) {
            lblEstado.setForeground(ROJO);
            lblEstado.setText("Revisa los campos marcados en rojo.");
            return;
        }

        String telefonoFormateado = validador.formatearTelefono(modelo.getTelefono());
        String mensajeTexto = whatsAppService.armarMensaje(modelo, telefonoFormateado);

        try {
            whatsAppService.abrirWhatsApp(mensajeTexto);
            lblEstado.setForeground(AZUL);
            lblEstado.setText("Listo. Se abrió WhatsApp con tu cotización.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, mensajeTexto, "Mensaje para WhatsApp", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean revisar(JLabel etiquetaError, String error, boolean valido) {
        if (error.isEmpty()) {
            etiquetaError.setText(" ");
            return valido;
        } else {
            etiquetaError.setText(error);
            return false;
        }
    }

    private JLabel crearError() {
        JLabel l = new JLabel(" ");
        l.setForeground(ROJO);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    private void agregarCampo(JPanel panel, String texto, JComponent campo, JLabel error) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.Y_AXIS));
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(AZUL_OSCURO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (!(campo instanceof JScrollPane)) {
            campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        }
        error.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(etiqueta);
        fila.add(Box.createVerticalStrut(4));
        fila.add(campo);
        fila.add(error);
        panel.add(fila);
    }
}
