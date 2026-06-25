package com.bwxor.piejfxsdk.dto;

import com.bwxor.piejfxsdk.type.CloneViewDialogChoice;

public record CloneViewResponse(CloneViewDialogChoice choice, String repoUrl, String destinationUrl) {
}
