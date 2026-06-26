package com.bwxor.piejfxsdk.validator;

import java.util.Set;

public final class GitBranchNameValidator {
    private static final Set<String> RESERVED_NAMES = Set.of(
            "HEAD",
            "FETCH_HEAD",
            "ORIG_HEAD",
            "MERGE_HEAD",
            "CHERRY_PICK_HEAD",
            "REBASE_HEAD"
    );

    private GitBranchNameValidator() {
    }

    public static boolean isValid(String branchName) {
        if (branchName == null || branchName.isEmpty()) {
            return false;
        }

        if (branchName.startsWith("remotes/") || branchName.startsWith("origin/")) {
            return false;
        }

        if (RESERVED_NAMES.contains(branchName)) {
            return false;
        }

        if (branchName.startsWith("/")
                || branchName.endsWith("/")
                || branchName.endsWith(".")
                || branchName.endsWith(".lock")) {
            return false;
        }

        if (branchName.contains(" ")
                || branchName.contains("..")
                || branchName.contains("@{")
                || branchName.contains("//")) {
            return false;
        }

        for (int i = 0; i < branchName.length(); i++) {
            char c = branchName.charAt(i);
            if (c < 32 || c == 127) {
                return false;
            }
            if (c == '~' || c == '^' || c == ':' || c == '?' || c == '*' || c == '[' || c == '\\') {
                return false;
            }
        }

        return true;
    }
}
