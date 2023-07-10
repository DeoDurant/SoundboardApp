package com.example.soundboardapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText profile_name, profile_description;
    private Button button_save, button_back;
    private FloatingActionButton fab;
    private FirebaseUser mAuth;
    private TextView tv;


    private DatabaseReference dbReference;
    RecyclerView recyclerView;
    ArrayList<Profile> list;
    MyAdapter adapter;
    List<String> keys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Adds an addValueEventListener for when the user first loads up the home page.
        recyclerView = view.findViewById(R.id.recyclerview);
        tv = view.findViewById(R.id.txtView_profile);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        dbReference = FirebaseDatabase.getInstance("https://soundboard-8ddb0-default-rtdb.firebaseio.com/")
                .getReference("Users");

        list = new ArrayList<>();
        keys = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(getContext(), list, keys);
        recyclerView.setAdapter(adapter);


        dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "Profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                if(list.isEmpty()){
//                    tv.setVisibility(View.VISIBLE);
//                }
//                else{
//                    tv.setVisibility(View.INVISIBLE);
//                }

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    list.add(profile);
                    keys.add(dataSnapshot.getKey());
                }


                adapter.notifyItemChanged(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // Sets the onClickListener of the Floating Action Button to the createNewContactDialog method.
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewProfileDialog();
            }
        });


        return view;
    }



    /**
     * A method that will add a new Profile in the database.
     */
    public void createNewProfileDialog(){

        dialogBuilder = new AlertDialog.Builder(getContext());
        final View profilePopupView = getLayoutInflater().inflate(R.layout.popup_profile, null);
        profile_name = profilePopupView.findViewById(R.id.profile_name);
        profile_description = profilePopupView.findViewById(R.id.profile_description);
        button_back = (Button) profilePopupView.findViewById(R.id.button_back);
        button_save = (Button) profilePopupView.findViewById(R.id.button_save);

        dialogBuilder.setView(profilePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        button_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = profile_name.getText().toString().trim();
                String description = profile_description.getText().toString().trim();

                Profile newProfile = new Profile(name, description);



                dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "Profiles").push()
                    .setValue(newProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Profile created!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });

        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });

    }

}