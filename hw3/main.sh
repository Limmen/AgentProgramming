#!/bin/bash

mvn compile exec:java -Dexec.mainClass=jade.Boot -Dexec.args='-host localhost -port 1099 -local-port 1099 -jade_domain_df_autocleanup true'