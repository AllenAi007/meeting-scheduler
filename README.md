# Meeting Scheduler

## Pre Installation
- Java 8 +
- Maven 3.0 +

## How to Run
- Run without default talks.json from classpath:  
`mvn exec:java`
- Run with parameter /absolutePath/talks.json:  
`mvn exec:java -Dexec.args="/Users/aihua/Downloads/talks.json"`

## Run test, test coverage report
`mvn clean test`