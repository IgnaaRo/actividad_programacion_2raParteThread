package com.mycompany.actpro3;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class ClienteHandler extends Thread {

    Socket socket;
    BufferedReader entrada;
    PrintWriter salida;
    String nombre;

    static int contador = 1;

    public ClienteHandler(Socket socket) throws IOException {
        this.socket = socket;

        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);

        nombre = "Cliente" + contador++;
        salida.println("Sos " + nombre);
        salida.println("Comandos:");
        salida.println("ALL msg | PRIVADO nombre msg | MULTI nombres msg | LISTA | CALC | DADO | FECHA | MAYUS | MINUS | REV | CONTAR | DOBLE | SALIR");
    }

    public void enviar(String msg) {
        salida.println(msg);
    }

    @Override
    public void run() {
        String msg;

        try {
            while ((msg = entrada.readLine()) != null) {

                System.out.println(nombre + ": " + msg);

                if (msg.equalsIgnoreCase("SALIR")) {
                    break;
                }

                
                else if (msg.startsWith("ALL")) {
                    String texto = msg.substring(4);
                    Servidor.enviarATodos(nombre + ": " + texto, this);
                }

                else if (msg.startsWith("PRIVADO")) {
                    String[] partes = msg.split(" ", 3);

                    if (partes.length == 3) {
                        Servidor.enviarPrivado(
                                partes[1],
                                "(Privado) " + nombre + ": " + partes[2],
                                this
                        );
                    } else {
                        salida.println("Formato incorrecto");
                    }
                }

                
                else if (msg.equalsIgnoreCase("LISTA")) {
                    Servidor.listarClientes(this);
                }

                
                else if (msg.startsWith("CALC")) {
                    try {
                        String expr = msg.substring(5);
                        double resultado = evaluar(expr);
                        salida.println("Resultado: " + resultado);
                    } catch (Exception e) {
                        salida.println("Error en la expresión");
                    }
                }

                else if (msg.equalsIgnoreCase("DADO")) {
                    int dado = (int)(Math.random() * 6) + 1;
                    salida.println("Dado: " + dado);
                }

                else if (msg.equalsIgnoreCase("FECHA")) {
                    salida.println("Fecha: " + LocalDateTime.now().withNano(0));
                }

                else if (msg.startsWith("MAYUS")) {
                    String texto = msg.substring(6).trim();
                    salida.println(texto.toUpperCase());
                }

                else if (msg.startsWith("MINUS")) {
                    String texto = msg.substring(6).trim();
                    salida.println(texto.toLowerCase());
                }

                else if (msg.startsWith("REV")) {
                    String texto = msg.substring(4).trim();
                    String invertido = new StringBuilder(texto).reverse().toString();
                    salida.println(invertido);
                }

                else if (msg.startsWith("CONTAR")) {
                    String texto = msg.substring(7).trim();
                    salida.println("Largo: " + texto.length());
                }

                else if (msg.startsWith("DOBLE")) {
                    String texto = msg.substring(6).trim();
                    salida.println(texto + " " + texto);
                }
                
                else if (msg.startsWith("MULTI")) {
                    try {
                        String[] partes = msg.split(" ", 3);

                        if (partes.length == 3) {
                            String[] usuarios = partes[1].split(",");
                            String mensaje = partes[2];

                            Servidor.enviarMultiple(usuarios, mensaje, this);
                        } else {
                            salida.println("Formato incorrecto. Uso: MULTI Cliente1,Cliente2 mensaje");
                        }

                    } catch (Exception e) {
                        salida.println("Error en MULTI");
                    }
                }
                else {
                    salida.println("Comando inválido");
                }
            }

            socket.close();
            Servidor.eliminar(this);
            System.out.println(nombre + " desconectado");

        } catch (IOException e) {
        System.out.println(nombre + " se desconectó inesperadamente");
    } finally {
        try {
            socket.close();
        } catch (IOException e) {}

        Servidor.eliminar(this);
        System.out.println(nombre + " eliminado del servidor");
    }
    }
    
    private double evaluar(String expr) {
    return new Object() {
        int pos = -1, ch;

        void nextChar() {
            ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < expr.length()) throw new RuntimeException("Error");
            return x;
        }

       
        double parseExpression() {
            double x = parseTerm();
            while (true) {
                if (eat('+')) x += parseTerm();
                else if (eat('-')) x -= parseTerm();
                else return x;
            }
        }

        
        double parseTerm() {
            double x = parseFactor();
            while (true) {
                if (eat('*')) x *= parseFactor();
                else if (eat('/')) x /= parseFactor();
                else return x;
            }
        }

        
        double parseFactor() {
            if (eat('+')) return parseFactor();
            if (eat('-')) return -parseFactor();

            double x;
            int startPos = this.pos;

            if (eat('(')) {
                x = parseExpression();
                eat(')');
            } else {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(expr.substring(startPos, this.pos));
            }

            return x;
        }
    }.parse();
}
}