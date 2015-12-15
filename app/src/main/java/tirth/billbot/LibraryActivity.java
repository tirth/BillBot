package tirth.billbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // get listView
        ListView scriptsList = (ListView)findViewById(R.id.scripts_list);

        final ArrayList<String> testList = new ArrayList<>();

        testList.add("Uno");
        testList.add("Dos");
        testList.add("Tres");
        testList.add("Quarto");
        testList.add("Fivo");
        testList.add("Sixo");
        testList.add("I don't know Spanisho");
        testList.add("Yo");

        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testList);

        scriptsList.setAdapter(scriptAdapter);

        // onClick listener
        scriptsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = testList.get(position);
                Toast.makeText(getApplicationContext(), "You picked " + selected + ", asshole", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openRecord(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
}
