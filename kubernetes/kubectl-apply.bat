kubectl apply -f database-config.yaml
kubectl apply -f database.yaml
kubectl apply -f application-event.yaml
kubectl apply -f application-reservation.yaml
kubectl apply -f application-frontend.yaml

kubectl apply -f monitoring.yaml

kubectl port-forward service/starevent-frontend 8080
kubectl port-forward service/grafana 3000
pause