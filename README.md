# GANGES - Gewährleistung von Anonymitäts-Garantien in Enterprise-Streaminganwendungen
A project for the module (Advanced) Distributed Systems Prototyping at the Technical University of Berlin. 

The interceptor interface is implemented from two different viewpoints. 

1. Producer_Interceptor:
The data is changed through an interceptor before the data reaches the Kafka Cluster.

2. Consumer_Interceptor: 
The data is the same; However, the consumer has a different output than the data of the cluster.
