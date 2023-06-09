 package com.example.mvvmsampleapp;

 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Toast;

 import androidx.activity.result.ActivityResult;
 import androidx.activity.result.ActivityResultCallback;
 import androidx.activity.result.ActivityResultLauncher;
 import androidx.activity.result.contract.ActivityResultContracts;
 import androidx.annotation.Nullable;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.lifecycle.Observer;
 import androidx.lifecycle.ViewModelProvider;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.google.android.material.floatingactionbutton.FloatingActionButton;

 import java.util.List;

 public class MainActivity extends AppCompatActivity {
     public static final int  ADD_NOTE_REQUEST = 1;
    private NoteViewModel noteViewModel;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK){
                Toast.makeText(MainActivity.this, "feito", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startForResult.launch(intent);
        }
    });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // update RecyclerView
                adapter.setNotes(notes);
            }
        });
    }

     public void returnData(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
             String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
             String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
             int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

             Note note = new Note(title, description, priority);
             noteViewModel.insert(note);

             Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
         }else{
             Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
         }
     }
 }