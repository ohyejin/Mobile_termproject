package mobile_test.kookmin.ac.kr.mobile_termproject;
//컴퓨터공학부 20143079 오예진

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // name of Database;
    String tableName = "idListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    // layout object
    EditText mEtName;
    EditText mEtDelete;
    EditText mEtNote;

    Button mBtInsert;
    Button mBtRead;
    Button mBtDelete;
    Button mBtAnal;

    int n1=0, n2=0, n3=0, n4=0, n5=0; //각각의 n = 운동, 공부, 시험, 미팅, 기타

    ListView mList;
    ArrayAdapter<String> musicAdapter;
    ArrayList<String> nameList;

    ArrayList<String> data_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase(dbName,dbMode,null); //db생성
        // create table;
        createTable();

        mEtName = (EditText) findViewById(R.id.et_text);
        mEtDelete = (EditText) findViewById(R.id.et_delete);
        mEtNote = (EditText) findViewById(R.id.note);

        mBtInsert = (Button) findViewById(R.id.bt_insert);
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtDelete = (Button) findViewById(R.id.bt_delete);
        mBtAnal = (Button) findViewById(R.id.anal);

        data_a = new ArrayList<String>();

        ListView mList = (ListView) findViewById(R.id.list_view);

        //입력 버튼
        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                int note =Integer.parseInt(mEtNote.getText().toString());
                data_a.clear();

                if(note==1) //운동 입력
                    n1++;
                else if(note==2) //공부 입력
                    n2++;
                else if(note==3) //시험 입력
                    n3++;
                else if(note==4) //미팅 입력
                    n4++;
                else //그 외의 기타 사건 입력
                    n5++;

                data_a.add(new String("운동"+n1+"번 입력"));
                data_a.add(new String("공부"+n2+"번 입력"));
                data_a.add(new String("시험"+n3+"번 입력"));
                data_a.add(new String("미팅"+n4+"번 입력"));
                data_a.add(new String("기타"+n5+"번 입력"));

                insertData(name);

            }
        });

        //삭제 버튼
        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer delete = Integer.parseInt(mEtDelete.getText().toString());
                removeData(delete);
            }
        });

        //입력한 사건들 읽기 버튼
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear(); //data가 쌓이므로 원본data를 수정하기 전에 한번 삭제 한다.
                selectAll();
                musicAdapter.notifyDataSetChanged();
            }
        });

        //입력한 사건들의 통계 버튼
        mBtAnal = (Button) findViewById(R.id.anal);
        mBtAnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, data_a+"\n", Toast.LENGTH_LONG).show();
            }
        });

        // Create listview
        nameList = new ArrayList<String>();
        musicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(musicAdapter);

    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "name text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data 추가
    public void insertData(String name) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "');";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 읽기(꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        result.close();
    }

    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + " ORDER BY id DESC";
        //" ORDER BY " + id + " DESC"
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();


        while (!results.isAfterLast()) {
            int id = results.getInt(0); //첫번째 index를 int 형으로 얻어온다
            String name = results.getString(1); //string형태로 얻어온다
            Log.d("mobile_project", "index= " + id + " name=" + name);

            nameList.add(name);

            results.moveToNext();
        }
        results.close();

    }

}


