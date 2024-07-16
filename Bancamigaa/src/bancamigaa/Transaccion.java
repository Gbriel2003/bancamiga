package bancamigaa;

public class Transaccion {
    String tipo;
    double tiempo;
    String cedulaCliente;

    Transaccion(String tipo, double tiempo, String cedulaCliente) {
        this.tipo = tipo;
        this.tiempo = tiempo;
        this.cedulaCliente = cedulaCliente;
    }

    @Override
    public String toString() {
        return tipo + " - Tiempo: " + tiempo + " - Cliente: " + cedulaCliente+"\n";
    }
}