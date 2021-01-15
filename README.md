# starevent-quarkus
A microservices event booking application developed using Quarkus

Images are already pushed on Docker hub, you can use it and jump "image build".

## Deploy on Docker

You can try it directly from Play with docker from: 

[![Try in PWD](https://raw.githubusercontent.com/play-with-docker/stacks/master/assets/images/button.png)](https://labs.play-with-docker.com/?stack=https://github.com/marcelloraffaele/starevent-quarkus/blob/main/docker/docker-compose.yaml)

Otherwise, you can try it on your machine. You can run the application with docker compose:
```
cd docker
docker-compose -f docker-compose.yaml up -d
```
Now you can test the application! Open a browser on http://localhost:8080 or invoke rest services by CURL.
When you finish testing you can clean everything:
```
docker-compose -f docker-compose.yaml down
```


## Deploy on Kubernets

Let's start the application with docker compose:
```
cd kubernetes
kubectl apply -f application.yaml
```
Now you can test the application! Open a browser on http://localhost:8080 or invoke rest services by CURL.
When you finish testing you can clean everything:
```
kubectl delete -f application.yaml
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
