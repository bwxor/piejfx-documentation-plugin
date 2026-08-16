package com.bwxor.piejfxsdk.dto;

import java.util.List;
import java.util.Map;

public record SRSDocumentAppendices(Map<String, String> glossary, List<String> openIssues) {
}
