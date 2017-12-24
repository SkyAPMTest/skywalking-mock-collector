# Apache SkyWalking collector simulator

## Why need a collector simulator?
Since sky-walking begun more and more complex from 3.0, testing all agent feature in manually seems impossible, and the maintainer team has less time to do so.

And even collector can run in standalong mode from 3.2, still hard to run the automatic-test for agent.

So here we come, **A whole new collector simulator**.

## What does the simulator do?
It is a simple application, with implementing all services, which required by agent. 
It just saves all the requests into files, only returns necessary mock result.

This collector uses in [agent auto tests](https://github.com/SkywalkingTest/skywalking-autotest-scenarios).
