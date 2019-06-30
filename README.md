# Meeting Scheduler

## Pre Installation
- Java 8 +
- Maven 3.0 +

## How to Run
- Run without default talks.json from classpath:  
`mvn exec:java`
- Run with parameter to parse external talk files /absolutePath/talks.json:  
`mvn exec:java -Dexec.args="/Users/aihua/Downloads/talks.json"`

## Run test, test coverage report coverage 89%
- Run mvn clean test as below and then open target/site/jacoco/index.html in browser  
`mvn clean test`