package com.bwxor.piejfxsdk.dto;

public record CommitLogEntry(String hash, String fullHash, String author, String date, String message) {
}
