# PropertySource
A way of abstracting away searching for "system properties". This package allows you to specify key/value properties as any mix of classpath resources, external property files, system variables and environmental variables. I anticipate extending this in the future to provide support for external key/value stores as well.

This project builds a JAR which can then be used within your projects.

## Building

The following notes all assume that you have access to the command line and know what to do there, have a fairly recent version of Maven, and at least Java 7. The code will build with Java 8, but is pegged to Java 7 compliance.

To build the  JAR after checking out the project:

```
git clone https://github.com/TheBellman/propertysource.git
cd propertysource
mvn clean package
```

After a bit of grinding, the JAR should be available at

```
target/PropertySource-1.1-SNAPSHOT.jar
```

A site report can be built locally as well, which will provide you with JavaDoc and test coverage details:

```
mvn site
```

This will result in a local web site that can be accessed via

```
target/site/index.html
```
**Note:** A current release version can be found in my private Artifactory at the URL below. There are instructions there for how to include this repository in your build cycle.

```
http://ec2-52-56-175-37.eu-west-2.compute.amazonaws.com:8080/#welcome
```

## Use

While it is possible to use the internal resolver classes, this is a very suboptimal way of using the package! Instead the ideal is to use `PropertySourceFactory.build()` to obtain a `PropertySource`:

```
PropertySourceConfig config = PropertySourceConfig.builder()
    .withCaching()
    .usingConsul("192.168.99.100", 8500, "properties/uat")
    .withFiles("/etc/main.properties", "/etc/other.properties")
    .withDirectory("/usr/local/lib/myapp/conf")
    .withResourceBase(MyClass.class)
    .withResources("default.properties");

PropertySource source = PropertySourceFactory.build(config);

String someValue = source.get("my.property.key");
boolean someFlag = source.getFlag("my.flag.key");
int someNumber = source.getNumber("minimum.size", 42);
```

Constructing a PropertySource in this way does define a specific hierarchy of locations. In priority order:

1. local cache, if in use
2. Consul, if reachable
2. JVM System Properties (c.f. defined with `-D<name>=<value>`)
2. environmental properties
3. available files
4. `.properties` files found in the specified directory
4. available resources

In it's current incarnation, caching is of little benefit, as the resolvers are all working from local resources that are effectively static, however since the local cache is checked first then frequently referenced properties will resolve as quickly as possible without going deeper into the resolution chain.

There is no reason why your code could not have different sources configured in different ways, each `PropertySource` is thread safe and independent of all others.

### Consul
Using [Consul](https://www.consul.io) as a property source is somewhat speculative, and I intend to provide a companion product as a fully fledged example of this library, including using Consul. In it's current form Consul support is quite simple, and makes three assumptions:

1. access is via HTTP, not HTTPS
2. There is no access constraint in play on retrieving from the Key/Value store
3. Keys take the form `prefix/key`, where `prefix` can be an arbitrarily deep hierarchy.

Consul's Key/Value store supports multipart keys, which I am interpreting to deal with as a namespace, in order to keep the resolution semantics the same as other sources of properties.

I am intending two enhancements around Consul for this library, first to deal with the constraints above, and secondly to allow the configuration properties for using Consul to be automatically picked up from the other available property sources if they are defined. This would allow a bootstrapping operation, where simple local properties could be used to point to Consul as a central shared property store.

### Logging
The package makes use of `slf4j`, and will be sensitive to the presence of `log4j` configuration. (In theory, currently this package should be considered a draft until I build an example test harness).
