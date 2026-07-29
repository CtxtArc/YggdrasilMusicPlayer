package com.example.yggdrasil;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "playlist_tracks",
    primaryKeys = {"playlistId", "trackPath"},
    foreignKeys = {
        @ForeignKey(
            entity = Playlist.class,
            parentColumns = "id",
            childColumns = "playlistId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = MusicTrack.class,
            parentColumns = "filePath",
            childColumns = "trackPath",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index("trackPath")}
)
public class PlaylistTrack {
    public long playlistId;
    @NonNull
    public String trackPath;
    public int displayOrder;

    public PlaylistTrack(long playlistId, @NonNull String trackPath, int displayOrder) {
        this.playlistId = playlistId;
        this.trackPath = trackPath;
        this.displayOrder = displayOrder;
    }
}