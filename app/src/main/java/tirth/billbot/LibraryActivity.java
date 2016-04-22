package tirth.billbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class LibraryActivity extends AppCompatActivity {

    public HashMap<String, Integer> nameMap;
    public HashMap<Long, String> dateMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        nameMap = new HashMap<>();
        dateMap = new HashMap<>();

        findViewById(R.id.radio_name).callOnClick();
    }

    public void sort(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // enumerate scripts
        String path = getFilesDir().getAbsolutePath();
        File allFiles = new File(path);
        File[] fileList = allFiles.listFiles();
        ArrayList<String> allFileList = new ArrayList<>();

        for (File file : fileList) {
            if (!file.getName().contains(".3gp"))
                continue;

            allFileList.add(file.getName());
            dateMap.put(file.lastModified(), file.getName());
        }

        for (String fileName : allFileList) {
            Log.v("THING", fileName);

            String[] fileStuff = fileName.split("-");

            String name = fileStuff[0];
            String num = fileStuff[1].substring(0, fileStuff[1].indexOf('.'));

            Integer number = Integer.parseInt(num);

            if (!nameMap.containsKey(name))
            {
                nameMap.put(name, number);
            }
            else {
                if (number > nameMap.get(name)) {
                    nameMap.remove(name);
                    nameMap.put(name, number);
                }
            }
        }

        // get listView
        ListView scriptsList = (ListView) findViewById(R.id.scripts_list);

        final ArrayList<String> names = new ArrayList<>();

        switch (view.getId()) {
            case R.id.radio_date:
                if (checked) {
                    ArrayList<Long> dates = new ArrayList<>();

                    for (Long date : dateMap.keySet()) {
                        dates.add(date);
                    }

                    Collections.sort(dates);

                    names.clear();

                    for (Long date : dates) {
                        String name = dateMap.get(date).split("-")[0];

                        if (!names.contains(name))
                            names.add(name);
                    }

                    Collections.reverse(names);
                }
                break;

            case R.id.radio_name:
                if (checked) {
                    for (String name : nameMap.keySet())
                        names.add(name);

                    Collections.sort(names);
                }
                break;
        }

        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);

        scriptsList.setAdapter(scriptAdapter);

        // onClick listener
        scriptsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = names.get(position);
                done(selected, nameMap.get(selected));
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
