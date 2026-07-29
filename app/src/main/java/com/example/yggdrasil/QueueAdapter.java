package com.example.yggdrasil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.net.Uri;
import android.content.ContentUris;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import java.util.ArrayList;
import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {

    private List<MediaItem> queueItems = new ArrayList<>();
    private int currentMediaItemIndex = -1;

    public interface OnQueueItemClickListener {
        void onQueueItemClick(int position);
    }

    private final OnQueueItemClickListener listener;

    public QueueAdapter(OnQueueItemClickListener listener) {
        this.listener = listener;
    }

    public void setQueueItems(List<MediaItem> items, int currentIndex) {
        this.queueItems = items;
        this.currentMediaItemIndex = currentIndex;
        notifyDataSetChanged();
    }

    public void setCurrentMediaItemIndex(int index) {
        this.currentMediaItemIndex = index;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_queue, parent, false);
        return new QueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder holder, int position) {
        MediaItem item = queueItems.get(position);
        MediaMetadata metadata = item.mediaMetadata;

        holder.title.setText(metadata.title != null ? metadata.title.toString() : "Unknown Title");
        holder.artist.setText(metadata.artist != null ? metadata.artist.toString() : "Unknown Artist");

        if (position == currentMediaItemIndex) {
            holder.playingIndicator.setVisibility(View.VISIBLE);
            holder.itemView.setSelected(true);
        } else {
            holder.playingIndicator.setVisibility(View.GONE);
            holder.itemView.setSelected(false);
        }

        if (metadata.artworkUri != null) {
            Glide.with(holder.itemView.getContext())
                    .load(metadata.artworkUri)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.albumArt);
        } else {
             holder.albumArt.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQueueItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return queueItems.size();
    }

    static class QueueViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView albumArt;
        ImageView playingIndicator;

        public QueueViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.queueTrackTitle);
            artist = itemView.findViewById(R.id.queueTrackArtist);
            albumArt = itemView.findViewById(R.id.queueAlbumArt);
            playingIndicator = itemView.findViewById(R.id.queuePlayingIndicator);
        }
    }
}