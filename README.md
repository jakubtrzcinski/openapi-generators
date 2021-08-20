# OpenApi Generators

An alternative for standard swagger generator that provides you 
an elegant code with various customizations with [Apache Velocity](https://github.com/apache/velocity-engine) templates.

## Supported Languages and Frameworks

### Java
* FeingClient
* Retrofit
* RestTemplate

### Kotlin
* FeingClient
* Retrofit
* RestTemplate

### Flutter / Dart
* Retrofit

### Angular
* HttpClient


## Requirements
To run generator it's needed to have at least JDK 11 installed on your machine.

## Usage
### Initializarion
Run in root of your project to download starting script
```bash
$ wget {jklhkh}/oasgen
$ chmod +x oasgen
```

To initialize generator in your project simply run following command. 

it will detect language of your project and initialize starting templates for given platform
```bash
$ ./oasgen init
```


### Generation

```bash
$ ./oasgen gen {path-to-swagger-json} {base-directory} --remove-conflicted
```

