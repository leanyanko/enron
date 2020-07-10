#!/bin/bash
echo "Hello World"

mvn clean compile assembly:single

java -cp target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App <database credentials>