# venus-sample-custom-function

We will create a simple custom function to translate words from english to Quenya (if a translation is available).

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](CHANGELOG.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit79_venus-sample-custom-function&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit79_venus-sample-custom-function)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit79_venus-sample-custom-function&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit79_venus-sample-custom-function)
[![License: MIT](https://img.shields.io/badge/License-MIT-teal.svg)](https://opensource.org/licenses/MIT)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)

This project is part of a series of mini tutorial on [Venus Fugerit Doc](https://github.com/fugerit-org/fj-doc),
here you can find the [other tutorials](https://github.com/fugerit79/venus-sample-index).

## Key modifications

1. Create our custom function (refer to [Apache FreeMarker documentation](https://freemarker.apache.org/docs/pgui_datamodel_method.html)).

```java
package org.fugerit.java.demo.venussamplecustomfunction.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.freemarker.fun.FMFunHelper;

import java.util.List;
import java.util.Properties;

/*
 * Translate a word to Quenya if available, return the same word otherwise
 *
 * Quenya is the language spoken by the high elves in J.R.R. Tolkien's world (https://lotr.fandom.com/wiki/Quenya).
 */
public class QuenyaFun implements TemplateMethodModelEx {

    private static final Properties QUENYA = PropsIO.loadFromClassLoaderSafe( "config/quenya.properties" );

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        FMFunHelper.checkParameterNumber( arguments, 1 );
        String wordToTranslate = arguments.get( 0 ).toString();
        // if not found, default to the input word
        String output = QUENYA.getProperty( wordToTranslate, wordToTranslate );
        return new SimpleScalar(output);
    }

}
```

and the [config/quenya.properties](src/main/resources/config/quenya.properties) file with words translations.

2. Add a config step to initialize one or more functions : 

```xml
<!-- adding quenya translate function to shared chain -->
<chainStep stepType="function">
    <function name="quenyaFun" value="org.fugerit.java.demo.venussamplecustomfunction.fun.QuenyaFun"/>
</chainStep>
```

3. Use the new function in [document.ftl](src/main/resources/venus-sample-custom-function/template/document.ftl) template : 

```ftl
<#-- using quenyaFun to translate title to Quenya if available -->
<cell><para>${quenyaFun(current.title)}</para></cell>
```

For instance, result in MarkDown format will be : 

```md
My sample title XML  


| Name | Surname | Title  |
|---------------|---------------|---------------|
| Luthien | Tinuviel | Tári  |
| Thorin | Oakshield | Aran  |
```

## Original project README

Here starts the original project readme as created by command :

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:8.16.9:init \
-DgroupId=org.fugerit.java.demo \
-DartifactId=venus-sample-custom-function \
-Dextensions=base,freemarker,mod-fop \
-DaddJacoco=true \
-DprojectVersion=1.0.0 \
-Dflavour=quarkus-3
```

## Quickstart

Requirement :

* maven 3.9.x
* java 21+ (GraalVM for native version)

1. Verify the app

```shell
mvn verify
```

2. Start the app

```shell
mvn quarkus:dev
```

3. Try the app

Open the [swagger-ui](http://localhost:8080/q/swagger-ui/)

Test available paths (for instance : [/doc/example.md](http://localhost:8080/doc/example.md))

NOTE:

* Powered by Quarkus 3.28.2
* Using Fugerit Venus Doc 8.16.9 (extensions : base,freemarker,mod-fop)

## Native version

If you picked only native modules, you should be able to build and run the AOT version (GraalVM 21+ needed).

Further documentation :

* [List of modules and native support](https://venusdocs.fugerit.org/guide/#available-extensions)
* [Fugerit Venus Doc native support introduction](https://venusdocs.fugerit.org/guide/#doc-native-support)

1. Build and verify

```shell
mvn package -Dnative
```

2. Start

```shell
./target/venus-sample-custom-function-1.0.0-runner
```

## Overview

This project has been initialized using [fj-doc-maven-plugin init goal](https://venusdocs.fugerit.org/guide/#maven-plugin-goal-init).

The quarkus 3 structure is similar to running the quarkus create goal : 

```shell
mvn io.quarkus.platform:quarkus-maven-plugin:3.28.2:create \
-DprojectGroupId=org.fugerit.java.demo \
-DprojectArtifactId=venus-sample-custom-function \
-Dextensions='rest,rest-jackson,config-yaml,smallrye-openapi'
```

## Quarkus readme

From here on, this is the original quarkus readme.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/getting-started-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- YAML Configuration ([guide](https://quarkus.io/guides/config-yaml)): Use YAML to configure your Quarkus application

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
