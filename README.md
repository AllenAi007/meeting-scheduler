# Meeting Scheduler

## Pre Installation
- Java 8 +
- Maven 3.0 +

## How to Run
- Go to project home folder, cd meeting-scheduler
- Run without parameter, will use default talks.json from classpath:  
`mvn clean compile exec:java`
- Run with parameter to parse external talk files /absolutePath/talks.json:  
`mvn clean compile exec:java -Dexec.args="/Users/aihua/Downloads/talks.json"`

## Run test, test coverage report coverage 88%
- Run mvn clean test as below and then open target/site/jacoco/index.html in browser  
`mvn clean test`