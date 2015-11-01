package com.rtong.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.rtong.gridimagesearch.R;
import com.rtong.gridimagesearch.models.SearchOptions;

public class SearchOptionActivity extends AppCompatActivity {

    private Spinner spnSize;
    private Spinner spnColor;
    private Spinner spnType;
    private EditText etSite;
    String[] valueList;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_option);

        SearchOptions options = (SearchOptions) getIntent().getParcelableExtra("options");

        //size option
        spnSize = (Spinner) findViewById(R.id.spnSize);
        valueList = getResources().getStringArray(R.array.size_list);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, valueList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSize.setAdapter(dataAdapter);
        int sizePosition = dataAdapter.getPosition(options.size);
        Log.d("in-- sizePos", Integer.toString(sizePosition));
        System.out.println("in-- sizePos" + Integer.toString(sizePosition));
        spnSize.setSelection(sizePosition);

        //color option
        spnColor = (Spinner) findViewById(R.id.spnColor);
        valueList = getResources().getStringArray(R.array.color_list);
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, valueList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(dataAdapter);
        int colorPosition = dataAdapter.getPosition(options.color);
        spnColor.setSelection(colorPosition);

        //type option
        spnType = (Spinner) findViewById(R.id.spnType);
        valueList = getResources().getStringArray(R.array.type_list);
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, valueList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(dataAdapter);
        int typePosition = dataAdapter.getPosition(options.type);
        spnType.setSelection(typePosition);

        // site option
        etSite = (EditText) findViewById(R.id.etSite);
        etSite.setText(options.site);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onSave(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view){
        String size = spnSize.getSelectedItem().toString();
        String color = spnColor.getSelectedItem().toString();
        String type = spnType.getSelectedItem().toString();
        String site = etSite.getText().toString();
        // 2. construct data intent
        Intent data = new Intent();
        // 3. send data in a bundle
        data.putExtra("size", size);
        data.putExtra("color", color);
        data.putExtra("type", type);
        data.putExtra("site", site);
        // 4. set result
        setResult(RESULT_OK, data);
        finish();
    }
}
