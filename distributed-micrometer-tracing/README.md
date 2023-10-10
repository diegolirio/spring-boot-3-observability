# Read First

## Run POC

Build and Generate images
```sh
./build-and-generate-images.sh
```

Run
```sh
docker-compose up
```

See distributed-traceId, in another terminal tab
```
docker-compose logs | grep [YOUR_TRACE_ID]
```

## Architecture and Design

![image](https://github.com/diegolirio/spring-boot-3-observability/assets/3913593/9cd769b9-afd9-449a-9e6a-edef05b9ec87)
