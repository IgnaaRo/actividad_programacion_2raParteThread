package com.mycompany.actpro3;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 5000);

            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter salida = new PrintWriter(
                    socket.getOutputStream(), true
            );

            Scanner scanner = new Scanner(System.in);

            System.out.println("Conectado al servidor");

            new Thread(() -> {
                try {
                    String res;
                    while ((res = entrada.readLine()) != null) {
                        System.out.println(res);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String mensaje = scanner.nextLine();
                salida.println(mensaje);

                if (mensaje.equalsIgnoreCase("SALIR")) {
                    break;
                }
            }

            socket.close();
            System.out.println("Cliente desconectado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}