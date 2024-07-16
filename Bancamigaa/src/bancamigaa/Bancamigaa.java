package bancamigaa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bancamigaa {
    private Cola colaClientes; //cola para los clientes en espera
    private ListaEnlazada<nodo> clientesAtendidos; //lista de los clientes atendidos 
    private ListaEnlazada<Transaccion> transaccionesDelDia; //lista de las transacciones del dia 

    public Bancamigaa() {
        this.colaClientes = new Cola(); // se inicializa la cola de los clientes
        this.clientesAtendidos = new ListaEnlazada<>(); // se inicializa la lista de clientes atendidos 
        this.transaccionesDelDia = new ListaEnlazada<>(); // se inicializa la lista de transacciones del dia 
    }

    public void cargarClientesPendientes(String archivo) { // metodo para cargar los clientes pendientes desde un archivo 
        System.out.print("Atendiendo Clientes Pendientes para luego atender a los ");
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                String apellido = datos[1];
                String cedula = datos[2];
                int edad = Integer.parseInt(datos[3]);
                nodo cliente = new nodo(nombre, apellido, cedula, edad); // agraga las solicitudes del cliente 
                for (int i = 4; i < datos.length; i += 2) {
                    cliente.agregarSolicitud(datos[i], Double.parseDouble(datos[i + 1]));
                }
                colaClientes.encolar(cliente); // encola el cliente 
            }
        } catch (IOException e) {
            System.out.println("Error al cargar clientes pendientes: " + e.getMessage());
        }
    }

    public void cargarClientes(String archivo) { // metodo para cargar los clientes del dia desde un archivo 
        System.out.println("Clientes del dia");
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                String apellido = datos[1];
                String cedula = datos[2];
                int edad = Integer.parseInt(datos[3]);
                nodo cliente = new nodo(nombre, apellido, cedula, edad); // agrega las solicitudes del cliente 
                for (int i = 4; i < datos.length; i += 2) {
                    cliente.agregarSolicitud(datos[i], Double.parseDouble(datos[i + 1]));
                }
                colaClientes.encolar(cliente); // posteriormente se encola al cliente 
            }
        } catch (IOException e) {
            System.out.println("Error al cargar clientes del día: " + e.getMessage());
        }
    }

    public void atenderClientes() { // metodo para atender los clientes que estan en la cola 
        while (!colaClientes.estaVacia()) {
            nodo cliente = colaClientes.desencolarPrioridad(); // desencola al cliente con prioridad 
            if (cliente != null) {
                System.out.println("Atendiendo cliente: " + cliente.nombre + " " + cliente.apellido);
                procesarSolicitudes(cliente); // se procesan las solicitudes del cliente 
                clientesAtendidos.agregar(cliente); // se agrega al cliente a la lista de atendidos 
            }
        }
        System.out.println("Atención de clientes finalizada.");
    }

    private void procesarSolicitudes(nodo cliente) { // metodo para procesar las solicitudes del cliente 
    Solicitud solicitudActual = cliente.solicitud;
    while (solicitudActual != null) {
        double tiempoProceso = solicitudActual.tiempo;
        String tipoTransaccion = solicitudActual.descripcion;

        switch (tipoTransaccion) {
            case "retiro":
                tiempoProceso = 4.0;
                break;
            case "deposito":
                tiempoProceso = 3.0;
                break;
            case "consulta":
                tiempoProceso = 1.5;
                break;
            case "actualizacion":
                tiempoProceso = 5.0;
                break;
            case "pago":
                tiempoProceso = 2.0;
                break;
            default:
                System.out.println("Tipo de transacción no reconocido: " + tipoTransaccion);
                // esto lanza una excepcion en caso de que el tipo de transaccion no exista y se cierra el programa
                throw new IllegalArgumentException("Tipo de transacción no reconocido: " + tipoTransaccion);
                
        }

        // se procesa la solicitud en caso de que sea valida 
        if (!tipoTransaccion.equals("invalido")) {
            System.out.println("Procesando solicitud: " + solicitudActual.descripcion);
            Transaccion transaccion = new Transaccion(solicitudActual.descripcion, tiempoProceso, cliente.nombre);
            transaccionesDelDia.agregar(transaccion); // se agrega la transaccion a la lista del dia 
            System.out.println("Transacción realizada: " + transaccion.toString());
        }

        solicitudActual = solicitudActual.siguiente;
    }
}

    public void cerrarTaquilla() { // metodo para cerrar la taquilla y posteriormente guardarlo en el archivo taquilla.log
        System.out.println("Cerrando taquilla...");
        String logFileName = "Taquilla.log.txt";
        File logFile = new File(logFileName);
        if (logFile.exists()) { // se renombra el archivo taquilla.log si ya existe uno del dia anterior 
            String newFileName = new SimpleDateFormat("'taquilla' dd-MM-yyyy'.log'").format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
            logFile.renameTo(new File(newFileName));
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFileName))) { // se escriben las transacciones del dia en el archivo 
            ListaEnlazada.Nodo<Transaccion> current = transaccionesDelDia.obtenerPrimero();
            while (current != null) {
                bw.write(current.dato.toString());
                bw.newLine();
                current = current.siguiente;
            }
        } catch (IOException e) {
            System.out.println("Error al escribir archivo de log: " + e.getMessage());
        }

        if (!colaClientes.estaVacia()) { // si aun quedan clientes en la cola se guardan en otro archivo llamado clientes_pendientes.in
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("clientes_pendientes.in.txt"))) {
                while (!colaClientes.estaVacia()) {
                    nodo cliente = colaClientes.desencolar();
                    bw.write(cliente.nombre + "," + cliente.apellido + "," + cliente.cedula + "," + cliente.edad);
                    Solicitud solicitud = cliente.solicitud;
                    while (solicitud != null) {
                        bw.write("," + solicitud.descripcion + "," + solicitud.tiempo);
                        solicitud = solicitud.siguiente;
                    }
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error al escribir archivo de clientes pendientes: " + e.getMessage());
            }
        }
        System.out.println("Taquilla cerrada.\n");
    }

    public static void main(String[] args) { // metodo main o principal para ejecutar el programa 
        System.out.println("-----Bienvenido a Bancamiga-----\n");

        Bancamigaa banco = new Bancamigaa();
        // Cargar clientes pendientes primero en caso de que existan 
        File clientesPendientesFile = new File("clientes_pendientes.in.txt");
        if (clientesPendientesFile.exists()) {
            banco.cargarClientesPendientes("clientes_pendientes.in.txt");
        }

        // cargar clientes del día
        banco.cargarClientes("Clientes.in.txt");

        // simulacion de atención al cliente
        banco.atenderClientes();

        // cierre de taquilla y registro de transacciones
        banco.cerrarTaquilla();

        System.out.println("Gracias por preferir Bancamiga.");
    }
}
