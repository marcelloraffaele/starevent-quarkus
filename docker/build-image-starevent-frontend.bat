cd..
cd starevent-frontend
mvn package -Dmaven.test.skip=true -Dquarkus.container-image.build=true
pause