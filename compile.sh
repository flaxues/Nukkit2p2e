rm nukkit-1.0-SNAPSHOT.jar
mvn package
cp target/nukkit-1.0-SNAPSHOT.jar ./nukkit-1.0-SNAPSHOT.jar
echo Done! Type ./start.sh to start the server.
