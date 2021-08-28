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