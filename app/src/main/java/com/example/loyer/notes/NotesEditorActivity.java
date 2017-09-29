package com.example.loyer.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NotesEditorActivity extends AppCompatActivity {

    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        // oluşturduğumuz idyi çektik
         noteId = intent.getIntExtra("noteId",-1);

        if(noteId != -1)
        {

            editText.setText(MainActivity.notes.get(noteId));
        }
        else{
            MainActivity.notes.add("");

            noteId = MainActivity.notes.size() -1;

            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        // edittext üzerinde değişiklik yaptığımızda bunu otomatik olarak kaydedicek
        // ve önceki sayfada da aynı string değer yazıcak
        // yani kod çalışırken yapılan değişikliği anlık uyglayacak.
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
              //bu fonksiyon sağlıyor yukarıda yazılanları
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(noteId,String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();


                // kalıcı kaydetmek için sharedi oluşturduk

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.loyer.notes",Context.MODE_PRIVATE);

                //stringi atmak için yeni sınıfla uğraşmamak için
                // hashset oluşturup stringi ona atıp öyle kaydettik
                HashSet<String> set = new HashSet(MainActivity.notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
