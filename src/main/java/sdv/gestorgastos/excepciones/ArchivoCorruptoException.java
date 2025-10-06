package sdv.gestorgastos.excepciones;

public class ArchivoCorruptoException extends RuntimeException {
    public ArchivoCorruptoException(String message) {
        super(message);
    }
}
