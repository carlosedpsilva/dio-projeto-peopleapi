package com.vaaaarlos.peopleapi.utils;

import java.time.LocalDate;
import java.util.Collections;

import com.vaaaarlos.peopleapi.dto.PersonDTO;
import com.vaaaarlos.peopleapi.entity.Person;

public class PersonUtils {
  
  private static final Long PERSON_ID = 1L;
  private static final String FIRST_NAME = "João";
  private static final String LAST_NAME = "Carlos";
  private static final String CPF_NUMBER = "573.514.250-00";
  public static final LocalDate BIRTH_DATE = LocalDate.of(2010, 4, 4);

  public static Person createFakeEntity() {
    return Person.builder()
      .id(PERSON_ID)
      .firstName(FIRST_NAME)
      .lastName(LAST_NAME)
      .cpf(CPF_NUMBER)
      .birthDate(BIRTH_DATE)
      .phones(Collections.singletonList(PhoneUtils.createFakeEntity()))
      .build();
  }  

  public static PersonDTO createFakeDTO() {
    return PersonDTO.builder()
      .firstName(FIRST_NAME)
      .lastName(LAST_NAME)
      .cpf(CPF_NUMBER)
      .birthDate("04/04/2010")
      .phones(Collections.singletonList(PhoneUtils.createFakeDTO()))
      .build();
  }
}
