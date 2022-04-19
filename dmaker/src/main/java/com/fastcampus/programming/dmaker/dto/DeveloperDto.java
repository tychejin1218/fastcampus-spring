package com.fastcampus.programming.dmaker.dto;

import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DeveloperDto {

  private DeveloperLevel developerLevel;
  private DeveloperSkillType developerSkillType;
  private String memberId;

  public static DeveloperDto fromEntity(Developer developer) {
    return DeveloperDto.builder()
        .developerLevel(developer.getDeveloperLevel())
        .developerSkillType(developer.getDeveloperSkillType())
        .memberId(developer.getMemberId())
        .build();
  }
}
