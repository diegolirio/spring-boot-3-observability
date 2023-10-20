echo '########## Generate Image Docker #############'

echo 'API 1'
cd spring-boot-3-micrometer-tracing-customers/
./gradlew bootBuildImage

echo 'API 2'
cd ../spring-boot-3-micrometer-tracing-addresses/
./gradlew bootBuildImage