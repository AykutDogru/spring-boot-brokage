#Intellij IDEA has been used#

You can start the application with `./mvnw spring-boot:run` command.
The embedded H2 database will be started along with the application.

## Roles
A user can be logging in via HTTP Basic or be unauthenticated. An authenticated user has a role according
enum `UserRole`, *ADMIN* or *USER*.

## Endpoints

There are four endpoints

| Method | Path                | Descriptions                            |
|--------|---------------------|-----------------------------------------|
| GET    | /orders             | List all existing orders                |
| POST   | /orders             | Adds a new order                        |
| POST   | /orders/match-order | Match pending orders                    |  
| DELETE | /orders/{orderId}   | Delete order by ID                      |
| POST   | /assets/withdraw    | Withdraw TRY for given amount and iban  |
| POST   | /assets/deposit     | deposit TRY for given amount and iban   |
| POST   | /assets             | List all existing assets  by customerId |

## Users
The initial admin user (admin/admin) is added via the `data.sql` that is added on the start of the 
application after creating on the database scheme. New user can be added via the rest endpoint. The entity
`Person` represents a user in the database.


## Swagger UI
The swagger ui can be accessed via the url <http://localhost:8080/swagger-ui.html>. The class `BasicAuth`
is a marker for the swagger ui to indicate an endpoint needs HTTP Basic authentication.
