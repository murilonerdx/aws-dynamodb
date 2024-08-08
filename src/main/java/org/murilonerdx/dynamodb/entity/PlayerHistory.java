package org.murilonerdx.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
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


	@DynamoDbAttribute("game_id")
	public Double getScore() {
		return score;
	}

	@DynamoDbAttribute("created_at")
	public Instant getCreatedAt() {
		return createdAt;
	}
}
