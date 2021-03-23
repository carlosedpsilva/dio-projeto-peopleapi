package com.vaaaarlos.peopleapi.service;

import java.util.List;
import java.util.stream.Collectors;

import com.vaaaarlos.peopleapi.dto.MessageResponseDTO;
import com.vaaaarlos.peopleapi.dto.PersonDTO;
import com.vaaaarlos.peopleapi.entity.Person;
import com.vaaaarlos.peopleapi.exception.PersonNotFoundException;
import com.vaaaarlos.peopleapi.mapper.PersonMapper;
import com.vaaaarlos.peopleapi.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private PersonMapper personMapper;

  public MessageResponseDTO createPerson(PersonDTO personDTO) {
    Person personToSave = personMapper.toModel(personDTO);
    Person savedPerson = personRepository.save(personToSave);
    return createMessageResponse(savedPerson.getId(), "Created person with ID ");
  }

  public List<PersonDTO> listAll() {
    List<Person> allPeople = personRepository.findAll();
    return allPeople.stream().map(personMapper::toDTO).collect(Collectors.toList());
  }

  public PersonDTO findById(Long id) throws PersonNotFoundException {
    Person person = verifyIfExists(id);
    return personMapper.toDTO(person);
  }
  
  public void deleteById(Long id) throws PersonNotFoundException {
    verifyIfExists(id);
    personRepository.deleteById(id);
  }
  
  public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
    verifyIfExists(id);
    Person personToUpdate = personMapper.toModel(personDTO);
    Person updatedPerson = personRepository.save(personToUpdate); 
    return createMessageResponse(updatedPerson.getId(), "Updated person with ID ");
  }

  private Person verifyIfExists(Long id) throws PersonNotFoundException {
    return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));    
  }

  private MessageResponseDTO createMessageResponse(Long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }

}
