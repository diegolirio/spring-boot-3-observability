# Customer Application

```shell
curl -H 'Content-Type: application/json' \
      -d '{ "name":"foo", "bornAt": "2000-05-03", "addresses": [{ "street": "Av Paulista", "number": 1320, "district": "Bela Vista", "city": "Sao Paulo", "state": "SP" }]}}' \
      -X POST \
      localhost:8080/customers
```


    