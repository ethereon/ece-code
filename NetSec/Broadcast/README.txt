Broadcast Server + Client
================================

[[ Build ]]

Use ant to build project and generate jar file.

[[ Run ]]

Use the shell scripts to execute the server/client, OR like this :

	(In the dir containing Broadcast.jar)

	java -classpath Broadcast.jar broadcast.server.BroadcastServer <port> 
	java -classpath Broadcast.jar broadcast.client.BroadcastClient <addr> <port>


