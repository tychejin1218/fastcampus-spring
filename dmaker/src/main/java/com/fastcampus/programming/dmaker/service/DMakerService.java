package com.fastcampus.programming.dmaker.service;

import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.NO_DEVELOPER;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.entity.RetiredDeveloper;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DMakerService {

  private final DeveloperRepository developerRepository;
  private final RetiredDeveloperRepository retiredDeveloperRepository;

  @Transactional
  public CreateDeveloper.Response createDeveloper(
      CreateDeveloper.Request request) {

    validateCreateDeveloperRequest(request);

    Developer developer = Developer.builder()
        .developerLevel(request.getDeveloperLevel())
        .developerSkillType(request.getDeveloperSkillType())
        .experienceYears(request.getExperienceYears())
        .memberId(request.getMemberId())
        .statusCode(StatusCode.EMPLOYED)
        .name(request.getName())
        .age(request.getAge())
        .build();

    developerRepository.save(developer);

    return CreateDeveloper.Response.fromEntity(developer);
  }

  private void validateCreateDeveloperRequest(
      CreateDeveloper.Request request) {

    validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

    developerRepository.findByMemberId(request.getMemberId())
        .ifPresent((developer -> {
          throw new DMakerException(DUPLICATED_MEMBER_ID);
        }));
  }

  public List<DeveloperDto> getAllEmployedDevelopers() {
    return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
        .stream().map(DeveloperDto::fromEntity)
        .collect(Collectors.toList());
  }

  public DeveloperDetailDto getDeveloperDetail(String memberId) {
    return developerRepository.findByMemberId(memberId)
        .map(DeveloperDetailDto::fromEntity)
        .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
  }

  public DeveloperDetailDto editDeveloper(
      String memberId,
      EditDeveloper.Request request) {

    validateEditDeveloperRequest(request);

    Developer developer = developerRepository.findByMemberId(memberId)
        .orElseThrow(() -> new DMakerException(NO_DEVELOPER));

    developer.setDeveloperLevel(request.getDeveloperLevel());
    developer.setDeveloperSkillType(request.getDeveloperSkillType());
    developer.setExperienceYears(request.getExperienceYears());

    return DeveloperDetailDto.fromEntity(developer);
  }

  private void validateEditDeveloperRequest(
      EditDeveloper.Request request) {
    validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
  }

  private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
    if (developerLevel == DeveloperLevel.SENIOR
        && experienceYears < 10) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
    if (developerLevel == DeveloperLevel.JUNGNIOR
        && (experienceYears < 4 || experienceYears > 10)) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
    if (developerLevel == DeveloperLevel.JUNIOR
        && experienceYears > 4) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
  }

  @Transactional
  public DeveloperDetailDto deleteDeveloper(String memberId) {
    // 1.EMPLOYED -> RETIRED
    Developer developer = developerRepository.findByMemberId(memberId)
        .orElseThrow(() -> new DMakerException(NO_DEVELOPER));

    developer.setStatusCode(StatusCode.RETIRED);

    // 2. save into RetiredDeveloper
    RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
        .memberId(memberId)
        .name(developer.getName())
        .build();

    retiredDeveloperRepository.save(retiredDeveloper);

    return DeveloperDetailDto.fromEntity(developer);
  }
}
