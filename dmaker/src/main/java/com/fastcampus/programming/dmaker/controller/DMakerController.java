package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.service.DMakerService;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

  private final DMakerService dMakerService;

  @GetMapping("/developers")
  public List<String> getAllDevelopers() {
    log.info("GET /developers HTTP/1.1");

    return Arrays.asList("snow", "elsa", "Olaf");
  }

  @PostMapping("/create-developer")
  public CreateDeveloper.Response createDevelopers(
      @Valid @RequestBody CreateDeveloper.Request request) {
    log.info("request : {}", request);

    return dMakerService.createDeveloper(request);
  }
}
