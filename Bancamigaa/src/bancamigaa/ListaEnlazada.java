package bancamigaa;

public class ListaEnlazada<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;

    public ListaEnlazada() {
        primero = null;
        ultimo = null;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
    }

    public Nodo<T> obtenerPrimero() {
        return primero;
    }

    static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
}