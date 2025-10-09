# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

##Chess Web API Diagram Link
```sh
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygRUQHPOB5afJCIJqTsXzQo8cIVG+XYIEJ4oYoJwkEkSYCklZFTDvytSMiybJupyc40vuFTLmKEpujKcobu8Srbig7m3p53lNPofw7BiyVbDsAW8iOwXCqF64ZalAIRfKRXqZgKrBhqbrarq+p+RWHAQGoMBoBAzBWmisUADxJh59I9n26FQH1pRWbU0gtaoAByHVRmiUYxnGInIOYY2gbU6b4aMYw5qoebzNBRYlq6RGGM1rXtZ11o5A29GmFV6owOVOwwNk71oCA0AouAvX9Ql9LhVuVljQNo6vSVIPVJQG3Ol2kMcAAkl9P0oOAS0oLGClFGtGBwxJdTpgAjAR+2HQWYwndA9SI+9Arit9xbo2A91NqDAOBSOtTA-I-2lODtSIx6OmjUmE0OeKGSqABI1jaLtQjMsylHWM3wUVR9aLEsVyiQTNS1LhO1K4R+mq+rl6a8h2swHRTZMX4-heCg6AxHEiTO67Dm+FgomCptjTSBG-ERu0EbdD0cmqApwwa4h6Ajdp5nPHHSGYKLlnw7UNn2D79lCT7MBOWoLmxfFXOJUyz7xOelvx2g2ULqoeWiuKT6XjKqcJ09GpzcwDmMuu4r84LQ39qB+tIijVAmkgHDVzymPY-GevizDRNOKTu3k-mx3FjTkrTJe0BIAAXigux2wx4MtxkMwQDQAYd9DnZg4Do5j+nydi+NWc5z70tZYTyTArLuq0ShgH1s8I2WYr6MW8I7FE65-DYHFBqfiaIYAAHElQaD9uJA2jQsGhwjvYJUsc65py0uUUBlCE4Z3KBNZAOR7JomLiSMu5RBbMLADgnMtd4L10bkFcoIU27GmfnzHuMA+4CSEoPT6I9361E-sA3+nZajT1nvPSRwAl4rVxhAqBG8t7Zn5BTPep1D4URPufS+jZr7vxbgVGAZC5ivnhm-Cu9I3EoBFt-SeLoeGAIQFgNRtDBFp1Xuog2MDdq+NUAWBo4xfFI2kAWYm4RgiBBBJseIuoUCNQMmrEEyRQBqiKebEEviZpKm1hcGAnQ4GeAQQEDgAB2NwTgUBOBiBGYIcAuIADZ4ATkMHwwwhjzD+3XvUKSHRSHkKPpE9AWYalKk0uvJOsIU50MUqMdZcwzI7KgJnDRh45AoAxHAMZ7DS5uS4coi56IJkYkOVyG83jm6iPyuIheW5pGyIHhfRRHMBbKNUevQJXYtHIB0S+bQ+icbRM2iTMm5jd6Fn3qWZkyz4i2IvmzRxXyW4hnXBMj0gKOrGhgDPOFbUYpgtHr2ceUK14aIAEIhjuUileeNIHsoNttdFuZMVU2xfULl647lEq-ic6FY5bn-lCXLEB39ahgLWUqWpRypn40FdAvCBFUnpPqJk7JzSHYBEsCgPsEBNhuyQAkMANq7UOoAFIQHFNgis-gykgDVHqsSvpZmNCaMyGSPRfEUJWfskY2AEDABtVAOAEAbJQDWCazZhNtlfg1XsrMCak2UFTemzNSo0nHK-GcqgSIABWXq0AYk9VLFAhIS6uXhuXHKnkG3ileUW5NpboDlrmGk4RuUfm1CwUyJ+CKpGql7tS4FQ9ChMohSy1VMSp5oDpXPf58heXgPWgakxIqDpiupjivFBL7EPRvlOmdzAKUAsXTAdo+SBTtGDDMemlYzTyjDFQzxnMe30hfXzNRE1AwAaA5GaMWMDEotmemTM28MVHSxVYmDwZzTvvDMhOB0iJlGGmm1alXUchKK+SozdUGs5Sp5Qh5ex79UxOeMK9DorMPiqsVKoubbnKyvXTRkj-j5WCvrY2kJYS2WlAiZReuhbE1DrTSO1xFbpC635cYw2RrRiWtaU7KASbHXOq8KZxAwZYDAGwAmwgeQCgwCDTMwmcyg4hzDhHYwicaHqs-gwiaHBbVMiucF7g6I7mds7N2puY5uB4AxDyPQBghQTv5LfI4D9DAHuAJ8sD3zlx32y1FD0D6itZcfhBvLImCu0eGqLBV4XQsya3QrT+yG3NxJGHAoAA
```
