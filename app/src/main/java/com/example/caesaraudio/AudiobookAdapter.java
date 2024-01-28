package com.example.caesaraudio;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AudiobookAdapter extends RecyclerView.Adapter<AudiobookAdapter.ViewHolder> {
    private List<List<Audiobook>> audiobookGroups;

    private LayoutInflater inflater;

    public AudiobookAdapter(Context context) {
        this.audiobookGroups = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_audiobook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudiobookAdapter.ViewHolder holder, int position) {
        List<Audiobook> group = audiobookGroups.get(position);
        // Assuming all audiobooks in a group have the same author and book
        Audiobook representative = group.get(0);
        holder.textTitle.setText(representative.getBook());
        holder.textAuthor.setText(representative.getAuthor());

    }

    @Override
    public int getItemCount() {
        return audiobookGroups.size();
    }

    public void updateData(List<List<Audiobook>> newAudiobookGroups) {
        audiobookGroups.clear();
        audiobookGroups.addAll(newAudiobookGroups);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.audiobookItemTitle);
            textAuthor = itemView.findViewById(R.id.audiobookItemAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        List<Audiobook> group = audiobookGroups.get(position);
                        Intent intent = new Intent(v.getContext(), AudiobookGroupActivity.class);
                        intent.putExtra("audiobookGroup", (Serializable) group);
                        v.getContext().startActivity(intent);

                    }
                }
            });
        }
    }
}
