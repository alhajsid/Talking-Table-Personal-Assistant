package com.example.demospeechrecognization.models


class AudioModel {
    internal var aPath: String=""
    internal var aName: String=""
    internal var aAlbum: String=""
    internal var aArtist: String=""
    internal var time: String=""

    fun getaPath(): String {
        return aPath
    }

    fun setabitmap(bitmap: String) {
        this.time = bitmap
    }

    fun setaPath(aPath: String) {
        this.aPath = aPath
    }

    fun getaName(): String {
        return aName
    }

    fun setaName(aName: String) {
        this.aName = aName
    }

    fun getaAlbum(): String {
        return aAlbum
    }

    fun setaAlbum(aAlbum: String) {
        this.aAlbum = aAlbum
    }

    fun getaArtist(): String {
        return aArtist
    }

    fun setaArtist(aArtist: String) {
        this.aArtist = aArtist
    }
}