[![Build Status](NOTE: Use something like Travis CI http://about.travis-ci.org/docs/user/getting-started/)

# Project name


## About

Simple Java Web Hello World project using gradle, jetty, guice, jersey, jackson, logback, junit, easymock, powermock.
This is inspired by [Dropwizard](http://dropwizard.codahale.com/) - probably you'd rather use DW directly.

## Features

To see what has changed in recent versions of __PROJECT__, see the [CHANGELOG]().

## Development

*   **run.py**: runs the project on two modes: *testing* and *server*;
    *   **testing**: watch for modifications on the *src* folder and runs *gradle test*;
    *   **server**: runs *gradle run*, then watch for modification on the *src* and runs *gradle war* to compile and copy
        all modified files to the running directory. Modification to class files will be hot reloaded, thanks to [pring-loaded](https://github.com/SpringSource/spring-loaded).

### Other useful *gradle* tasks

*   **reports** - generate several reports for the project: *tests results*, *code coverage* and *javadoc*;
*   **eclipse** - generates/update eclipse *.project* and *.classpath* files - if using eclipse you'll need to run this command everytime you change your dependencies;
*   **dependencyUpdates** - ;
*   **license**(?) - update the source file's header with the license header from **resources/HEADER.license**.

