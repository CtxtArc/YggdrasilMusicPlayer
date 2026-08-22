package com.example.yggdrasil;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class BackupManager {
    public static class BackupData {
        public Map<String, Set<String>> trackTags; // filePath -> tags
        public List<PlaylistData> playlists;

        public BackupData() {
            trackTags = new HashMap<>();
            playlists = new ArrayList<>();
        }
    }

    public static class PlaylistData {
        public String name;
        public List<String> trackPaths;

        public PlaylistData(String name) {
            this.name = name;
            this.trackPaths = new ArrayList<>();
        }
    }

    public static void exportBackup(Context context, AppDatabase db) {
        try {
            BackupData data = new BackupData();
            List<MusicTrack> tracks = db.trackDao().getAll();
            for (MusicTrack t : tracks) {
                if (t.getTags() != null && !t.getTags().isEmpty()) {
                    data.trackTags.put(t.getFilePath(), t.getTags());
                }
            }

            List<Playlist> playlists = db.playlistDao().getAllPlaylists();
            for (Playlist p : playlists) {
                PlaylistData pd = new PlaylistData(p.getName());
                List<MusicTrack> playlistTracks = db.playlistDao().getTracksForPlaylist(p.getId());
                for (MusicTrack pt : playlistTracks) {
                    pd.trackPaths.add(pt.getFilePath());
                }
                data.playlists.add(pd);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File backupFile = new File(context.getExternalFilesDir(null), "yggdrasil_backup.json");
            try (FileWriter writer = new FileWriter(backupFile)) {
                gson.toJson(data, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean backupExists(Context context) {
        File backupFile = new File(context.getExternalFilesDir(null), "yggdrasil_backup.json");
        return backupFile.exists();
    }

    public static void importBackup(Context context, AppDatabase db) {
        try {
            File backupFile = new File(context.getExternalFilesDir(null), "yggdrasil_backup.json");
            if (!backupFile.exists()) return;

            Gson gson = new Gson();
            BackupData data;
            try (FileReader reader = new FileReader(backupFile)) {
                data = gson.fromJson(reader, BackupData.class);
            }

            if (data == null) return;

            // Import tags
            for (Map.Entry<String, Set<String>> entry : data.trackTags.entrySet()) {
                MusicTrack track = db.trackDao().getByPath(entry.getKey());
                if (track != null) {
                    track.setTags(entry.getValue());
                    db.trackDao().update(track);
                }
            }

            // Import playlists
            List<Playlist> existingPlaylists = db.playlistDao().getAllPlaylists();
            for (PlaylistData pd : data.playlists) {
                // Check if playlist with same name exists
                boolean exists = false;
                for (Playlist ep : existingPlaylists) {
                    if (ep.getName().equals(pd.name)) {
                        exists = true;
                        break;
                    }
                }
                if (exists) continue;

                long playlistId = db.playlistDao().insert(new Playlist(pd.name));
                int order = 0;
                for (String path : pd.trackPaths) {
                    db.playlistDao().addTrackToPlaylist(new PlaylistTrack(playlistId, path, order++));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
