#!/bin/bash

#mvn compile exec:java -Dexec.mainClass=jade.Boot -Dexec.args='-gui'

mvn compile exec:java -Dexec.mainClass=jade.Boot -Dexec.args='-host localhost -port 1099 -local-port 1099'