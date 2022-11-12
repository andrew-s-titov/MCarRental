# MCarRental

A small project with microservice architecture to demonstrate my current skills.

The app is intended for car booking: car owners can add their cars and manage them, and clients are able to rent available cars and manage their bookings. There's an endpoint for car owners to mark bookings as finished and also to report car damage.  
Any user (car owner or client) must register using email and password in order to use the app. JWT (access and refresh) are created on login and are used for authentication and authorization.  
Users are notified by email about all booking events: owners - regarding their cars, clients - regarding their bookings. 


Technologies used:
- Java 11
- HTML (email template)

Frameworks:
- Spring (Boot, MVC, WebFlux, Data, Security, AOP)
- Spring Cloud (Configuration Server, Service Registration and Discovery, Netflix OpenFeign)
- JUnit, Mockito (tests)

Tools:
- Docker
- PostgreSQL
- Flyway
- ElasticSearch
- Kafka
- Java Mail Sender
- Thymeleaf
- Lombok

Features:
- i18n (English, Russian) for email messages