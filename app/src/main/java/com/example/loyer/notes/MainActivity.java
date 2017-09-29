package com.example.loyer.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ListView lstNotes;

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;


    //menüyüy ekledik
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

        //açılan menüde seçim yapılınca ne olacağını ekledik
    // noteeditör activitesi açıldı.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.mnaddNote)
        {
            Intent intent = new Intent(getApplicationContext(),NotesEditorActivity.class);

            startActivity(intent);

            return true;
        }

        return false;


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstNotes = (ListView) findViewById(R.id.lstNotes);

        // açılış ekranında oluşturduk
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.loyer.notes", Context.MODE_PRIVATE);
            //notları sıfırladık
        HashSet<String> set = (HashSet<String>)sharedPreferences.getStringSet("notes",null);
        // eğer notlar boşsa ekledik
        if (set == null) {

            notes.add("Örnek not");
        }else{
            // boş değilse hashsettekileri atadık.
            notes = new ArrayList(set);
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,notes);

        lstNotes.setAdapter(arrayAdapter);
        // listeden bir itema dokunulduğun da noteeditor sayfasının açılmasını sağladık
        lstNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                Intent intent = new Intent(getApplicationContext(),NotesEditorActivity.class);
                //açılan editore notları karıştırmamak için id atadık
                intent.putExtra("noteId",i);

                startActivity(intent);
            }
        });

        // listedeki itemlardan birine uzun süre basılınca silme işlemi için
        // gerekenleri yaptık
        lstNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                // alertdialogtaki i ile karışmaması için bu değişkeni oluşturduk
                final int itemToDelete = i;

              new AlertDialog.Builder(MainActivity.this)
                      .setIcon(android.R.drawable.ic_dialog_alert)
                      .setTitle("Are you sure?")
                      .setMessage("Do you wanna delete this note?")
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                             // oluşturduğumuz listeden sildik ve listeyi güncelledik.
                              notes.remove(itemToDelete);
                              arrayAdapter.notifyDataSetChanged();

                              // buraya da kopyaladık çünkü not silince güncellenmesi gerek.
                              SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.loyer.notes", Context.MODE_PRIVATE);


                              HashSet<String> set = new HashSet(MainActivity.notes);

                              sharedPreferences.edit().putStringSet("notes", set).apply();
                          }
                      })
                      .setNegativeButton("No",null)
                      .show();

                return true;
            }
        });


    }
}
