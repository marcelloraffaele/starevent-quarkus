cd..
cd starevent-event
mvn package -Dmaven.test.skip=true -Dquarkus.container-image.push=true
pause