# Starevent
Inside this folder there's all the necessary to install the "starevent" example application inside a kubernetes environment.

If you want to change some parameters feel free to clone the repository and play with this files.

You can find database configurations inside `database-config.yaml` file. Here you can change the secrets ( admin password and other user password) and the configmap that contains inital database configuration ( a sql script that will be run when the database is created the first time).

The `Database.yaml` file contains a service, the database deployment and a persistent claim that enable the database to persist data for all it's life.

The other files that start with "`application-*`" are files that create the single piece of application. This are originally created by Quarkus during build and modified only to add some configurarions ( database password read from secret and some url).

The `monitor.yaml` is the configuration of prometheus scrape_configs and grafana datasource and creation of relative services and deployments.

## How to install
To Install it, you need a running Kubernetes environment where to run the following kubectl commands:

```bash
kubectl apply -f database-config.yaml
kubectl apply -f database.yaml
kubectl apply -f application-event.yaml
kubectl apply -f application-reservation.yaml
kubectl apply -f application-frontend.yaml
kubectl apply -f monitoring.yaml
```
### How to inspect it
```bash
kubectl get pod,service
```

### How to test the application
If you want to navigate the aplication using the frontend:
```bash
kubectl port-forward service/starevent-frontend 8080
```
and open a page from the browser `http://127.0.0.1:8080`

To monitor the application metrics:
```bash
kubectl port-forward service/grafana 3000
```
and open a page from the browser `http://127.0.0.1:3000`

Or prometeus
```bash
kubectl port-forward service/prometheus 9090
```
and open a page from the browser `http://127.0.0.1:9090`

And finally if you want to call other services directly, create a port forward to it services:
```bash
kubectl port-forward service/starevent-event 8081
kubectl port-forward service/starevent-reservation 8082
```


## How to clean up
```bash
kubectl delete -f monitoring.yaml
kubectl delete -f application-frontend.yaml
kubectl delete -f application-reservation.yaml
kubectl delete -f application-event.yaml
kubectl delete -f database.yaml
kubectl delete -f database-config.yaml
```