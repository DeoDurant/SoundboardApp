package com.example.soundboardapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.firebase.database.core.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Profile> list;
    List<String> keys;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText profile_edit_name, profile_edit_description;
    private Button button_edit, button_edit_back, button_delete_back, button_delete;

    DatabaseReference dbReference;

    public MyAdapter(Context context, ArrayList<Profile> list, List<String> keys) {
        this.context = context;
        this.list = list;
        this.keys = keys;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.profile_entry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Profile profile = list.get(position);
        holder.name.setText(profile.getName());
        holder.description.setText(profile.getDescription());
        holder.options.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.options);
            popupMenu.inflate(R.menu.options_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.menu_edit:
                        editProfile(keys.get(holder.getAdapterPosition()));
                        break;
                    case R.id.menu_delete:
                        deleteProfile(keys.get(holder.getAdapterPosition()));
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    /**
     * A method that edits a selected profile.
     * @param key
     */
    public void editProfile(String key){
        dbReference = FirebaseDatabase.getInstance("https://soundboard-8ddb0-default-rtdb.firebaseio.com/")
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "Profiles" + "/" + key);

        dialogBuilder = new AlertDialog.Builder(context);
        final View profilePopupView = LayoutInflater.from(context).inflate(R.layout.popup_profile_edit, null);
        profile_edit_name = profilePopupView.findViewById(R.id.profile_edit_name);
        profile_edit_description = profilePopupView.findViewById(R.id.profile_edit_description);
        button_edit_back = (Button) profilePopupView.findViewById(R.id.button_edit_back);
        button_edit = (Button) profilePopupView.findViewById(R.id.button_edit);

        dialogBuilder.setView(profilePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        button_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = profile_edit_name.getText().toString().trim();
                String description = profile_edit_description.getText().toString().trim();

                Profile updatedProfile = new Profile(name, description);

                dbReference
                        .setValue(updatedProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else{
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        button_edit_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });

    }

    /**
     * A method that deletes a selected profile.
     * @param key
     */
    public void deleteProfile(String key){
        dbReference = FirebaseDatabase.getInstance("https://soundboard-8ddb0-default-rtdb.firebaseio.com/")
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "Profiles" + "/" + key);

        dialogBuilder = new AlertDialog.Builder(context);
        final View profilePopupView = LayoutInflater.from(context).inflate(R.layout.popup_profile_delete, null);
        button_delete_back = (Button) profilePopupView.findViewById(R.id.button_delete_back);
        button_delete = (Button) profilePopupView.findViewById(R.id.button_delete);

        dialogBuilder.setView(profilePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        button_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                dbReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Profile deleted!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        button_delete_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, options;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            description = itemView.findViewById(R.id.text_description);
            options = itemView.findViewById(R.id.tv_options);
        }
    }
}
