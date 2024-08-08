package org.murilonerdx.dynamodb.entity;

import lombok.*;
import org.murilonerdx.dynamodb.controller.ScoreDto;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@DynamoDbBean
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PlayerHistory {
	private String username;
	private UUID gameId;
	private Double score;
	private Instant createdAt;

	@DynamoDbAttribute("username")
	@DynamoDbPartitionKey
	public String getUsername() {
		return username;
	}

	@DynamoDbAttribute("game_id")
	@DynamoDbSortKey
	public UUID getGameId() {
		return gameId;
	}


	@DynamoDbAttribute("score")
	public Double getScore() {
		return score;
	}

	@DynamoDbAttribute("created_at")
	public Instant getCreatedAt() {
		return createdAt;
	}

	public static PlayerHistory fromScore(String username, ScoreDto scoreDTO){
		return PlayerHistory.builder()
				.score(scoreDTO.score())
				.gameId(UUID.randomUUID())
				.createdAt(Instant.now())
				.username(username)
				.build();
	}
}
