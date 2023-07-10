package com.example.soundboardapp;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private ParseAdapter parseAdapter;
    private String btnURL;

    private ArrayList<Soundboard> parseSoundboard = new ArrayList<>();
    List<String> URLs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        // Adds an addValueEventListener for when the user first loads up the home page.
        URLs = new ArrayList<>();
        recyclerView = view.findViewById(R.id.soundRecycleView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parseAdapter = new ParseAdapter(parseSoundboard, getContext(), URLs);
        recyclerView.setAdapter(parseAdapter);

        Content content = new Content();
        content.execute();


        return view;
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            parseAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                String url = "https://simpleguics2pygame.readthedocs.io/en/latest/_static/links/snd_links.html";
                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("ul.sounds");

//                System.out.println(data);

                for (int i = 0; i < 40; i++){

                    String soundURL = data.select("li")
                            .select("a.url")
                            .eq(i)
                            .attr("href");

                    String title = data.parents().select("a").eq(i).text();
                    URLs.add(soundURL);


                    parseSoundboard.add(new Soundboard("Name: " + title, "URL: " + soundURL));
                }

            } catch (Exception e){

            }
            return null;
        }
    }
}

//import android.media.AudioAttributes;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import java.io.IOException;
//
//
//public class SearchFragment extends Fragment {
//
//    private Button btnAudio;
//    private MediaPlayer mediaPlayer;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        btnAudio = view.findViewById(R.id.btnAudio);
//        btnAudio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //String url = "http://commondatastorage.googleapis.com/codeskulptor-assets/Collision8-Bit.ogg";
//                String url = "https://freesound.org/people/Bram/sounds/15543/";
//
//                try{
//                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setAudioAttributes(
//                            new AudioAttributes.Builder()
//                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                                    .build()
//                    );
//                    mediaPlayer.setDataSource(url);
//                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
//                    mediaPlayer.start();
//                }
//                catch(IOException e){
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//
//
//        return view;
//    }
//}

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.ProgressBar;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.util.ArrayList;

