package com.coel.codyn.fragment.file;

import androidx.annotation.NonNull;

public class FileProgress {
    @NonNull
    private String displayName;

    private Integer progress;

    FileProgress(String displayName) {
        progress = 0;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
