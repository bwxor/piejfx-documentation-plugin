package com.bwxor.piejfxsdk.dto;

import java.util.Map;

public record SRSDocumentOverallDescription(String productPerspective, Map<String, String> userClassesAndCharacteristics, String operatingEnvironment, String constraints) {
}
