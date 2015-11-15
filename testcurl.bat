curl -X POST http://localhost:9000/user -H "Content-Type: application/json" -d "{\"email\": \"test@test.com\"}"

curl -X GET http://localhost:9000/user/1

curl -X PUT http://localhost:9000/user/1 -H "Content-Type: application/json" -d "{\"email\": \"newemail@test.com\"}"

curl -X GET http://localhost:9000/user/1

curl -X DELETE http://localhost:9000/user/1

curl -X GET http://localhost:9000/user/1



curl -X POST http://localhost:9000/item -H "Content-Type: application/json" -d "{\"name\": \"Kulot Soraya\", \"price\": 10}" -i

curl -X GET http://localhost:9000/item/1

curl -X PUT http://localhost:9000/item/1 -H "Content-Type: application/json" -d "{\"name\": \"Kulot Soraya\", \"price\": 88}" -i

curl -X GET http://localhost:9000/item/1

curl -X DELETE http://localhost:9000/item/1

curl -X GET http://localhost:9000/item/1



curl -X POST http://localhost:9000/user/1/purchase -H "Content-Type: application/json" -d "{\"status\": \"shopping\"}" -i


curl -X POST http://localhost:9000/purchase/3/cart -H "Content-Type: application/json" -d "{\"itemId\": 2, \"quantity\": 3}" -i

curl -X GET http://localhost:9000/purchase/3/cart/2

curl -X PUT http://localhost:9000/purchase/3/cart/2 -H "Content-Type: application/json" -d "{\"itemId\": 1, \"quantity\": 30}" -i

curl -X GET http://localhost:9000/purchase/3/cart/2

curl -X DELETE http://localhost:9000/purchase/3/cart/2

curl -X GET http://localhost:9000/purchase/3/cart/all

