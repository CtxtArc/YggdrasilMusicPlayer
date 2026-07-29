package com.example.yggdrasil;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "current_queue")
public class QueueItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String trackPath;
    private int displayOrder;

    public QueueItem(@NonNull String trackPath, int displayOrder) {
        this.trackPath = trackPath;
        this.displayOrder = displayOrder;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTrackPath() { return trackPath; }
    public void setTrackPath(@NonNull String trackPath) { this.trackPath = trackPath; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
