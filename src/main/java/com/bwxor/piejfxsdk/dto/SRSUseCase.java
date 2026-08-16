package com.bwxor.piejfxsdk.dto;

public record SRSUseCase(String id, String actor, String preconditions, String mainFlow, String alternateFlow, String postConditions) {
}
