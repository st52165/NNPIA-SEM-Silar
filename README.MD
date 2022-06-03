# Spring Boot Security and working example JWT authentication

Source codes are downloaded from https://www.javainuse.com/spring/boot-jwt

This example was created because of broken dependencies in the origin source codes. 
Demo of http request for IntelliJ is included in the file [example.http](example.http)

Workspace for [Insomnia](insomnia-workspace.json) is also included.

The application contains following two basic endpoints:
- POST      /authenticate - returns token
- GET       /hello - test authenticated endpoint 

The application contains employee REST API which simulates real-life use-case  :
- GET       /employees - returns all employees
- POST      /employees - creates new employee
- DELETE    /employees/{employeeId} - delete employee by id


### Get token request
```http request


> POST /authenticate HTTP/1.1
> Host: localhost:8080
> User-Agent: insomnia/2020.4.2
> Content-Type: application/json
> Accept: */*
> Content-Length: 59

| {
|     "username": "javainuse",
|     "password": "password"
| }
```

#### Get response
```
HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 05 May 2021 18:24:21 GMT

{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2MjAyNTcwNjEsImlhdCI6MTYyMDIzOTA2MX0.jsQ-jG6A2HHyXKFbYoBGzXbhqpK134M2Uw6yvXA1DD1lEGltNRO3-c1u04yLvs9HJ24q0aQz1CVlky6EcQ53uA"
}

Response code: 200; Time: 103ms; Content length: 191 bytes

```

---

### Request with bearer token
Then the token have to be used in next request
```http request

> GET /hello HTTP/1.1
> Host: localhost:8080
> User-Agent: insomnia/2020.4.2
> Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2MjAyNTcyNTMsImlhdCI6MTYyMDIzOTI1M30.GmNaf94DRscU5DDJ88lWpJ-MQzQqZR8DYgXEA4h2PKNuHmHsZOZ7qpraXT4_Wk7tPTlnentvDSa3vN1pfgrP7Q
> Accept: */*
```
#### Response 
```
HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/plain;charset=UTF-8
Content-Length: 11
Date: Wed, 05 May 2021 18:25:49 GMT

Hello World

Response code: 200; Time: 21ms; Content length: 11 bytes
```


## Example in insomnia

![Get token](./img/get-token.png)

![Get token](./img/get-request.png)

