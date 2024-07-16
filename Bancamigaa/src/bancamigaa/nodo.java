package bancamigaa;


public class nodo {
    String nombre;
    String apellido;
    String cedula;
    int edad;
    int numSolicitudes;
    Solicitud solicitud;  // lista enlazada de solicitudes del cliente
    nodo siguiente;

    public nodo(String nombre, String apellido, String cedula, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.edad = edad;
        this.numSolicitudes = 0;
        this.solicitud = null;
        this.siguiente = null;
    }

    public void agregarSolicitud(String descripcion, double tiempo) { 
        if (this.numSolicitudes < 5) {
            Solicitud nuevaSolicitud = new Solicitud(descripcion, tiempo);
            if (this.solicitud == null) {
                this.solicitud = nuevaSolicitud;
            } else {
                Solicitud temp = this.solicitud;
                while (temp.siguiente != null) {
                    temp = temp.siguiente;
                }
                temp.siguiente = nuevaSolicitud;
            }
            this.numSolicitudes++;
        } else {
            System.out.println("El cliente " + this.nombre + " " + this.apellido + " ya ha alcanzado el máximo de solicitudes permitidas (5).");
        }
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + cedula + " - Edad: " + edad;
    }
}

class Solicitud {
    String descripcion;
    double tiempo;
    Solicitud siguiente;

    public Solicitud(String descripcion, double tiempo) {
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.siguiente = null;
    }
}