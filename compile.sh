#!/bin/bash

echo "Updating..."
git pull
echo "Done!"
echo "Compiling..."
mvn compile
echo "Done! Type ./start.sh to start the server."
