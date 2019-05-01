# QueueSimulator
Application simulate server queue. M/M/1 queue which represents the queue 
length in a system having a single server, where arrivals are determined 
by a Poisson process and job service times have an exponential distribution.

![Alt text](img/mm1.png "M/M/1")

## Project tasks
####Task1:
Operate mean arrival time (lambda) between lowerValueOfArrivals and upperValueOfArrivals
and plot mean delay of the system E[T]

####Task2:
Turn on and turn off the system with probability of Poff and Pon, operate  mean arrival time (lambda)
between lowerValueOfArrivals and upperValueOfArrivals and plot mean delay of the system E[T].
Plot also theoretical values of E[T] as
E[T] = (lambda/(mu*Pon) + E(Coff)*Poff) / ((1 - lambda/(mu*Pon))*lambda)
####Task3:
The same as Task2 but system event handling has uniform distribution between (0.1;0.15).

## Requirements
* Groovy Version: 2.5.4 JVM: 1.8.0_191
* Gradle 5.4

## Usage
```$xslt
> gradle run
```

## Configuration

```$xslt
gedit src/main/groovy/resources/logback.xml --> root.level (INFO/DEBUG etc.)
```