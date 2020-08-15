package com.example.task0723;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public class MusicData {
//    private int mp3Picture;
//    private String mp3FileName;
//
//    public MusicData(int mp3Picture, String mp3FileName) {
//        this.mp3Picture = mp3Picture;
//        this.mp3FileName = mp3FileName;
//
//    }
//
//    public MusicData(int mp3Picture) {
//        this.mp3Picture = mp3Picture;
//    }
//
//    public MusicData(String mp3FileName) {
//        this.mp3FileName = mp3FileName;
//    }
//
//    public int getMp3Picture() {
//        return mp3Picture;
//    }
//
//    public void setMp3Picture(int mp3Picture) {
//        this.mp3Picture = mp3Picture;
//    }
//
//    public String getMp3FileName() {
//        return mp3FileName;
//    }
//
//    public void setMp3FileName(String mp3FileName) {
//        this.mp3FileName = mp3FileName;
//    }

    private Bitmap mp3Picture;
    private String mp3FileName;
    private String singer;
    private String fullName;

    public MusicData(@Nullable Bitmap mp3Picture, String mp3FileName, String singer, String fullName) {
        this.mp3Picture = mp3Picture;
        this.mp3FileName = mp3FileName;
        this.singer = singer;
        this.fullName = fullName;
    }

    public Bitmap getMp3Picture() {
        return mp3Picture;
    }

    public void setMp3Picture(Bitmap mp3Picture) {
        this.mp3Picture = mp3Picture;
    }

    public String getMp3FileName() {
        return mp3FileName;
    }

    public void setMp3FileName(String mp3FileName) {
        this.mp3FileName = mp3FileName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
