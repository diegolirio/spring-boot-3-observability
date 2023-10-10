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

>>>