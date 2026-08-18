package com.bwxor.piejfxsdk.dto;

import java.util.List;

/**
 * Full Change Request document model.
 */
public record ChangeRequestDocument(
        String changeId,
        String title,
        String requestedBy,
        String requestDate,
        String targetVersion,
        String priority,
        String status,
        String description,
        String justification,
        List<ChangeRequestImpactItem> impactedComponents,
        String testingStrategy,
        String rollbackPlan,
        String approvedBy,
        String approvalDate
) {
}
