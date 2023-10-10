echo '########## Generate Image Docker #############'

echo 'API 1'
cd spring-boot-3-and-distributed-tracing-one/
./gradlew bootBuildImage

echo 'API 2'
cd ../spring-boot-3-and-distributed-tracing-two/
./gradlew bootBuildImage