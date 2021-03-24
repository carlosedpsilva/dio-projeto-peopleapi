package com.vaaaarlos.peopleapi.service;

import static com.vaaaarlos.peopleapi.utils.PersonUtils.createFakeDTO;
import static com.vaaaarlos.peopleapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.vaaaarlos.peopleapi.dto.MessageResponseDTO;
import com.vaaaarlos.peopleapi.dto.PersonDTO;
import com.vaaaarlos.peopleapi.entity.Person;
import com.vaaaarlos.peopleapi.exception.PersonNotFoundException;
import com.vaaaarlos.peopleapi.mapper.PersonMapper;
import com.vaaaarlos.peopleapi.repository.PersonRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

  @Mock
  private PersonRepository personRepository;

  @Spy
  private PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

  @InjectMocks
  private PersonService personService;

  @Test
  void testGivenPersonDTOThenReturnSavedMessage() {
    PersonDTO personDTO = createFakeDTO();
    Person expectedSavedPerson = createFakeEntity();

    when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);
    
    MessageResponseDTO expectedSuccessMessage = createExpectedMessageResponse(expectedSavedPerson.getId(), "Created person with ID ");
    MessageResponseDTO sucessMessage = personService.createPerson(personDTO);

    Assertions.assertEquals(expectedSuccessMessage, sucessMessage);
  }

  @Test
  void testGivenValidPersonIdThenReturnThisPerson() throws PersonNotFoundException {
    PersonDTO expectedPersonDTO = createFakeDTO();
    Person expectedSavedPerson = createFakeEntity();
    expectedPersonDTO.setId(expectedSavedPerson.getId());

    when(personRepository.findById(expectedSavedPerson.getId())).thenReturn(Optional.of(expectedSavedPerson));
    
    PersonDTO personDTO = personService.findById(expectedSavedPerson.getId());

    assertEquals(expectedPersonDTO, personDTO);
    assertEquals(expectedPersonDTO.getId(), personDTO.getId());
  }

  @Test
  void testGivenInvlaidPersonIdThenThrowException() {
    var invalidId = 1L;
    
    when(personRepository.findById(invalidId)).thenReturn(Optional.ofNullable(any(Person.class)));

    assertThrows(PersonNotFoundException.class, () -> personService.findById(invalidId));
  }

  private MessageResponseDTO createExpectedMessageResponse(Long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }

}
