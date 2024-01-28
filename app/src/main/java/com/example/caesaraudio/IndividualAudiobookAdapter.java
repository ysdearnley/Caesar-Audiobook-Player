package com.example.caesaraudio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caesaraudio.Audiobook;

import java.util.List;

public class IndividualAudiobookAdapter extends RecyclerView.Adapter<IndividualAudiobookAdapter.ViewHolder> {

    private final List<Audiobook> audiobookList;
    private final LayoutInflater inflater;

    public IndividualAudiobookAdapter(Context context, List<Audiobook> audiobookList) {
        this.audiobookList = audiobookList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.individual_chapters, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Audiobook audiobook = audiobookList.get(position);
        holder.textTitle.setText(audiobook.getTitle());
        holder.textAuthor.setText(audiobook.getAuthor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, we directly handle the click event to play the audiobook
                Intent intent = new Intent(holder.itemView.getContext(), AudiobookPlayerActivity.class);
                intent.putExtra("AudiobookFilePath", audiobook.getFilepath());
                intent.putExtra("AudiobookTitle", audiobook.getTitle());
                intent.putExtra("AudiobookAuthor", audiobook.getAuthor());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return audiobookList != null ? audiobookList.size() : 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.individualChapterTitle);
            textAuthor = itemView.findViewById(R.id.individualChapterAuthor);
        }
    }
}
