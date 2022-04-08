package com.fastcampus.programming.dmaker.controller;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DMakerController {

  @GetMapping("/developers")
  public List<String> getAllDevelopers() {
    log.info("GET /developers HTTP/1.1");

    return Arrays.asList("snow", "elsa", "Olaf");
  }
}
