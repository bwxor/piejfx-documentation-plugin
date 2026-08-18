package com.bwxor.piejfxsdk.dto;

import java.util.List;

/**
 * Full Release Notes document model.
 */
public record ReleaseNotesDocument(
        String productName,
        String version,
        String releaseDate,
        String releasedBy,
        String summary,
        List<ReleaseNotesEntry> newFeatures,
        List<ReleaseNotesEntry> bugFixes,
        List<ReleaseNotesEntry> knownIssues,
        String migrationNotes
) {
}
