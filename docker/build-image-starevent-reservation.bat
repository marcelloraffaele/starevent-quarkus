cd ..
cd starevent-reservation
mvn package -Dmaven.test.skip=true -Dquarkus.container-image.build=true
pause