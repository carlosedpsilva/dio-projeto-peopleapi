package com.vaaaarlos.peopleapi.service;

import com.vaaaarlos.peopleapi.dto.MessageResponseDTO;
import com.vaaaarlos.peopleapi.dto.PersonDTO;
import com.vaaaarlos.peopleapi.entity.Person;
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

}
