package tirth.billbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // get listView
        ListView scriptsList = (ListView)findViewById(R.id.scripts_list);

        // enumerate scripts
        String path = getFilesDir().getAbsolutePath();
        File allFiles = new File(path);
        File[] fileList = allFiles.listFiles();
        ArrayList<String> allFileList = new ArrayList<>();

        for (File file : fileList) {
            allFileList.add(file.getName());
        }

        final HashMap<String, Integer> stringMap = new HashMap<>();

        for (String fileName : allFileList) {
            String[] fileStuff = fileName.split("-");

            String name = fileStuff[0];
            Integer number = Integer.parseInt(fileStuff[1]);

            if (!stringMap.containsKey(name))
                stringMap.put(name, number);
            else {
                if (number > stringMap.get(name)) {
                    stringMap.remove(name);
                    stringMap.put(name, number);
                }
            }
        }

        final ArrayList<String> names = new ArrayList<>();

        for (String name :
                stringMap.keySet()) {
         names.add(name);
        }

        final ArrayList<String> testList = new ArrayList<>();

        testList.add("Uno");
        testList.add("Dos");
        testList.add("Tres");
        testList.add("Quarto");
        testList.add("Fivo");
        testList.add("Sixo");
        testList.add("I don't know Spanisho");
        testList.add("Yo");

        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);

        scriptsList.setAdapter(scriptAdapter);

        // onClick listener
        scriptsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = names.get(position);

//                Toast.makeText(getApplicationContext(), "You picked " + selected, Toast.LENGTH_LONG).show();

                done(selected, stringMap.get(selected));
            }
        });
    }

    public void openRecord(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    public void done(String scriptName, int numRecordings) {
        Intent intent = new Intent(this, PlaybackActivity.class);

        intent.putExtra("scriptName", scriptName);
        intent.putExtra("numRecordings", numRecordings);

        startActivity(intent);
    }
}
