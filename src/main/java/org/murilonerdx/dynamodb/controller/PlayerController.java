package org.murilonerdx.dynamodb.controller;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.murilonerdx.dynamodb.entity.PlayerHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

	@GetMapping("/{username}/games")
	public ResponseEntity<List<PlayerHistory>> findUsername(@PathVariable("username") String username) {
		Key key = Key
				.builder()
				.partitionValue(username)
				.build();


		QueryConditional condition = QueryConditional.keyEqualTo(key);

		QueryEnhancedRequest build =
				QueryEnhancedRequest
						.builder()
						.queryConditional(condition)
						.build();

		PageIterable<PlayerHistory> history = dynamoDbTemplate.query(build, PlayerHistory.class);

		return ResponseEntity.ok(history.items().stream().collect(Collectors.toList()));
	}


	@GetMapping("/{username}/games/{gameId}")
	public ResponseEntity<PlayerHistory> findById(@PathVariable("username") String username, @PathVariable("gameId") String gameId) {
		Key key = Key
				.builder()
				.partitionValue(username)
				.sortValue(gameId)
				.build();

		Optional<PlayerHistory> load = Optional.ofNullable(dynamoDbTemplate.load(key, PlayerHistory.class));

		return (load.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()));
	}

	@DeleteMapping("/{playerId}/games/{gameId}")
	public ResponseEntity<Void> delete(@PathVariable("playerId") String playerId,
									   @PathVariable("gameId") String gameId) {
		var key = Key.builder()
				.partitionValue(playerId)
				.sortValue(gameId)
				.build();

		var player = Optional.ofNullable(dynamoDbTemplate.load(key, PlayerHistory.class));

		if (player.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		dynamoDbTemplate.delete(key, PlayerHistory.class);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{playerId}/games/{gameId}")
	public ResponseEntity<Void> update(@PathVariable("playerId") String playerId,
									   @PathVariable("gameId") String gameId,
									   @RequestBody ScoreDto scoreDto) {
		var key = Key.builder()
				.partitionValue(playerId)
				.sortValue(gameId)
				.build();

		var player = Optional.ofNullable(dynamoDbTemplate.load(key, PlayerHistory.class));

		if (player.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		player.get().setScore(scoreDto.score());

		dynamoDbTemplate.save(player);

		return ResponseEntity.noContent().build();
	}
}
