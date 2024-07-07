# Memoize

## Why?

### The un-cacheable nature of some data across multiple runs of the same logic

As a matter of fact, it's not always possible to cache some data across API calls. At times, there's a need to memoize
the result of a function call within the boundary of only one single API call. For example, let's say we call a Map
service to get the distance to travel from point A to point B. Across different API calls at different points in time,
the resulting distance might be different due to changing traffic conditions. Hence, it's only feasible to cache the
result within a single API call so that all logic in this boundary uses the same distance value.

Similarly, the processing of each incoming Kakfa or RabbitMQ event might also need its own cache of memoized results
so that we can maintain high-performance without leaking data from one event handling iteration to the next.

### The code structure constraint

In addition to the un-cacheable nature of some data across multiple runs of the same logic, the key that lets you know
memoization is required is when you need to execute the same sub-logic with the same set of arguments more than once.

For example, let's say you need to fetch `UserInfo` for a given `userId` once to get `countryOfResidence` for validation
and once to get `email` for sending order confirmation email. These logic could have been developed as 2 independent
modules that take `userId` as input.

You can choose to refactor the 2 modules to take `UserInfo` as input and perform the fetching logic in the parent code.
While it might sound simple initially, you should first imagine your code as a tree. The common parent of these 2 modules
might be X levels above them. This means you need to pass `UserInfo` from the common parent through X levels of code
before reaching these 2 modules.

Alternatively, you can maintain the independence of your 2 modules and use this library to apply memoization instead.
Your code at runtime will fetch `UserInfo` for a given `userId` only once and then reuse the result in subsequent calls.
You effectively get the performance benefits without the huge cost of refactoring.

## How to use

Take a look and run the examples provided in the `sample` project to understand how to use this library and observe
the potential impacts this library can deliver for you.