package zeev.fraiman.buildcustomtable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    MyField myField;
    ArrayList<MyField> allFields;
    ArrayAdapter<String> adapter;
    EditText etName;
    String stName, stType, stTable;
    Spinner spinner;
    String[] dataTypes = {"TEXT", "INTEGER", "REAL", "BLOB"};
    Button bNext, bBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stName=etName.getText().toString();
                myField=new MyField(stName, stType);
                allFields.add(myField);
                etName.setText("");
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                dataTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Обработка выбора элемента
                stType = dataTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Обработка события, когда ничего не выбрано
            }
        });

        bBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stTable="CREATE TABLE MyTable ( ";
                for (int i = 0; i < allFields.size() - 1; i++) {
                    myField=allFields.get(i);
                    stTable+=myField.getFieldName()+" "+myField.getFieldType()+", ";
                }
                myField=allFields.get(allFields.size()-1);
                stTable+=myField.getFieldName()+" "+myField.getFieldType()+" );";
                AlertDialog.Builder adb=new AlertDialog.Builder(context);
                adb.setMessage(stTable);
                adb.setPositiveButton("Build", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HelperDB helperDB=new HelperDB(context);
                        helperDB.createTable(stTable);
                    }
                });
                adb.create().show();
            }
        });
    }

    private void initComponents() {
        context=this;
        etName= (EditText) findViewById(R.id.etName);
        spinner= (Spinner) findViewById(R.id.spinner);
        allFields=new ArrayList<>();
        bBuild= (Button) findViewById(R.id.bBuild);
        bNext= (Button) findViewById(R.id.bNext);
    }
}