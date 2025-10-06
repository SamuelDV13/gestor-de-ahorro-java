package sdv.gestorgastos.util;

import javax.swing.JOptionPane;

public final class Mensajes {

    private Mensajes(){

    }

    public static void mostrarError(String mensaje){
        JOptionPane.showMessageDialog(null,mensaje,"ERROR",JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarWarning(String mensaje){
        JOptionPane.showMessageDialog(null,mensaje,"WARNING",JOptionPane.WARNING_MESSAGE);
    }

    public static void mostrarInfo(String mensaje){
        JOptionPane.showMessageDialog(null,mensaje,"INFO",JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmarAccion(String pregunta){
        int desicion = JOptionPane.showConfirmDialog(null, pregunta, "Confirmar accion", JOptionPane.YES_NO_OPTION,  JOptionPane.QUESTION_MESSAGE);
        return desicion == JOptionPane.YES_OPTION;
    }

}
