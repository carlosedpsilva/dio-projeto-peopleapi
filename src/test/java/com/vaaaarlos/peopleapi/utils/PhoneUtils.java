package com.vaaaarlos.peopleapi.utils;

import com.vaaaarlos.peopleapi.dto.PhoneDTO;
import com.vaaaarlos.peopleapi.entity.Phone;
import com.vaaaarlos.peopleapi.enums.PhoneType;

public class PhoneUtils {

  private static final String PHONE_NUMBER = "(11)99999-9999";
  private static final PhoneType PHONE_TYPE = PhoneType.MOBILE;
  private static final Long PHONE_ID = 1L;

  public static Phone createFakeEntity() {
    return Phone.builder()
      .id(PHONE_ID)
      .number(PHONE_NUMBER)
      .type(PHONE_TYPE)
      .build();
  }

  public static PhoneDTO createFakeDTO() {
    return PhoneDTO.builder()
      .id(PHONE_ID)
      .number(PHONE_NUMBER)
      .type(PHONE_TYPE)
      .build();
  }

}
