package com.bwxor.piejfxsdk.dto;

/**
 * An item listed under an affected component in a Change Request.
 */
public record ChangeRequestImpactItem(String component, String description) {
}
