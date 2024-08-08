package org.murilonerdx.dynamodb.controller;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.murilonerdx.dynamodb.entity.PlayerHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {
	private final DynamoDbTemplate dynamoDbTemplate;

	public PlayerController(DynamoDbTemplate dynamoDbTemplate) {
		this.dynamoDbTemplate = dynamoDbTemplate;
	}

	@PostMapping("/{username}/games")
	public ResponseEntity<Void> save(@PathVariable("username") String username,
									 @RequestBody ScoreDto score) {

		PlayerHistory playerHistory = PlayerHistory.fromScore(username, score);
		dynamoDbTemplate.save(playerHistory);

		return ResponseEntity.ok().build();
	}
}
