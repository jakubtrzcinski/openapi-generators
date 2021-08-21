# OpenApi Generators

**THIS IS EARLY PREVIEW OF THE GENERATOR, not all features are yet implemented!**

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
$ wget https://raw.githubusercontent.com/jakubtrzcinski/openapi-generators/master/oasgen.sh
$ chmod +x oasgen.sh
```

To initialize generator in your project simply run following command. 

it will detect language of your project and initialize starting templates for given platform
```bash
$ ./oasgen.sh init
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

### Dart Retrofit

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
<details>
    <summary>/pet/PetClient.kt</summary>

```dart
package io.trzcinski.test.pet

import retrofit2.http.*
import retrofit2.Call

import io.trzcinski.test.pet.dto.Pet

interface PetClient {

        @POST("/pet/{petId}/uploadImage")
        fun uploadFile(
             petId: Int
        ): Call<Void>

        @PUT("/pet")
        fun updatePet(
             payload: Pet
        ): Call<Void>

        @POST("/pet")
        fun addPet(
             payload: Pet
        ): Call<Void>

        @GET("/pet/findByStatus")
        fun findPetsByStatus(
             status: String
        ): Call<Void>

        @GET("/pet/findByTags")
        fun findPetsByTags(
             tags: String
        ): Call<Void>

        @GET("/pet/{petId}")
        fun getPetById(
             petId: Int
        ): Call<Void>

        @POST("/pet/{petId}")
        fun updatePetWithForm(
             petId: Int
        ): Call<Void>

        @DELETE("/pet/{petId}")
        fun deletePet(
             api_key: String,
             petId: Int
        ): Call<Void>
}
```
</details>

<details>
    <summary>/pet/dto/Pet.kt</summary>

```dart
package io.trzcinski.test.pet.dto

data class Pet(
    val id: Int?,
    val category: Category?,
    val name: String,
    val photoUrls: String,
    val tags: Tag?,
    val status: String?,
)
}

```
</details>
### Java FeingClient
todo
### Kotlin FeingClient
todo
