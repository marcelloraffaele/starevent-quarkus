# starevent-quarkus
A microservices event booking application developed using Quarkus

Images are already pushed on Docker hub, you can use it and jump "image build".

# Image build
If you want to rebuild the images:

```
cd starevent-event
mvn package -Dmaven.test.skip=true -Dquarkus.container-image.build=true

cd ..
cd starevent-reservation
mvn package -Dmaven.test.skip=true -Dquarkus.container-image.build=true

cd ..
cd starevent-frontend
mvn package -Dmaven.test.skip=true -Dquarkus.container-image.build=true
```

# Deploy on Docker

Let's start the application with docker compose:
```
cd docker
docker-compose -f docker-compose.yaml up -d
```

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

When you finish testing you can clean everything:
```
docker-compose -f docker-compose.yaml down
```


# Deploy on Kubernets

Let's start the application with docker compose:
```
cd kubernetes
kubectl apply -f application.yaml
```

When you finish testing you can clean everything:
```
kubectl delete -f application.yaml
```