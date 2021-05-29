# store

## Development

To start your application in the dev profile, run:

```
./mvnw
```

## Building for production

### Packaging as jar

To build the final jar and optimize the store application for production, run:

```

./mvnw -Pprod clean verify


```

To ensure everything worked, run:

```

java -jar target/*.jar


```

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```

./mvnw -Pprod,war clean verify


```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

```
docker-compose -f src/main/docker/sonar.yml up -d
```

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```
