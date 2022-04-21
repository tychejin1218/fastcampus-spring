package com.fastcampus.programming.dmaker.service;

import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.NO_DEVELOPER;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.dto.EditDeveloper.Request;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.entity.RetiredDeveloper;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DMakerService {

  private final DeveloperRepository developerRepository;
  private final RetiredDeveloperRepository retiredDeveloperRepository;

  @Transactional
  public CreateDeveloper.Response createDeveloper(
      CreateDeveloper.Request request) {

    validateCreateDeveloperRequest(request);

    return CreateDeveloper.Response.fromEntity(
        developerRepository.save(createDeveloperFromRequest(request))
    );
  }

  public Developer createDeveloperFromRequest(CreateDeveloper.Request request) {
    return Developer.builder()
        .developerLevel(request.getDeveloperLevel())
        .developerSkillType(request.getDeveloperSkillType())
        .experienceYears(request.getExperienceYears())
        .memberId(request.getMemberId())
        .statusCode(StatusCode.EMPLOYED)
        .name(request.getName())
        .age(request.getAge())
        .build();
  }

  private void validateCreateDeveloperRequest(
      @NonNull CreateDeveloper.Request request) {

    request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());

    developerRepository.findByMemberId(request.getMemberId())
        .ifPresent((developer -> {
          throw new DMakerException(DUPLICATED_MEMBER_ID);
        }));

    //throw new ArrayIndexOutOfBoundsException();
  }

  @Transactional(readOnly = true)
  public List<DeveloperDto> getAllEmployedDevelopers() {
    return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
        .stream().map(DeveloperDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public DeveloperDetailDto getDeveloperDetail(String memberId) {
    return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
  }

  private Developer getDeveloperByMemberId(String memberId) {
    return developerRepository.findByMemberId(memberId)
        .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
  }

  @Transactional
  public DeveloperDetailDto editDeveloper(
      String memberId,
      EditDeveloper.Request request) {

    request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());

    return DeveloperDetailDto.fromEntity(
        getUpdatedDeveloperFromRequest(request, getDeveloperByMemberId(memberId))
    );
  }

  private Developer getUpdatedDeveloperFromRequest(Request request, Developer developer) {
    developer.setDeveloperLevel(request.getDeveloperLevel());
    developer.setDeveloperSkillType(request.getDeveloperSkillType());
    developer.setExperienceYears(request.getExperienceYears());

    return developer;
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
