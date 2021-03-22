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
    return MessageResponseDTO.builder().message("Created person with ID " + savedPerson.getId()).build();
  }

  public List<PersonDTO> listAll() {
    List<Person> allPeople = personRepository.findAll();
    return allPeople.stream().map(personMapper::toDTO).collect(Collectors.toList());
  }

  public PersonDTO findById(Long id) throws PersonNotFoundException {
    Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    return personMapper.toDTO(person);
  }

}
