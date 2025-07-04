# workshop-llm

This is a Workshop repository to help you understand core concepts to build LLM-powered applications in Java!

## Introduction

Large Language Models became extremely popular after ChatGPT and many other generative applications became available for the public. 
They unlocked a greenfield of possibilities for new products and services, transforming our daily routine and existing solutions!

Understanding some fundamentals of this new technology will help you keep updated and stay relevant in the AI revolution :)

## Setup your environment

For this workshop, you'll need to setup the following:

- Java 21+ (https://jdk.java.net/archive/)
- Maven (https://maven.apache.org/download.cgi)

## Concepts & Hands-on

### Models
In the context of Large Language Models (LLMs), "models" refer to the algorithms or computational frameworks trained on vast amounts of text data to understand, generate, and manipulate natural language. These models use statistical methods to find patterns in data, enabling them to predict the next word in a sentence, answer questions, translate languages, and perform a variety of other language-based tasks.

### Memory
Memory does not mean "memory" in the traditional sense of storing information long-term, as LLMs do not remember past interactions or learn from them incrementally in real-time. Instead, it refers to the model's ability to retain and utilize information from the input it receives during a single interaction. The memory capacity of an LLM is often quantified by the number of tokens (words or pieces of words) it can process in one go.

### Retrieval Augmented Generation
Retrieval-Augmented Generation (RAG) is an advanced model architecture that enhances the performance of language models by enriching them with external source of data.

### Tools
Tools are actions that a language model can choose to take. With tools, a language model is used as a reasoning engine to determine which actions to take and in which order.

### Agents
Agents are a more advanced form of tools, where the language model can choose to call one or more tools in a specific order, based on the context of the conversation. Agents can also have memory, allowing them to remember past interactions and use that information to inform their decisions.

## Usage

1. Build the application from the source code:
```bash
./mvnw clean package
```

2. Use the following commands to run each example:
```bash
./mvnw exec:java -D'exec.mainClass=_01_Model'

./mvnw exec:java -D'exec.mainClass=_02_Memory'

./mvnw exec:java -D'exec.mainClass=_03_Retrieval'

./mvnw exec:java -D'exec.mainClass=_04_0_Tools'

./mvnw exec:java -D'exec.mainClass=_04_1_Tools'

./mvnw exec:java -D'exec.mainClass=_05_Agents'
```

## Running the examples in Docker
You can also run the examples in Docker.
1. Build the Docker image:
```bash
docker build -t workshop-llm .
```

2. Run the Docker container:
```bash
docker run -it --rm workshop-llm
```

Now you can run the examples inside the Docker container using the same commands as above :)