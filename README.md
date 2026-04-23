# Sistema Cliente-Servidor en Java

## 1. Introducción
Este proyecto implementa un sistema cliente-servidor utilizando sockets en Java. Permite la conexión simultánea de múltiples clientes mediante el uso de hilos.

---

## 2. Funcionamiento general
El servidor escucha conexiones en el puerto 5000. Cada cliente que se conecta es atendido por un hilo independiente (`ClienteHandler`), lo que permite que múltiples clientes interactúen al mismo tiempo.

---

## 3. Comunicación
Los clientes pueden enviar mensajes utilizando distintos comandos:

- `ALL`: envía un mensaje a todos los clientes conectados  
- `PRIVADO`: envía un mensaje a un usuario específico  
- `MULTI`: envía un mensaje a varios usuarios al mismo tiempo  

---

## 4. Funcionalidades

- `LISTA`: muestra los usuarios conectados  
- `CALC`: resuelve expresiones matemáticas  
- `DADO`: genera un número aleatorio del 1 al 6  
- `FECHA`: muestra la fecha y hora actual  
- `MAYUS`: convierte texto a mayúsculas  
- `MINUS`: convierte texto a minúsculas  
- `REV`: invierte un texto  
- `CONTAR`: cuenta la cantidad de caracteres  
- `DOBLE`: repite el texto dos veces  

---

## 5. Manejo de errores
El sistema controla distintos tipos de errores, como:

- Usuarios inexistentes en mensajes privados o múltiples  
- Formato incorrecto de comandos  
- Errores en expresiones matemáticas  

---

## 6. Concurrencia
Se utilizan hilos (`Thread`) para permitir múltiples conexiones simultáneas sin bloquear el servidor. Cada cliente se ejecuta en paralelo mediante un `ClienteHandler`.

---
