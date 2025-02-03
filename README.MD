- Build
Clone
cd into dir
docker-compose up --build -d

- Confs
spring.data.mongodb.uri=mongodb://root:example@localhost:27017/mouts?authSource=admin

- Redis
Make sure Redis is running on localhost:6379. You can use Docker to run Redis:
```
docker run --name redis -d -p 6379:6379 redis
```

- Swagger UI
Access the Swagger UI at:
```
http://localhost:8081/swagger-ui.html
```

- Registra Pedido (Externo A)
curl -X POST http://localhost:8081/pedidos -H "Content-Type: application/json" -d "{\"codigoPedido\":\"123ABC\",\"valorTotal\":250.50,\"dataCriacao\":\"2024-01-30T12:00:00\",\"status\":\"RECEBIDO\"}"

- Lista Pedidos (Externo B)
curl http://localhost:8081/pedidos



--------------------------------------------------------------------------------------------------