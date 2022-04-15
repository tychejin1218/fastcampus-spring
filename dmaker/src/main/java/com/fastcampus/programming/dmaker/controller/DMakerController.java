package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.service.DMakerService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

  private final DMakerService dMakerService;

  @GetMapping("/developers")
  public List<DeveloperDto> getAllDevelopers() {
    log.info("GET /developers HTTP/1.1");

    return dMakerService.getAllEmployedDevelopers();
  }

  @GetMapping("/developer/{memberId}")
  public DeveloperDetailDto getDeveloperDetail(
      @PathVariable String memberId) {

    return dMakerService.getDeveloperDetail(memberId);
  }

  @PostMapping("/create-developer")
  public CreateDeveloper.Response createDevelopers(
      @Valid @RequestBody CreateDeveloper.Request request) {
    log.info("request : {}", request);

    return dMakerService.createDeveloper(request);
  }

  @PutMapping("/developer/{memberId}")
  public DeveloperDetailDto editDeveloper(
      @PathVariable String memberId,
      @Valid @RequestBody EditDeveloper.Request request) {

    return dMakerService.editDeveloper(memberId, request);
  }

  @DeleteMapping("/developer/{memberId}")
  public DeveloperDetailDto deleteDeveloperDetail(
      @PathVariable String memberId) {

    return dMakerService.deleteDeveloper(memberId);
  }
}
