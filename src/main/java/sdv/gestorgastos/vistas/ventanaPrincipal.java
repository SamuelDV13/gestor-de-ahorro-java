package sdv.gestorgastos.vistas;

import com.github.lgooddatepicker.components.DatePicker;
import sdv.gestorgastos.controladores.ObjetivoControlador;
import sdv.gestorgastos.modelo.Ahorro;
import sdv.gestorgastos.modelo.Objetivo;
import sdv.gestorgastos.modelo.TipoPlazo;
import sdv.gestorgastos.util.GestorArchivos;
import sdv.gestorgastos.util.Mensajes;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ventanaPrincipal extends JFrame {
    // ... (todos tus componentes de la UI JPanels, JButtons, etc. quedan igual) ...
    private JPanel panel_objetivo;
    private JPanel panel_principal;
    private JPanel panel_ahorro;
    private JPanel panel_progreso;
    private JPanel panel_historial;
    private JPanel panel_proximo;
    private JTextField txt_ahorroObj;
    //private JTextField txt_fechaObj;
    private JButton btn_abandonarObj;
    private JButton btn_guardar;
    private JButton btn_ahorrar;
    private JList<String> list_ahorros;
    private JLabel lbl_proximaFecha;
    private JLabel lbl_cantidad;
    private JLabel lbl_ahorroActual;
    private JLabel lbl_fechaObj;
    private JLabel lbl_periodos;
    private JLabel lbl_ahorroObj;
    private JComboBox<TipoPlazo> cb_periodos;
    private JLabel lbl_cantidadAhorroT;
    private JScrollPane scroll_ahorro;
    private DatePicker datePicker;
    private JButton btn_analizar;


    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();

    private final Objetivo objetivo;
    private final ObjetivoControlador controlador;

    public ventanaPrincipal(Objetivo objetivoInicial) {
        this.objetivo = objetivoInicial;
        this.controlador = new ObjetivoControlador(this.objetivo, this);

        initComponents();
        list_ahorros.setModel(modeloLista);
        llenarComboPlazos();
        initEvents();

        actualizarVista();
    }

    private void initComponents() {
        getContentPane().add(panel_principal);
        setTitle("GestorGastos");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        GestorArchivos gestorArchivos = new GestorArchivos();

        Objetivo objetivoCargado;
        if(gestorArchivos.cargarArchivos()){
            objetivoCargado = gestorArchivos.getObjetivo();
        } else{
            objetivoCargado = new Objetivo();
        }


        final Objetivo modeloFinal = objetivoCargado;
        java.awt.EventQueue.invokeLater(() -> {
            ventanaPrincipal ventana = new ventanaPrincipal(modeloFinal);
            ventana.setVisible(true);
        });
    }



    public void actualizarVista() {
        // Primero, determina el estado actual de la aplicación.
        boolean hayObjetivoActivo = (objetivo != null && objetivo.getCantidadObjetivo() != null);

        // PARTE A: Controla el estado de los componentes (habilitado/deshabilitado)
        if (hayObjetivoActivo) {
            // Estado: Hay un objetivo en progreso.
            txt_ahorroObj.setEditable(false);
            datePicker.setEnabled(false);
            cb_periodos.setEnabled(false);
            btn_guardar.setEnabled(false);

            btn_abandonarObj.setEnabled(true);
            btn_ahorrar.setEnabled(true);
            btn_analizar.setEnabled(true);
        } else {
            // Estado: No hay objetivo, listo para crear uno nuevo.
            txt_ahorroObj.setEditable(true);
            datePicker.setEnabled(true);
            cb_periodos.setEnabled(true);
            btn_guardar.setEnabled(true);

            btn_abandonarObj.setEnabled(false);
            btn_ahorrar.setEnabled(false);
            btn_analizar.setEnabled(false);
        }

        // PARTE B: Llena los componentes con los datos del modelo
        if (hayObjetivoActivo) {
            txt_ahorroObj.setText(objetivo.getCantidadObjetivo().toString());
            datePicker.setDate(objetivo.getFechaObjetivo());
            cb_periodos.setSelectedItem(objetivo.getPlazo().getTipo()); // Forma más segura de seleccionar

            lbl_cantidadAhorroT.setText("$" + objetivo.verProgreso());

            if (!objetivo.obtenerProximos().isEmpty()) {
                lbl_proximaFecha.setText("Proxima Fecha: " + objetivo.obtenerProximos().getFirst().getFechaProgramada().toString());
                lbl_cantidad.setText("Cantidad: $" + objetivo.obtenerProximos().getFirst().getCantidad());
            } else {
                lbl_proximaFecha.setText("Proxima Fecha:");
                lbl_cantidad.setText("Cantidad:");
            }
        } else {
            // Limpia los campos si no hay objetivo
            txt_ahorroObj.setText("");
            datePicker.clear();
            cb_periodos.setSelectedIndex(0);
            lbl_cantidadAhorroT.setText("Crea un objetivo para empezar");
            lbl_proximaFecha.setText("Proxima Fecha:");
            lbl_cantidad.setText("Cantidad:");
        }

        // PARTE C: Actualiza la lista de ahorros
        llenarModeloLista();

        // PARTE D: Lógica de colores (si el objetivo está cumplido)
        // (Suponiendo que tienes un método en Objetivo para saber si se cumplió)
        if (hayObjetivoActivo && objetivo.verificarObjetivo()) {
            lbl_ahorroActual.setForeground(new Color(121, 220, 121));
            lbl_cantidadAhorroT.setForeground(new Color(121, 220, 121));
        } else {
            lbl_ahorroActual.setForeground(new Color(255, 253, 247));
            lbl_cantidadAhorroT.setForeground(new Color(255, 253, 247));
        }
    }


    private void llenarModeloLista(){
        modeloLista.clear();
        // Solo intenta llenar la lista si hay un objetivo
        if (objetivo != null && objetivo.obtenerProximos() != null) {
            List<Ahorro> listaProximos = objetivo.obtenerProximos();
            int i = 1;
            for(Ahorro ahorro : listaProximos){
                String texto = i++ + ".- " + "Fecha: " + ahorro.getFechaProgramada() + " --- " + "Cantidad: $" + ahorro.getCantidad();
                modeloLista.addElement(texto);
            }
        }
    }


    private void llenarComboPlazos(){
        cb_periodos.addItem(TipoPlazo.AUTOMATICO);
        cb_periodos.addItem(TipoPlazo.DIARIO);
        cb_periodos.addItem(TipoPlazo.SEMANAL);
        cb_periodos.addItem(TipoPlazo.QUINCENAL);
        cb_periodos.addItem(TipoPlazo.MENSUAL);
    }


    private void initEvents() {
        btn_abandonarObj.addActionListener(e -> {

            if(Mensajes.confirmarAccion("¿Deseas eliminar este objetivo?")){
                controlador.eliminarObjetivo();
            }
        });

        btn_guardar.addActionListener(e -> {
            String monto = txt_ahorroObj.getText();
            String fecha = datePicker.getDate().toString();
            TipoPlazo plazo = (TipoPlazo) cb_periodos.getSelectedItem();

            controlador.crearPlanAhorro(monto, fecha, plazo);
        });


        btn_ahorrar.addActionListener(e -> {
            controlador.MarcarAhorro();
        });
    }
}
