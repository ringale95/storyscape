package edu.neu.csye6200.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.csye6200.dto.StoryDTO;

@RestController
@RequestMapping("/stories")
public class StoryController {

    @PostMapping()
    public ResponseEntity<String> createStory(@RequestBody StoryDTO storyDTO) {
        return ResponseEntity.ok("Story created with title: " + storyDTO.getTitle());
    }
}
