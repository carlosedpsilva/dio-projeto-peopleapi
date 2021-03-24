package com.vaaaarlos.peopleapi.service;

import static com.vaaaarlos.peopleapi.utils.PersonUtils.createFakeDTO;
import static com.vaaaarlos.peopleapi.utils.PersonUtils.createFakeEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.vaaaarlos.peopleapi.dto.MessageResponseDTO;
import com.vaaaarlos.peopleapi.dto.PersonDTO;
import com.vaaaarlos.peopleapi.entity.Person;
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

  private MessageResponseDTO createExpectedMessageResponse(Long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }

}
