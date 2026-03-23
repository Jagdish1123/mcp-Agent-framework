package com.example.genericagent.ai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

@Component
public class BedrockAiExtractor {

    private final BedrockRuntimeClient client;
    private final String modelId;

    public BedrockAiExtractor(
            @Value("${spring.ai.bedrock.region}") String region,
            @Value("${spring.ai.bedrock.model-id}") String modelId
    ) {
        this.modelId = modelId;
        this.client = BedrockRuntimeClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public String invoke(String prompt) {

        // Build JSON payload dynamically
        JsonObject payload = new JsonObject();
        payload.addProperty("max_tokens", 1024);
        payload.addProperty("temperature", 0.1);
        payload.addProperty("anthropic_version", "bedrock-2023-05-31");

        JsonArray messages = new JsonArray();
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);

        payload.add("messages", messages);

        // Build request
        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(modelId)
                .body(SdkBytes.fromUtf8String(payload.toString()))
                .build();

        // Invoke Bedrock and return response
        InvokeModelResponse response = client.invokeModel(request);
        return response.body().asUtf8String();
    }
}