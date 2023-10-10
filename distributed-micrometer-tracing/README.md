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

Request HTTP
```sh
curl -X GET http://localhost:9999/customers
```



See distributed-traceId, in another terminal tab
```
docker-compose logs | grep [YOUR_TRACE_ID]
```

## Architecture and Design

Sync :: `GET /customers`
![image](https://github.com/diegolirio/spring-boot-3-observability/assets/3913593/9cd769b9-afd9-449a-9e6a-edef05b9ec87)

Async :: `POST /customers` and then `service1 produces message (customer) to topic and detail consumes and persist one`
![image](https://github.com/diegolirio/spring-boot-3-observability/assets/3913593/7049c6f8-a311-4517-95cd-c35e5378b9e0)
