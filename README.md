# starevent-quarkus
Starevent-quarkus is a microservices event booking application developed using Quarkus.
This project is a simple use case of a web application for the management of events related to concerts.
The code was made to show how to create microservices architecture using Quarkus as Cloud Native Stack. The code was discussed inside the following post:
- https://marcelloraffaele.github.io/from-microservices-to-kubernetes-with-quarkus-1/
- https://marcelloraffaele.github.io/from-microservices-to-kubernetes-with-quarkus-2/


Docker Images are already pushed on Docker hub, you can use it and jump "image build".
If you want to test the application, you can clone the repository and work directly on folders:
- docker: a set docker-compose descriptor and other configuration file to run the application into Docker
- kubernetes: a set of kubernetes descriptor that you can use to run the application.



## Deploy on Docker

You can run the application with docker compose:
```
cd docker
docker-compose -f docker-compose.yaml up -d
```
Now you can test the application! Open a browser on http://localhost:8080 or invoke rest services by CURL.

You can see the prometheus page visiting [http://127.0.0.1:9090](http://127.0.0.1:9090).

You can see the grafana page visiting [http://127.0.0.1:3000](http://127.0.0.1:3000) and set default user and password (admin/admin).

When you finish testing you can clean everything:
```
docker-compose -f docker-compose.yaml down
```


## Deploy on Kubernets
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


### How to clean up
```bash
kubectl delete -f monitoring.yaml
kubectl delete -f application-frontend.yaml
kubectl delete -f application-reservation.yaml
kubectl delete -f application-event.yaml
kubectl delete -f database.yaml
kubectl delete -f database-config.yaml
```



## Testing the application 

Open a browser on http://localhost:8080

or test directly the rest services:
```
## event endpoint
### /
curl -X GET http://localhost:8081/api/events

### /random
curl -X GET http://localhost:8081/api/events/random

### /{id}
curl -X GET http://localhost:8081/api/events/7

### create resource (POST)
curl -X POST "http://localhost:8081/api/events" -H  "accept: application/json" -H  "Content-Type: application/json" -d '{
  "address": "via Roma 1",
  "artist": "Carmen Consoli",
  "availability": 100,
  "description": "Carmen Consoli torna con un nuovo tour nel 2021!",
  "price": 100,
  "startDate": "01/07/2021 20:00:00",
  "title": "title 1",
  "where": "palasport 1"
}' -v

```
For many other test open the client.rest file.


## Image build
If you want to rebuild the images:

```
cd starevent-event
mvn package -Dquarkus.container-image.build=true

cd ..
cd starevent-reservation
mvn package -Dquarkus.container-image.build=true

cd ..
cd starevent-frontend
mvn package -Dquarkus.container-image.build=true
```
