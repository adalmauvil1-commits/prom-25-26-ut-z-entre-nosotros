# Txatrea

Este es un proyecto de ejemplo para mostrar cómo crear un servicio de chat sencillo.

## v1
 
La primera versión tiene la parte de servidor y de cliente integrada en una misma clase.
Al ejecutarse la clase TxatServerClient se solicita el rol con el que se desea iniciar.

### Servidor

El servidor escucha en un puerto y acepta conexiones de clientes que gestiona en hilos de tipo ClientHandler.
En clientWriters se guardan los PrintWriter de cada cliente conectado para poder enviar mensajes a todos los clientes.
En cada hilo de gestión de cliente en el servidor se lee el mensaje enviado por el cliente y se retransmite a todos los clientes conectados utilizando clientWriters.

### Cliente

Cada cliente puede enviar mensajes que se retransmiten a todos los demás clientes conectados.
En cada cliente se crean tres flujos: uno para leer desde el servidor (in), otro para escribir al servidor(out) y otro para leer desde la consola (userInput).
Inicialmente se recoge el nombre del cliente en una variable local llamada username.
A continuación, se inicia un hilo que lee los mensajes del servidor y los muestra por consola.
Finalmente, en el hilo principal se lee la entrada del usuario y se envía al servidor en un bucle "infinito".

## v2

En esta versión se ha separado la parte de servidor y cliente en dos clases diferentes: TxatServer y TxatClient.
La clase TxatClientHandler también se ha separado en una clase independiente para gestionar cada cliente conectado al servidor.
La clase TxatServer y TxatClientHandler comparten la lista de PrintWriter de los clientes conectados para poder enviar mensajes a todos los clientes.

## v3

En esta versión se utiliza un mapa de clientes conectados en el servidor, donde la clave es el nombre del cliente y el valor es su PrintWriter. 
Esto puede permitir enviar mensajes a clientes específicos en lugar de a todos los clientes conectados (sin implementar todavía).
Cambia la clase TxatClient y TxatClientHandler para que el cliente envíe su nombre al servidor al conectarse, y el servidor lo almacene en el mapa de clientes conectados.
Si el nombre del cliente ya existe en el mapa, el servidor envia un mensaje de rechazo al cliente para que este pruebe con otro nombre.

## v4

Si un cliente escribe la palabra "BYE" saldrá del cliente y se cerrará la conexión con el servidor. El servidor eliminará al cliente del mapa de clientes conectados y notificará a los demás clientes que el cliente se ha desconectado.

## v5

Si un cliente escribe la palabra "WHO" el servidor enviará al cliente una lista de los nombres de los clientes conectados actualmente. El cliente muestra esta lista por consola.

## v6

Se implementa una interfaz gráfica de usuario (GUI) para el cliente utilizando JavaFX. 

## v7

Se notifica al cliente cuando un cliente se conecta o desconecta del servidor.
