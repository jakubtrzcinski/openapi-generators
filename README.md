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
$ ./oasgen.sh gen {path-to-swagger-json} {base-directory}
```

eg.
```bash
$ ./oasgen.sh gen https://petstore.swagger.io/v2/swagger.json packages/shared/lib/api
```

## Examples
All following examples are generated from https://petstore.swagger.io/ with unmodified templates

### Flutter/Dart and Retrofit

<details>
    <summary>/pet/pet_client.dart</summary>

```dart
import 'package:dio/dio.dart';
import 'package:retrofit/retrofit.dart';

import 'model/pet.dart';

part 'pet_client.g.dart';
@RestApi()
abstract class PetClient {
factory PetClient(Dio dio, {String baseUrl}) = _PetClient;

    @POST("/pet/{petId}/uploadImage")
    Future<void> uploadFile(
             int petId
    );

    @PUT("/pet")
    Future<void> updatePet(
             Pet payload
    );

    @POST("/pet")
    Future<void> addPet(
             Pet payload
    );

    @GET("/pet/findByStatus")
    Future<void> findPetsByStatus(
             String status
    );

    @GET("/pet/findByTags")
    Future<void> findPetsByTags(
             String tags
    );

    @GET("/pet/{petId}")
    Future<void> getPetById(
             int petId
    );

    @POST("/pet/{petId}")
    Future<void> updatePetWithForm(
             int petId
    );

    @DELETE("/pet/{petId}")
    Future<void> deletePet(
             String api_key,
             int petId
    );
}
```
</details>

<details>
    <summary>/pet/model/pet.dart</summary>

```dart
import 'package:json_annotation/json_annotation.dart';

import '../../commons/model/category.dart';
import '../../commons/model/tag.dart';

part 'pet.g.dart';

@JsonSerializable()
class Pet {
  int? id;
  Category? category;
  String name;
  String photoUrls;
  Tag? tags;
  String? status;

  Pet({
    this.id,
    this.category,
    required this.name,
    required this.photoUrls,
    this.tags,
    this.status,
  });

  factory Pet.fromJson(Map<String, dynamic> json) => _$PetFromJson(json);

  Map<String, dynamic> toJson() => _$PetToJson(this);
}

```
</details>


### Kotlin Retrofit
todo
### Java FeingClient
todo
### Kotlin FeingClient
todo