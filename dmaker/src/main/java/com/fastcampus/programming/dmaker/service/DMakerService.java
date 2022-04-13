package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
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
}
