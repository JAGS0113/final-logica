// ENCABEZADO DEL ARCHIVO: no se ejecuta, solo prepara.
// Orden en Java: 1) package  2) import  3) clase  4) dentro de la clase, el main.

// PAQUETE: la carpeta donde vive este archivo (src/com/mary/cotizacion).
// Siempre va en la primera línea de código.
package com.mary.cotizacion;

// IMPORTS: herramientas que Java ya trae hechas y que vamos a usar.
import java.awt.*;                          // colores, fuentes y cómo se acomodan los elementos
import java.awt.event.ActionEvent;          // el evento del clic
import java.awt.event.ActionListener;       // el que escucha el clic del botón Enviar
import java.net.URI;                        // para abrir el link de WhatsApp
import java.net.URLEncoder;                 // convierte espacios y tildes para que el link no se dañe
import java.nio.charset.StandardCharsets;   // usa UTF-8 para que salgan bien la ñ y las tildes
import javax.swing.*;                       // la ventana, las cajas de texto, los botones y las listas

/*
  Main: todo el formulario en un solo archivo.
  Arriba está la ventana (Swing) y abajo las VALIDACIONES (la lógica),
  que revisan cada dato y devuelven "" si está bien o el mensaje de error.
  1. ENTRADA   -> getText(), isSelected(), getSelectedIndex()
  2. PROCESO   -> validar cada campo con las funciones validar...
  3. DECISIÓN  -> bandera formularioValido
  4. SALIDA    -> abrir WhatsApp con el mensaje armado
*/
public class Main extends JFrame {

    // PUNTO DE INICIO: el programa arranca aquí y muestra la ventana.
    public static void main(String[] args) {
        Main ventana = new Main(); // crea la ventana
        ventana.setVisible(true);                            // la muestra en pantalla
    }

    static final String NUMERO_WHATSAPP = "573217823068";

    // Colores de Jags
    static final Color AZUL_OSCURO = new Color(0x02, 0x06, 0x13);
    static final Color AZUL = new Color(0x19, 0x57, 0xA1);
    static final Color AZUL_CLARO = new Color(0x31, 0x90, 0xDD);
    static final Color ROJO = new Color(0xC6, 0x28, 0x28);

    JTextField txtNombre = new JTextField();
    JTextField txtTelefono = new JTextField();
    JRadioButton rbEmpresa = new JRadioButton("Empresa");
    JRadioButton rbResidencial = new JRadioButton("Residencial");
    String[] servicios = {
        "Selecciona un servicio", "Cámaras de seguridad (CCTV)", "Redes de datos",
        "Puntos POS", "Recuperación de datos", "Instalación de software",
        "Mantenimiento y reparación de PC", "Soporte preventivo"
    };
    JComboBox<String> cbServicio = new JComboBox<>(servicios);
    JTextField txtCiudad = new JTextField("Medellín");
    JTextArea txtMensaje = new JTextArea(3, 20);

    JLabel errNombre = crearError();
    JLabel errTelefono = crearError();
    JLabel errTipo = crearError();
    JLabel errServicio = crearError();
    JLabel errCiudad = crearError();
    JLabel errMensaje = crearError();
    JLabel lblEstado = new JLabel(" ");

    public Main() {
        setTitle("Jags Technology · Solicita tu cotización");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 720);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        // Encabezado azul
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

        // EVENTO: como el botón "=" de la calculadora.
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarCotizacion();
            }
        });

        add(new JScrollPane(panel));
    }

    void enviarCotizacion() {
        // 1. ENTRADA
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        boolean esEmpresa = rbEmpresa.isSelected();
        boolean esResidencial = rbResidencial.isSelected();
        int posServicio = cbServicio.getSelectedIndex();
        String ciudad = txtCiudad.getText().trim();
        String mensaje = txtMensaje.getText().trim().replace("\n", " ");

        // 2. PROCESO: la bandera empieza en true
        boolean formularioValido = true;
        formularioValido = revisar(errNombre, validarNombre(nombre), formularioValido);
        formularioValido = revisar(errTelefono, validarTelefono(telefono), formularioValido);
        formularioValido = revisar(errTipo, validarTipo(esEmpresa, esResidencial), formularioValido);
        formularioValido = revisar(errServicio, validarServicio(posServicio), formularioValido);
        formularioValido = revisar(errCiudad, validarCiudad(ciudad), formularioValido);
        formularioValido = revisar(errMensaje, validarMensaje(mensaje), formularioValido);

        // 3. DECISIÓN: si algo falló, se corta aquí
        if (formularioValido == false) {
            lblEstado.setForeground(ROJO);
            lblEstado.setText("Revisa los campos marcados en rojo.");
            return;
        }

        // 4. SALIDA
        String tipo;
        if (esEmpresa) {
            tipo = "Empresa";
        } else {
            tipo = "Residencial";
        }
        String texto = armarMensaje(nombre, formatearTelefono(telefono), tipo,
                servicios[posServicio], ciudad, mensaje);
        // URLEncoder prepara tildes, espacios y saltos de línea para el enlace.
        String url = "https://wa.me/" + NUMERO_WHATSAPP + "?text="
                + URLEncoder.encode(texto, StandardCharsets.UTF_8).replace("+", "%20");

        try {
            Desktop.getDesktop().browse(new URI(url));
            lblEstado.setForeground(AZUL);
            lblEstado.setText("Listo. Se abrió WhatsApp con tu cotización.");
        } catch (Exception ex) {
            // Si no se puede abrir el navegador, mostramos el mensaje.
            JOptionPane.showMessageDialog(this, texto, "Mensaje para WhatsApp",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Muestra u oculta el error rojo y devuelve cómo queda la bandera.
    boolean revisar(JLabel etiquetaError, String error, boolean valido) {
        if (error.equals("")) {
            etiquetaError.setText(" ");
            return valido;
        } else {
            etiquetaError.setText(error);
            return false;
        }
    }

    JLabel crearError() {
        JLabel l = new JLabel(" ");
        l.setForeground(ROJO);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    void agregarCampo(JPanel panel, String texto, JComponent campo, JLabel error) {
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

    // ===================== VALIDACIONES (la lógica) =====================
    // Antes estaban en otro archivo; ahora van aquí, dentro de la misma clase.


    // Recorre el texto letra por letra con un while y deja solo los dígitos.
    public String dejarSoloNumeros(String texto) {
        String numeros = "";
        int i = 0; // Contador inicial
        while (i < texto.length()) {
            char caracter = texto.charAt(i);
            if (caracter >= '0' && caracter <= '9') {
                numeros = numeros + caracter;
            }
            i++;
        }
        return numeros;
    }

    public boolean tieneNumero(String texto) {
        if (dejarSoloNumeros(texto).equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public String validarNombre(String nombre) {
        if (nombre.equals("")) {
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
        if (escrito.equals("")) {
            return "Escribe tu número de celular.";
        }
        String digitos = dejarSoloNumeros(escrito);
        // 57 es el indicativo de Colombia: si lo escribieron, se quita.
        if (digitos.length() == 12 && digitos.substring(0, 2).equals("57")) {
            digitos = digitos.substring(2);
        }
        // Un celular colombiano tiene 10 dígitos y empieza por 3.
        if (digitos.length() == 10 && digitos.charAt(0) == '3') {
            return "";
        }
        return "Debe ser un celular de 10 dígitos, como 321 782 3068.";
    }

    // Devuelve el celular ordenado: +57 321 782 3068
    public String formatearTelefono(String escrito) {
        String digitos = dejarSoloNumeros(escrito);
        if (digitos.length() == 12) {
            digitos = digitos.substring(2);
        }
        return "+57 " + digitos.substring(0, 3) + " " + digitos.substring(3, 6) + " " + digitos.substring(6);
    }

    public String validarTipo(boolean empresa, boolean residencial) {
        if (empresa || residencial) {
            return "";
        }
        return "Indica si es para una empresa o para un hogar.";
    }

    public String validarServicio(int posicion) {
        // La posición 0 de la lista es "Selecciona un servicio".
        if (posicion == 0) {
            return "Elige el servicio que quieres cotizar.";
        }
        return "";
    }

    public String validarCiudad(String ciudad) {
        if (ciudad.equals("")) {
            return "Escribe la ciudad.";
        }
        if (ciudad.length() < 2 || tieneNumero(ciudad)) {
            return "Escribe la ciudad con letras, por ejemplo Medellín.";
        }
        return "";
    }

    public String validarMensaje(String mensaje) {
        if (mensaje.length() > 300) {
            return "Máximo 300 caracteres.";
        }
        return "";
    }

    // Une los datos con + y \n para que WhatsApp los muestre ordenados.
    public String armarMensaje(String nombre, String telefono, String tipo,
                               String servicio, String ciudad, String mensaje) {
        String texto = "Hola, quiero solicitar una cotización.\n\n";
        texto = texto + "Nombre: " + nombre + "\n";
        texto = texto + "Teléfono: " + telefono + "\n";
        texto = texto + "Tipo de cliente: " + tipo + "\n";
        texto = texto + "Servicio: " + servicio + "\n";
        texto = texto + "Ciudad: " + ciudad;
        if (!mensaje.equals("")) {
            texto = texto + "\nMensaje: " + mensaje;
        }
        return texto;
    }
}