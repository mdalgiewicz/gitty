
#####Architektura
Aplikacja powstała w oparciu o architekturę heksagonalną z wykorzystaniem wzorca CQRS.

#####Stos technologiczny:
* Java 8
* Spring Boot 2.0
* Maven 3
* GitHub API v3
* Spock
* JUnit 4

#####Uruchomienie
Opcjonalnie: `$ mvn failsafe:integration-test` 

`$ mvn clean install`

`$ java -jar ../gitty/gitty-rest-api/target/Gitty.jar`

`Language: en, pl`
`Media Types: application/json`
`
Http GET: http://localhost:8090/gitty/v1/repositories/dalgim/gitty`

Przykład prawidłowej odpowiedzi: 
```json
{
    "fullName": "dalgim/gitty",
    "description": "Fast and reliable message broker built on top of Kafka.",
    "cloneUrl": "https://github.com/dalgim/gitty.git",
    "stars": 341,
    "createdAt": "2015-05-15T08:11:16",
    "_links":{
        "self":{
          "href": "http://localhost:8090/gitty/v1/repositories/dalgim/gitty"
          }
     }
}
```

Przykład błędnej odpowiedzi:

```json
{
    "status": 404,
    "errorCode": "repository_not_found",
    "message": "Repozytorium nie istnieje",
    "timestamp": "2018-04-15T23:35:21.294"
}
```
