package com.fastcampus.programming.dmaker.service;

import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DMakerService {

  private final DeveloperRepository developerRepository;

  @Transactional
  public void createDeveloper(
      CreateDeveloper.Request request) {
    Developer developer = Developer.builder()
        .developerLevel(DeveloperLevel.JUNGNIOR)
        .developerSkillType(DeveloperSkillType.FRONT_END)
        .experienceYears(2)
        .name("Olaf")
        .age(5)
        .build();

    developerRepository.save(developer);

  }

  private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {

    DeveloperLevel developerLevel = request.getDeveloperLevel();
    Integer experienceYears = request.getExperienceYears();
    if (developerLevel == DeveloperLevel.SENIOR
        && experienceYears < 10) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
    if (developerLevel == DeveloperLevel.JUNGNIOR
        && experienceYears < 4 || experienceYears > 10) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
    if (developerLevel == DeveloperLevel.JUNGNIOR
        && experienceYears > 4) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }

    developerRepository.findByMemberId(request.getMemberId())
        .ifPresent((developer -> {
          throw new DMakerException(DUPLICATED_MEMBER_ID);
        }));
  }
}
