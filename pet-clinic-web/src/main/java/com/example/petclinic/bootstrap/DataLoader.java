package com.example.petclinic.bootstrap;

import com.example.petclinic.model.*;
import com.example.petclinic.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

  private final OwnerService ownerService;
  private final VetService vetService;
  private final PetTypeService petTypeService;
  private final SpecialityService specialityService;
  private final VisitService visitService;

  @Autowired
  public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService, SpecialityService specialityService, VisitService visitService) {
    this.ownerService = ownerService;
    this.vetService = vetService;
    this.petTypeService = petTypeService;
    this.specialityService = specialityService;
    this.visitService = visitService;
  }

  @Override
  public void run(String... args) throws Exception {
    int count = petTypeService.findAll().size();

    if (count == 0) {
      loadData();
    }
  }

  private void loadData() {
    PetType dog = new PetType();
    dog.setName("Dog");
    PetType savedDogPetType = petTypeService.save(dog);

    PetType cat = new PetType();
    cat.setName("Cat");
    PetType savedCatPetType = petTypeService.save(cat);

    Speciality radiology = new Speciality();
    radiology.setDescription("Radiology");
    Speciality savedRadiology = specialityService.save(radiology);

    Speciality surgery = new Speciality();
    surgery.setDescription("Surgery");
    Speciality savedSurgery = specialityService.save(surgery);

    Speciality dentistry = new Speciality();
    dentistry.setDescription("Dentistry");
    Speciality savedDentistry = specialityService.save(dentistry);

    Owner owner1 = new Owner();
    owner1.setFirstName("Ana");
    owner1.setLastName("Laes");
    owner1.setAddress("Str. Nera");
    owner1.setCity("Resita");
    owner1.setTelephone("124456");

    Pet mikesPet = new Pet();
    mikesPet.setPetType(savedDogPetType);
    mikesPet.setOwner(owner1);
    mikesPet.setBirthDate(LocalDate.now());
    mikesPet.setName("Rosco");
    owner1.getPets().add(mikesPet);
    ownerService.save(owner1);

    Owner owner2 = new Owner();
    owner2.setFirstName("Maria");
    owner2.setLastName("Cantacuzino");
    owner2.setAddress("Str.New");
    owner2.setCity("Timisoara");
    owner2.setTelephone("124456");

    Pet newCat = new Pet();
    newCat.setName("Miti");
    newCat.setBirthDate(LocalDate.now());
    newCat.setOwner(owner2);
    newCat.setPetType(savedCatPetType);
    owner2.getPets().add(newCat);
    ownerService.save(owner2);

    System.out.println("Loaded Owners...");

    Visit catVisit = new Visit();
    catVisit.setPet(newCat);
    catVisit.setDate(LocalDate.now());
    catVisit.setDescription("Sneezy Kitty");

    visitService.save(catVisit);

    Vet vet1 = new Vet();
    vet1.setFirstName("Sam");
    vet1.setLastName("Axe");
    vet1.getSpecialities().add(savedRadiology);
    vetService.save(vet1);

    Vet vet2 = new Vet();
    vet2.setFirstName("Jessie");
    vet2.setLastName("Porter");
    vet2.getSpecialities().add(savedSurgery);
    vetService.save(vet2);

    System.out.println("Loaded Vets...");
  }
}
