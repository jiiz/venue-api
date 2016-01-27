# Venue API

Venue API is a Java based standalone service that provides a REST API for
retrieving FourSquare venues and storing them as favorites.

- Provides a list of FourSquare venues by given location name or coordinates, optionally filtered by venue name based search string.
- Retrieves a list of photos for specified venue.
- Aggregates a statistics summary for given venue.
- Allows user to save favorite venues with keywords.
- Allows user to modify favorite venue keywords
- Allows user to retrieve and delete the favorite venues.

### Requirements

Following software is required to run Venue API:

- JDK 7 or newer (tested with jdk1.8.0_45)
- Maven 3.x (tested with 3.2.1)

In addition port __8080__ on localhost should not be reserved.

### How to run

Clone the project in the folder of your choosing.

Configure FourSquare API credentials by adding the maven profile below to
your maven user specific settings.xml file (`/Users/[user]/.m2/settings.xml` on Windows). You can also can supply the credentials
from command line, see details below.
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

_Credentials in settings.xml_
```
mvn clean install && tomcat7:run
```

_Credentials from command line_
```
mvn clean install -Dfoursquare.client_id=[ID] -Dfoursquare.client_secret=[SECRET] && tomcat7:run
```

This will first install the project to your local maven repository and then deploy the built service WARs to a Tomcat 7 container.

### Usage

After the services are successfully started the resources can be accessed on `http://localhost:8080` as specified in the table below.

| Resource                | Path                                                              | Methods           |
|-------------------------|-------------------------------------------------------------------|-------------------|
| Venues                  | `/venues?locName=[locName]&locCoords=[coordinates]&query=[query]` | GET               |
| Venue photos            | `/venues/[venueId]/photos`                                        | GET               |
| Venue summary           | `/venues/[venueId]/summary`                                       | GET               |
| Favorite venues         | `/favorites`                                                      | GET, POST, DELETE |
| Favorite venue          | `/favorites/[venueId]`                                            | GET, DELETE       |
| Favorite venue keywords | `/favorites/[venueId]/keywords`                                   | PUT               |

### Authentication

Adding and modifying favorite venues requires HTTP Basic authentication,
and a CSRF protection header `X-Requested-By` with arbitrary value in the request.
Without the CSRF header HTTP 400 - Bad Request is returned.

Default username and password are `apiuser` / `password`

### Architecture

Venue API follows Microservices architecture paradigm by splitting API functionalities into two different service deployments,
where decoupling is based on the data entities.

__Search Venues service__ provides search operations to FourSquare venue data.
__Favorite Venues service__ allows to store, update and delete favorite FourSquare venues with keywords to a file based storage.

Each Venue API services builds to a separate .war archive and can be deployed independently.

### Security & performance aspects

Security is taken into account by requiring HTTP Basic Auth for state changing operations (POST, PUT, DELETE) and
enabling Cross-Site-Request-Forgery (CSRF) protection for these operations.
Enabled CSRF protection means that state changing requests require a `X-Requested-By` header to be present in the request.

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