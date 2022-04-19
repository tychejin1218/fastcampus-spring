package com.fastcampus.programming.dmaker.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  @Mock
  private DeveloperRepository developerRepository;

  @Mock
  private RetiredDeveloperRepository retiredDeveloperRepository;

  @InjectMocks
  private DMakerService dMakerService;

  @Test
  public void testSomething() {

    given(developerRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(Developer.builder()
            .developerLevel(DeveloperLevel.JUNIOR)
            .developerSkillType(DeveloperSkillType.BACK_END)
            .experienceYears(2)
            .memberId("Micky")
            .name("mouse")
            .age(30)
            .build()));

    DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

    assertEquals(DeveloperLevel.JUNIOR, developerDetail.getDeveloperLevel());
    assertEquals(DeveloperSkillType.BACK_END, developerDetail.getDeveloperSkillType());
    assertEquals(2, developerDetail.getExperienceYears());
  }
}