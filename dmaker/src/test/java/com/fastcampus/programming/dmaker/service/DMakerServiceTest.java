package com.fastcampus.programming.dmaker.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
          .developerSkillType(DeveloperSkillType.BACK_END)
          .experienceYears(2)
          .memberId("Micky")
          .name("mouse")
          .age(30)
          .build();

  private final CreateDeveloper.Request defaultCreateRequest =
      CreateDeveloper.Request.builder()
          .developerLevel(DeveloperLevel.SENIOR)
          .developerSkillType(DeveloperSkillType.BACK_END)
          .experienceYears(12)
          .memberId("Micky")
          .name("mouse")
          .age(40)
          .build();

  @Mock
  private DeveloperRepository developerRepository;

  @InjectMocks
  private DMakerService dMakerService;

  @Test
  public void testSomething() {

    given(developerRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(defaultDeveloper));

    DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

    assertEquals(DeveloperLevel.JUNIOR, developerDetail.getDeveloperLevel());
    assertEquals(DeveloperSkillType.BACK_END, developerDetail.getDeveloperSkillType());
    assertEquals(2, developerDetail.getExperienceYears());
  }

  @Test
  void createDeveloperTest_success() {
    //given
    given(developerRepository.findByMemberId(anyString()))
        .willReturn(Optional.empty());

    ArgumentCaptor<Developer> captor =
        ArgumentCaptor.forClass(Developer.class);

    //when
    CreateDeveloper.Response developer = dMakerService.createDeveloper(
        defaultCreateRequest);

    //then
    verify(developerRepository, times(1))
        .save(captor.capture());
    Developer saveDeveloper = captor.getValue();
    assertEquals(DeveloperLevel.SENIOR, saveDeveloper.getDeveloperLevel());
    assertEquals(DeveloperSkillType.BACK_END, saveDeveloper.getDeveloperSkillType());
    assertEquals(12, saveDeveloper.getExperienceYears());
  }

  @Test
  void createDeveloperTest_failed_with_duplicated() {
    //given
    given(developerRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(defaultDeveloper));

    //when
    //then
    DMakerException dMakerException = assertThrows(DMakerException.class,
        () -> dMakerService.createDeveloper(defaultCreateRequest)
    );

    assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
  }
}