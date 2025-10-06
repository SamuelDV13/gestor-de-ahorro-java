package sdv.gestorgastos.modelo;

public class Plazo {

    private TipoPlazo tipo;
    private int periodos;

    public Plazo() {

    }

    public Plazo(TipoPlazo tipo, int periodos) {
        this.tipo = tipo;
        this.periodos = periodos;
    }

    public TipoPlazo getTipo() {
        return tipo;
    }

    public int getPeriodos() {
        return periodos;
    }

    public void setTipo(TipoPlazo tipo) {
        this.tipo = tipo;
    }

    public void setPeriodos(int periodos) {
        this.periodos = periodos;
    }
}
