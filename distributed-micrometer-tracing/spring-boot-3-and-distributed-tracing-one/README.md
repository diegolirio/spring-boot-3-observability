# One Application

> Request start here

```shell
curl localhost:8079/customers
```

```shell
curl -H "x-b3─traceid : -65249b3b575efbaea3be01eb630e7f61" \
  localhost:8079/customers

```


```shell
curl -H 'Content-Type: application/json' \
      -d '{ "name":"foo","detail":{"document": "123", "bornAt": "2000-05-03"}}' \
      -X POST \
      localhost:8079/customers
```