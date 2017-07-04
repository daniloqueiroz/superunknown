[![Release](https://jitpack.io/v/daniloqueiroz/superunknown.svg)](https://jitpack.io/#daniloqueiroz/superunknown)

# Superunknown


## About

Simple Java Web framework based on Jersey, Netty, Logback and Gson.

It's actually just a Glue between these libraries - and apart from this, it's not much opinionated.

## Getting start

```java
public static void main(String[] args) {
    new Application()
        .register(MyResource.class)
        .start();
}
```

See [Application.java](https://github.com/daniloqueiroz/superunknown/blob/master/src/main/java/superunknown/Application.java) for more about how to get started.
