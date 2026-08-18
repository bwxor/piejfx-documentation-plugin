package com.bwxor.piejfxsdk.dto;

/**
 * A single change entry (feature, fix, or known issue) within a release.
 */
public record ReleaseNotesEntry(String category, String description) {
}
