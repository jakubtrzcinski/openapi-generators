# OpenApi Generators

**THIS IS EARLY PREVIEW OF THE GENERATOR, not all features are yet implemented!**

An alternative for standard swagger generator that provides you 
an elegant code with various customizations with [Apache Velocity](https://github.com/apache/velocity-engine) templates.

## Supported Languages and Frameworks

### Java
* FeingClient

### Kotlin
* Retrofit

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

import 'dto/pet.dart';
import '../../commons/dto/api_response.dart';

part 'pet_client.g.dart';
@RestApi()
abstract class  PetClient {
  factory PetClient(Dio dio, {String baseUrl}) = _PetClient;

  @POST("/pet/{petId}/uploadImage")
  Future<ApiResponse> uploadFile(
      @Path("petId") int petId
  );

  @PUT("/pet")
  Future<void> updatePet(
  );

  @POST("/pet")
  Future<void> addPet(
  );

  @GET("/pet/findByStatus")
  Future<List<Pet>> findPetsByStatus(
      @Query("status") List<String> status
  );

  @GET("/pet/findByTags")
  Future<List<Pet>> findPetsByTags(
      @Query("tags") List<String> tags
  );

  @GET("/pet/{petId}")
  Future<Pet> getPetById(
      @Path("petId") int petId
  );

  @POST("/pet/{petId}")
  Future<void> updatePetWithForm(
      @Path("petId") int petId
  );

  @DELETE("/pet/{petId}")
  Future<void> deletePet(
      @Header("api_key") String api_key,
      @Path("petId") int petId
  );
}
```
</details>

<details>
    <summary>/pet/model/pet.dart</summary>

```dart
import 'package:json_annotation/json_annotation.dart';

import '../../commons/dto/category.dart';
import '../../commons/dto/tag.dart';
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

```kotlin
package out.io.trzcinski.test.pet

import retrofit2.http.*
import retrofit2.Call

import out.io.trzcinski.test.pet.dto.Pet
import out.io.trzcinski.test.commons.dto.ApiResponse

interface PetClient {

    @POST("/pet/{petId}/uploadImage")
    fun uploadFile(
        @Path("petId") petId: Int
    ): Call<ApiResponse>

    @PUT("/pet")
    fun updatePet(
        @Body() payload: Pet
    ): Call<Void>

    @POST("/pet")
    fun addPet(
        @Body() payload: Pet
    ): Call<Void>

    @GET("/pet/findByStatus")
    fun findPetsByStatus(
        @Query("status") status: List<String>
    ): Call<List<Pet>>

    @GET("/pet/findByTags")
    fun findPetsByTags(
        @Query("tags") tags: List<String>
    ): Call<List<Pet>>

    @GET("/pet/{petId}")
    fun getPetById(
        @Path("petId") petId: Int
    ): Call<Pet>

    @POST("/pet/{petId}")
    fun updatePetWithForm(
        @Path("petId") petId: Int
    ): Call<Void>

    @DELETE("/pet/{petId}")
    fun deletePet(
        @Header("api_key") api_key: String,
        @Path("petId") petId: Int
    ): Call<Void>
}
```
</details>

<details>
    <summary>/pet/dto/Pet.kt</summary>

```kotlin
package out.io.trzcinski.test.pet.dto

import out.io.trzcinski.test.commons.dto.Category
import out.io.trzcinski.test.commons.dto.Tag

data class Pet(
    val id: Int?,
    val category: Category?,
    val name: String,
    val photoUrls: List<String>,
    val tags: List<Tag>?,
    val status: String?,
)

```
</details>
    
    
### Java FeingClient
<details>
    <summary>/pet/PetClient.java</summary>

```java
package io.trzcinski.test.pet;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;

import io.trzcinski.test.pet.dto.Pet;
import io.trzcinski.test.commons.dto.ApiResponse;

interface PetClient {

    @PostMapping("/pet/{petId}/uploadImage")
    ApiResponse uploadFile(
        @RequestParam("petId") Integer petId
    );

    @PutMapping("/pet")
    void updatePet(
        @RequestBody() Pet payload
    );

    @PostMapping("/pet")
    void addPet(
        @RequestBody() Pet payload
    );

    @GetMapping("/pet/findByStatus")
    List<Pet> findPetsByStatus(
        @RequestParam("status") List<String> status
    );

    @GetMapping("/pet/findByTags")
    List<Pet> findPetsByTags(
        @RequestParam("tags") List<String> tags
    );

    @GetMapping("/pet/{petId}")
    Pet getPetById(
        @RequestParam("petId") Integer petId
    );

    @PostMapping("/pet/{petId}")
    void updatePetWithForm(
        @RequestParam("petId") Integer petId
    );

    @DeleteMapping("/pet/{petId}")
    void deletePet(
        @RequestHeader("api_key") String api_key,
        @RequestParam("petId") Integer petId
    );
}
```
</details>

<details>
    <summary>/pet/dto/Pet.java</summary>

```java
package io.trzcinski.test.pet.dto;

import io.trzcinski.test.commons.dto.Category;
import io.trzcinski.test.commons.dto.Tag;
import lombok.Data;
import org.springframework.lang.Nullable;
import java.util.List;

@Data
public class Pet{
    @Nullable
    private Integer id;
    @Nullable
    private Category category;
    private String name;
    private List<String> photoUrls;
    @Nullable
    private List<Tag> tags;
    @Nullable
    private String status;
}
```
</details>

### Angular HttpClient
todo
