package org.murilonerdx.dynamodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {
	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.builder()
				.endpointOverride(URI.create("http://localhost:4566"))
				.region(Region.SA_EAST_1)
				.credentialsProvider(StaticCredentialsProvider.create(
						AwsBasicCredentials.create("test", "test")
				))
				.build();

	}
}
