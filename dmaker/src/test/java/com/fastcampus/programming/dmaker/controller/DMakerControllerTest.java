package com.fastcampus.programming.dmaker.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.service.DMakerService;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {

  protected MediaType contentType =
      new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          StandardCharsets.UTF_8);

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DMakerService dMakerService;

  @Test
  void getAllDevelopers() throws Exception {

    DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
        .developerLevel(DeveloperLevel.JUNIOR)
        .developerSkillType(DeveloperSkillType.FRONT_END)
        .memberId("memberId01")
        .build();

    DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
        .developerLevel(DeveloperLevel.SENIOR)
        .developerSkillType(DeveloperSkillType.BACK_END)
        .memberId("memberId02")
        .build();

    given(dMakerService.getAllEmployedDevelopers())
        .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

    mockMvc.perform(get("/developers").contentType(contentType))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(
            jsonPath("$.[0].developerLevel",
                is(DeveloperLevel.JUNIOR.name())))
        .andExpect(
            jsonPath("$.[0].developerSkillType",
                is(DeveloperSkillType.FRONT_END.name())))
        .andExpect(
            jsonPath("$.[1].developerLevel",
                is(DeveloperLevel.SENIOR.name())))
        .andExpect(
            jsonPath("$.[1].developerSkillType",
                is(DeveloperSkillType.BACK_END.name())));
  }
}