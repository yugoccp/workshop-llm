# Hello Langchain4J

Sample project to start using the power of Langchain for Java! 

Specially created for [The Developer's Conference 2023](https://thedevconf.com/tdc/2023/future/) Future edition.

## Requirements
- Java 17
- Maven 3
- OpenAI account

## Usage

1. Create a copy of `.env.example` and rename to `.env`
2. Fill the `.env` file with the OpenAI API token and organization ID
3. Export `.env` file to make env vars available:
```bash
export $(cat .env | xargs)
```
4. Build the application from the source code:
```bash
./mvnw clean package
```
5. Use the following commands to run each example:
```bash
./mvnw exec:java -D'exec.mainClass=_00_Model'

./mvnw exec:java -D'exec.mainClass=_01_Prompt'

./mvnw exec:java -D'exec.mainClass=_02_Memory'

./mvnw exec:java -D'exec.mainClass=_03_Retrieval'

./mvnw exec:java -D'exec.mainClass=_04_Agents'
```

## References
- [langchain4j-examples](https://github.com/langchain4j/langchain4j-examples)
- [Introduction to LangChain](https://www.baeldung.com/java-langchain-basics)
- [Java Meets AI: A Hands-On Guide to Building LLM-Powered Applications with LangChain4j](https://devoxx.be/talk/?id=34002)