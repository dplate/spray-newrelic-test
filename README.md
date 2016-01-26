# spray-newrelic-test
A service to test the NewRelic Spray support.

## Usage
The service must be started with
```
-javaagent:newrelic.jar
```
to provide a NewRelic agent. In the same folder must be a `newrelic.yml` with the 
NewRelic credentials and a defined app name.

## Included test endpoints

### Transaction recording

#### http://localhost:8080/without-future
An endpoint which waits a second and then returns a string

#### http://localhost:8080/with-future
Same as before, but encapsulate inside an additional future

#### http://localhost:8080/with-future-onsuccess
Same as before, but using the onSuccess directive of Spray

### Error logging

#### http://localhost:8080/error-without-future
An endpoint which just throws an exception

#### http://localhost:8080/error-with-future
Same as before, but encapsulate inside an additional future

### More realistic example

#### http://localhost:8080/spray-client-without-future
An endpoint which calls another webpage via spray-client and returns the response code

#### http://localhost:8080/spray-client-with-future
Same as before, but encapsulate inside an additional future

## Results

### Java Agent 3.25.0

- :white_check_mark: without-future: Works
- :x: with-future: Nothing inside the future is recorded and the response time is wrong
- :x: with-future-onsuccess: Same as with-future
- :white_check_mark: error-without-future: Works
- :x: error-with-future: No error logged
- :white_check_mark: spray-client-without-future: Works
- :x: spray-client-with-future: Same as with-future