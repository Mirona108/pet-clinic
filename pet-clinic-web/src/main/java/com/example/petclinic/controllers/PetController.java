package com.example.petclinic.controllers;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import com.example.petclinic.services.OwnerService;
import com.example.petclinic.services.PetService;
import com.example.petclinic.services.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

  private final PetService petService;
  private final OwnerService ownerService;
  private final PetTypeService petTypeService;

  public PetController(PetService petService, OwnerService ownerService, PetTypeService petTypeService) {
    this.petService = petService;
    this.ownerService = ownerService;
    this.petTypeService = petTypeService;
  }

  @ModelAttribute("types")
  public Collection<PetType> populatePetTypes(){
    Collection<PetType> types = this.petTypeService.findAll();
    return this.petTypeService.findAll();
  }

  @ModelAttribute("owner")
  public Owner findOwner(@PathVariable Long ownerId){
    return ownerService.findById(ownerId);
  }

  @InitBinder("owner")
  public void initOwnerBinder(WebDataBinder dataBinder){
    dataBinder.setDisallowedFields("id");
  }

  @GetMapping("/pets/new")
  public String initCreationForm(Owner owner, Model model){
    Pet pet = new Pet();
    owner.getPets().add(pet);
    pet.setOwner(owner);
    model.addAttribute("pet", pet);
    return "pets/createOrUpdatePetForm";
  }

  @PostMapping("/pets/new")
  public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
    if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null){
      result.rejectValue("name", "duplicate", "already exists");
    }
    owner.getPets().add(pet);
    if (result.hasErrors()) {
      model.addAttribute("pet", pet);
      return "pets/createOrUpdatePetForm";
    } else {
      petService.save(pet);
      return "redirect:/owners/" + owner.getId();
    }
  }

  @GetMapping("/pets/{petId}/edit")
  public String initUpdateForm(@PathVariable Long petId, Model model) {
    model.addAttribute("pet", petService.findById(petId));
    return "pets/createOrUpdatePetForm";
  }

  @PostMapping("/pets/{petId}/edit")
  public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, Model model) {
    if (result.hasErrors()) {
      pet.setOwner(owner);
      model.addAttribute("pet", pet);
      return "pets/createOrUpdatePetForm";
    } else {
      owner.getPets().add(pet);
      petService.save(pet);
      return "redirect:/owners/" + owner.getId();
    }
  }
}
