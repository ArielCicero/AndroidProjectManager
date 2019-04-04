package com.sda.android.projectmanager;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sda.android.projectmanager.model.Task;
import com.sda.android.projectmanager.model.TaskAdapter;

public class HomeActivity extends AppCompatActivity implements TaskAdapter.RVItemSelected {

    private Toolbar tbToolbar;


    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbReference;

    private TextView tvDescription;
    private ImageView ivStatus;


    private ImageButton btTodo;
    private ImageButton btInProgress;
    private ImageButton btDone;

    private FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tbToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbToolbar);
        // getSupportActionBar().setTitle("Home");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        mDatabase = FirebaseDatabase.getInstance();

        mDbReference = mDatabase.getReference().child("Tasks").child(userId);

        tvDescription = findViewById(R.id.tvDescription);
        ivStatus = findViewById(R.id.ivStatus);

        btTodo = findViewById(R.id.btTodo);
        btInProgress = findViewById(R.id.btInProgress);
        btDone = findViewById(R.id.btDone);

        manager = this.getSupportFragmentManager();

        // The phone is in portrait mode
        if(findViewById(R.id.layout_portrait) != null){
            manager.beginTransaction()
                    .hide(manager.findFragmentById(R.id.detailFrag))
                    .show(manager.findFragmentById(R.id.listFrag))
                    .commit();
        }

        // The phone is in landscape mode
        if(findViewById(R.id.layout_landscape) != null){
            manager.beginTransaction()
                    .show(manager.findFragmentById(R.id.detailFrag))
                    .show(manager.findFragmentById(R.id.listFrag))
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecycleViewItemSelected(final String taskKey, final Task task) {
        tvDescription.setText(task.getDescription());
        findViewById(R.id.detailsLayout).setVisibility(View.VISIBLE);

        btTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStatus.setImageResource(R.drawable.todo);
                Task updatedTask = new Task(
                        taskKey,
                        task.getTitle(),
                        task.getDescription(),
                        "todo"
                );
                mDbReference.child(taskKey).setValue(updatedTask);
            }
        });

        btInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStatus.setImageResource(R.drawable.progress);
                Task updatedTask = new Task(
                        taskKey,
                        task.getTitle(),
                        task.getDescription(),
                        "in-progress"
                );
                mDbReference.child(taskKey).setValue(updatedTask);
            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStatus.setImageResource(R.drawable.done);
                Task updatedTask = new Task(
                        taskKey,
                        task.getTitle(),
                        task.getDescription(),
                        "done"
                );
                mDbReference.child(taskKey).setValue(updatedTask);
            }
        });

        switch (task.getStatus()){
            case "todo":
                ivStatus.setImageResource(R.drawable.todo);
                break;
            case "in-progress":
                ivStatus.setImageResource(R.drawable.progress);
                break;
            case "done":
                ivStatus.setImageResource(R.drawable.done);
                break;
        }

        // The phone is in portrait mode
        if(findViewById(R.id.layout_portrait) != null){
            manager = this.getSupportFragmentManager();

            manager.beginTransaction()
                    .show(manager.findFragmentById(R.id.detailFrag))
                    .hide(manager.findFragmentById(R.id.listFrag))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
