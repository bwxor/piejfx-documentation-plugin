package com.bwxor.piejfxsdk.dto;

import java.util.List;
import java.util.Map;

public record SRSDocumentIntroduction(String purpose, String scope, Map<String, String> definitionsAcronymsAbreviations, List<String> references) {
}
