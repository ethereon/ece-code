Cryptonite
================================

[[ Build ]]

Use ant to build the project and generate the jar file.

[[ Run ]]

Use the shell script to run Cryptonite, like so :

	Encryption (Alice sends Bob a file) :
	./cryptonite -e testKeys/Bob.pub testKeys/Alice.pem Alchemy.txt Secret.bin
	
	Decryption (Bob decrypts Alice's file and verifies her signature) :
	./cryptonite -d testKeys/Bob.pem testKeys/Alice.pub Secret.bin Alchemy.txt
	
	
[[ Key files ]]

Cryptonite currently supports X.509 PEM ( Privacy Enhanced Mail ) Base64 encoded files, such as those generated by OpenSSL.
See the testKeys directory for samples.