package com.bwxor.piejfxsdk.dto;

import com.bwxor.piejfxsdk.type.Status;

public record SRSDocumentTitlePage(String projectName, String documentVersion, String date, String authors, Status status) {
}
