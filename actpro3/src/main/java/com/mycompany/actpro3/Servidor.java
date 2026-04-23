package com.mycompany.actpro3;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

    static List<ClienteHandler> clientes = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("Servidor iniciado en puerto 5000...");

        while (true) {
            Socket socket = servidor.accept();
            System.out.println("Cliente conectado");

            ClienteHandler cliente = new ClienteHandler(socket);
            clientes.add(cliente);
            cliente.start();
        }
    }

    public static void enviarATodos(String mensaje, ClienteHandler emisor) {
        for (ClienteHandler c : clientes) {
            if (c != emisor) {
                c.enviar(mensaje);
            }
        }
    }

    public static void enviarPrivado(String nombre, String mensaje, ClienteHandler emisor) {
    boolean encontrado = false;

    for (ClienteHandler c : clientes) {
        if (c.nombre.equals(nombre)) {
            c.enviar(mensaje);
            encontrado = true;
        }
    }

    if (!encontrado) {
        emisor.enviar("Usuario no encontrado ");
    }
    }

    public static void listarClientes(ClienteHandler cliente) {
        String lista = "Clientes conectados: ";
        for (ClienteHandler c : clientes) {
            lista += c.nombre + " ";
        }
        cliente.enviar(lista);
    }
    
    public static void enviarMultiple(String[] nombres, String mensaje, ClienteHandler emisor) {
    boolean algunoEnviado = false;

    for (String nombre : nombres) {
        boolean encontrado = false;

        for (ClienteHandler c : clientes) {
            if (c.nombre.equals(nombre.trim())) {
                c.enviar("(Multi) " + emisor.nombre + ": " + mensaje);
                encontrado = true;
                algunoEnviado = true;
            }
        }

        if (!encontrado) {
            emisor.enviar("Usuario no encontrado: " + nombre);
        }
    }

    if (!algunoEnviado) {
        emisor.enviar("No se pudo enviar a ningún usuario");
    }
    }
    public static void eliminar(ClienteHandler cliente) {
        clientes.remove(cliente);
    }
}