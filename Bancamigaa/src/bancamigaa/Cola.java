package bancamigaa;

public class Cola {
    private Nodo front;  // nodo frontal de la cola
    private Nodo tail;   // nodo final de la cola
    private int size;    // tamaño de la cola

    // constructor
    public Cola() {
        this.front = null;
        this.tail = null;
        this.size = 0;
    }

    // metodo para encolar un cliente en la cola
    public void encolar(nodo cliente) {
        Nodo nuevoNodo = new Nodo(cliente);
        if (estaVacia()) {
            front = nuevoNodo;
        } else {
            tail.siguiente = nuevoNodo;
        }
        tail = nuevoNodo;
        size++;
    }

    // metodo para desencolar un cliente de la cola
    public nodo desencolar() {
        if (estaVacia()) {
            return null;
        }
        nodo clienteDesencolado = front.cliente;
        front = front.siguiente;
        if (front == null) {
            tail = null;
        }
        size--;
        return clienteDesencolado;
    }

    // metodo para obtener el tamaño de la cola
    public int obtenerTamanio() {
        return size;
    }

    // metodo para verificar si la cola está vacía
    public boolean estaVacia() {
        return size == 0;
    }

    // metodo para desencolar con prioridad según la edad y cantidad de preferencias
    public nodo desencolarPrioridad() {
        nodo clientePrioritario = null;
        Nodo anterior = null;
        Nodo actual = front;

        int edadMinima = 60;
        int contadorPreferencia = 4;

        while (actual != null) {
            if (actual.cliente.edad < edadMinima || contadorPreferencia > 0) {
                clientePrioritario = actual.cliente;
                if (anterior == null) {
                    front = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                if (actual == tail) {
                    tail = anterior;
                }
                size--;
                if (actual.cliente.edad >= edadMinima) {
                    contadorPreferencia--;
                }
                break;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        return clientePrioritario;
    }

    // clase interna nodo para almacenar los elementos de la cola
    private static class Nodo {
        nodo cliente;  // cliente almacenado en el nodo
        Nodo siguiente;  // referencia al siguiente nodo en la cola

        // constructor del nodo
        Nodo(nodo cliente) {
            this.cliente = cliente;
            this.siguiente = null;
        }
    }
}