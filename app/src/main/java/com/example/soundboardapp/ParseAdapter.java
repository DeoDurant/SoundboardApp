package com.example.soundboardapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundboardapp.Soundboard;

import java.util.ArrayList;
import java.util.List;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {

    private ArrayList<Soundboard> parseSoundboard;
    private Context context;
    List<String> soundURL;
    private Button btnAudio;
    private MediaPlayer mediaPlayer;

    public ParseAdapter(ArrayList<Soundboard> parseItems, Context context, List<String> url){
        this.parseSoundboard = parseItems;
        this.context = context;
        this.soundURL = url;
    }

    @NonNull
    @Override
    public ParseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapter.ViewHolder holder, int position) {
        Soundboard soundboard = parseSoundboard.get(position);
        holder.textViewName.setText(soundboard.getName());
        holder.textViewLink.setText(soundboard.getLink());
        holder.btnAudioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String url = soundURL.get(holder.getAdapterPosition());

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                    mediaPlayer.start();

//                    if(mediaPlayer.isPlaying()){
//
//                        mediaPlayer.pause();
//                    }

                }
                catch(Exception e){
                    System.out.println("This is error" + e);
                }
            }
        });
        holder.btnAudioPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return parseSoundboard.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewName, textViewLink;
        Button btnAudioPlay, btnAudioPause;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            textViewName = itemView.findViewById(R.id.txtView_name);
            textViewLink = itemView.findViewById(R.id.txtView_url);
            btnAudioPlay = itemView.findViewById(R.id.btn_soundPlay);
            btnAudioPause = itemView.findViewById(R.id.btn_soundPause);
        }
    }
}
