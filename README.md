# PILOTES's API
## Overview
This API provides functionalities to manage Orders of _Pilotes_. It is built using Java 11 and Spring and Springboot, allowing users to create, edit orders and customers.

## Getting Started

### Run the application

1) Make sure you have Maven and Java 11 installed.
2) Uncompress de project and got to the root directory of the project and execute
```
mvn clean package
```
2) Open the folder _target_ of the project and execute:

```
java -jar backend-technical-test-2.0.0-SNAPSHOT.jar
```

2) The most simple way to do this is using the Swagger API

Open this [URL](http://localhost:8090/swagger-ui/index.html)

3) These are que endpoints

Things to take into account:

To get access to the _Search Orders_ endpoint are needed the following credentials:
```
user: admin
password: admin
```

To get access to de _Data Base Admin_ once started the application, follow the next URL:
http://localhost:8090/h2-console
and use the credentials:
```
user: sa
password: p
```

## Use Cases (using Swagger API)
### Create an Order
Assuming we want to create a new Order of 5 pilotes for Mr. X then we do the following
1) Create the Client "Mr. X" assuming that is not created yet we
```
POST /api/v1/clients/create 
```
```json
{
  "firstName": "Mr. X",
  "lastName": "Some",
  "emailAddress": "mrx@some.com",
  "phoneNumber": "499628727"
}
```
2) Once the _Client_, keep the _id_ in mind and proceed to create the _Order_
```
POST /api/v1/orders/create 
```
* **deliveryAddress**: Address where will be delivered
* **quantityPilotes**: The allowed values are FIVE, TEN, FIFTEEN corresponding to 5, 10 and 15 respectively
* **client**: Client id created before.
* **unitPrice**: price for unit.
```json
{
  "deliveryAddress": "some address",
  "quantityPilotes": "FIVE",
  "client": 1,
  "unitPrice": 1.2
}
```
3) To list the _Order_ we use the endpoint which filters our search by the fields: _First Name_, _Last Name_, _Email_
* Here we will be asked for the credentials because is a secured endpoint.
* This filter match partially with each field also.
```
GET /api/v1/orders/search?firstName=Mr x&lastName=Some&email=Some 
```
4) To edit some _Order_, we use the endpoint.
```
PUT /api/v1/orders/update
```
* We can change all the fields of the order.
```json
{
  "deliveryAddress": "string",
  "quantityPilotes": "FIVE",
  "client": 0,
  "unitPrice": 0,
  "id": 0
}
```

If we want to _List_ or _Edit_ a _Client_ we can see the _Swagger_ documentation.


### By **Martín Bolatti**
