README for mltrading
==========================

This projet is a my projet for ces data scientist certificate from telecom paris

It is a java stack with angular form frontend
it use jhipster for generate boilderplate
Jhipster is plugged with mongodb for security and text data
Influxdb is used to store metrics data from google/finance and others providers
Spark is use for machine learning part


Spark Mllib use hundred of Random forest and Gradiant boost trees  models to predict stock value for 
period of 20 and 40 days.
Features are selected by genetics algorithms 




how install:

install mongodb and run it 
install influxdb and run it , configure manually ip and port in InfluxDao => to change
install spark and run master and slave , configure manually ip and port in CacheMLStock




influxdb mac os
                                                                                                       
/usr/local/var/influxdb/data
/usr/local/opt/influx
