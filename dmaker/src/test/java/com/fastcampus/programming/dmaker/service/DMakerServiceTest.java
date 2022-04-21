package com.fastcampus.programming.dmaker.service;


import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;
import static com.fastcampus.programming.dmaker.type.DeveloperLevel.JUNGNIOR;
import static com.fastcampus.programming.dmaker.type.DeveloperLevel.JUNIOR;
import static com.fastcampus.programming.dmaker.type.DeveloperLevel.SENIOR;
import static com.fastcampus.programming.dmaker.type.DeveloperSkillType.BACK_END;
import static com.fastcampus.programming.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*@SpringBootTest
class DMakerServiceTest {

  @Autowired
  private DMakerService dMakerService;

  @Test
  public void testSomething() {

    dMakerService.createDeveloper(CreateDeveloper.Request.builder()
        .developerLevel(DeveloperLevel.JUNIOR)
        .developerSkillType(DeveloperSkillType.BACK_END)
        .experienceYears(2)
        .memberId("Micky")
        .name("mouse")
        .age(30)
        .build());

    List<DeveloperDto> allEmployedDevelopers = dMakerService.getAllEmployedDevelopers();
    System.out.println("===== ===== ===== ===== ===== ===== ===== ===== ===== =====");
    System.out.println(allEmployedDevelopers);
    System.out.println("===== ===== ===== ===== ===== ===== ===== ===== ===== =====");
  }
}*/

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

  private final Developer defaultDeveloper =
      Developer.builder()
          .developerLevel(DeveloperLevel.JUNIOR)
          .developerSkillType(BACK_END)
          .experienceYears(2)
          .memberId("Micky")
          .name("mouse")
          .age(30)
          .build();
  @Mock
  private DeveloperRepository developerRepository;
  @InjectMocks
  private DMakerService dMakerService;

  private CreateDeveloper.Request getCreateRequest(
      DeveloperLevel developerLevel,
      DeveloperSkillType developerSkillType,
      Integer experienceYears
  ) {

    return CreateDeveloper.Request.builder()
        .developerLevel(developerLevel)
        .developerSkillType(developerSkillType)
        .experienceYears(experienceYears)
        .memberId("Micky")
        .name("mouse")
        .age(40)
        .build();
  }

  @Test
  public void testSomething() {

    given(developerRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(defaultDeveloper));

    DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

    assertEquals(DeveloperLevel.JUNIOR, developerDetail.getDeveloperLevel());
    assertEquals(BACK_END, developerDetail.getDeveloperSkillType());
    assertEquals(2, developerDetail.getExperienceYears());
  }

  @Test
  void createDeveloperTest_success() {
    //given
    given(developerRepository.findByMemberId(anyString()))
        .willReturn(Optional.empty());
    given(developerRepository.save(any()))
        .willReturn(defaultDeveloper);

    ArgumentCaptor<Developer> captor =
        ArgumentCaptor.forClass(Developer.class);

    //when
    CreateDeveloper.Response developer = dMakerService.createDeveloper(
        getCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS));

    //then
    verify(developerRepository, times(1))
        .save(captor.capture());
    Developer saveDeveloper = captor.getValue();
    assertEquals(SENIOR, saveDeveloper.getDeveloperLevel());
    assertEquals(FRONT_END, saveDeveloper.getDeveloperSkillType());
    assertEquals(MIN_SENIOR_EXPERIENCE_YEARS, saveDeveloper.getExperienceYears());
  }

  @Test
  void createDeveloperTest_failed_with_duplicated() {
    //given
    given(developerRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(defaultDeveloper));

    //when
    //then
    DMakerException dMakerException = assertThrows(DMakerException.class,
        () -> dMakerService.createDeveloper(
            getCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS))
    );

    assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
  }

  @Test
  void createDeveloperTest_fail_with_unmatched_level() {
    //given
    //when
    DMakerException dMakerException = assertThrows(DMakerException.class,
        () -> dMakerService.createDeveloper(
            getCreateRequest(JUNIOR, FRONT_END, MAX_JUNIOR_EXPERIENCE_YEARS + 1))
    );

    //then
    assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

    //when
    dMakerException = assertThrows(DMakerException.class,
        () -> dMakerService.createDeveloper(
            getCreateRequest(JUNGNIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 1))
    );

    //then
    assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

    //when
    dMakerException = assertThrows(DMakerException.class,
        () -> dMakerService.createDeveloper(
            getCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS - 1))
    );

    //then
    assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());
  }
}