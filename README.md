# Feed Reader Service

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

```java 8, tomcat 9, maven, postman```

## Built With & Deployment

```mvn clean install```

## Running the tests

Testing with Postman for localhost env:
https://documenter.getpostman.com/view/2652877/feedreaderservice/6nBuqFi

```The feed-service supports following RESTful methods: 

-create a Feed (PUT)

-create an article, prereq: feed needs to exist (PUT)

-subscribe a user to a feed (POST)

-unsubscribe a user to a feed (POST)

get user's feed (GET)

get user's articles (GET)
```
