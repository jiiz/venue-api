# Venue API

Venue API is a Java based standalone service that provides a REST API for
retrieving FourSquare venues and storing them as favorites.

- Provides a list of FourSquare venues by given location name or coordinates, optionally filtered by venue name based search string.
- Retrieves a list of photos for specified venue.
- Aggregates a statistics summary for given venue.
- Allows user to save selected venues as favorites and add keywords to them.
- Allows user to retrieve and modify the favorite venues.

### Requirements

Following software is required to run Venue API:

- JDK 7 or newer (Tested with jdk1.8.0_45)
- Maven 3.x (Tested with 3.2.1)

In addition port __8080__ on localhost should not be reserved.

### How to run

Clone the project in the folder of your choosing.

Configure FourSquare API credentials by adding the maven profile below to
your maven user specific settings.xml file (`/Users/[user]/.m2/settings.xml` on Windows). You can also can supply the credentials on command line, see details below.
```xml
<profile>
    <id>venue-api</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
        <foursquare.client_id>[ID]</foursquare.client_id>
        <foursquare.client_secret>[SECRET]</foursquare.client_secret>
    </properties>
</profile>
```

Issue either one of the following commands in the project folder:

- If you setup the credentials to maven settings.xml
```
mvn clean install && tomcat7:run
```

- If you'd like to input them from command line
```
mvn clean install -Dfoursquare.client_id=[ID] -Dfoursquare.client_secret=[SECRET] && tomcat7:run
```

This will first install the project to your local maven repository and then deploy the built service WARs to a Tomcat 7 container.

Resources can be accessed on localhost at following URLs:

- Venues: `http://localhost:8080/venues?locName=[locName]&locCoords=[coordinates]&query=[query]`
- Venue photos: `http://localhost:8080/venues/[venueId]/photos`
- Venue summary: `http://localhost:8080/venues/[venueId]/summary`

- Favorite venues: `http://localhost:8080/favorites`
- Favorite venue: `http://localhost:8080/favorites/[venueId]`

### Architecture

Venue API follows Microservices architecture paradigm by splitting API functionalities into two different service deployments,
where decoupling is based on the data entities.

- __Search Venues service__ provides search operations to FourSquare venue data.
- __Favorite Venues service__ allows to store, update and delete favorite FourSquare venues with keywords to a file based storage.

Each Venue API services builds to a separate .war archive and can be deployed independently.

### Security & performance aspects

Security is taken into account by adding Cross-Site-Request-Forgery (CSRF) protection to all state changing requests (POST, PUT, DELETE).
State changing requests require a `X-Requested-With` header to be present in the request.

Performance is considered in the file storage implementation where the locking mechanism
allows multiple threads to concurrently read the data if no write operations are going on.
If the file is written, the storage is locked for other write and read operations.


### Testing

Venue API project contains both unit and integration tests to ensure system functionality.
Unit tests are kept lightweight and do not require any kind of deployment
due to mocked resources, e.g. a mocked FourSquare client.

Integration tests deploy the tested service into a Tomcat container and runs the tests against a live API.
For Venue Search service integration tests, the FourSquare server functionality is mocked in order to perform full functionality tests.

Only unit tests are run when Venue API is started with given instructions,
but full test setup with the integration tests can be run with the command below.

```
mvn clean verify -P integration-test
```

### Change log

- v1.0.0 - First version