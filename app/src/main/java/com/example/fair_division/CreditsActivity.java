package com.example.fair_division;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreditsActivity extends AppCompatActivity {

    RecyclerView creditsPplList;
    static ArrayList<HashMap<String, Integer>> preferenceslog;
    static ArrayList<String> goodsList;
    static ArrayList<String> ppllist;
    Button calculateAlloc;
    AppDatabase db;
    CompositeDisposable disposable = new CompositeDisposable();
    AllocationDao allocationDao;
    Python py;
    PyObject nash;
    PyObject mnw;

    // Convert Python array to Java 2D array
    private int[][] toJava2DArray(PyObject pyArray) {
        int numRows = pyArray.callAttr("__len__").toInt();
        int[][] javaArray = new int[numRows][];
        for (int i = 0; i < numRows; i++) {
            PyObject pyRow = pyArray.callAttr("__getitem__",i);
            int numCols = pyRow.callAttr("__len__").toInt();
            Log.d("numcols",String.valueOf(numCols));
            javaArray[i] = new int[numCols];
            for (int j = 0; j < numCols; j++) {
                javaArray[i][j] = pyRow.callAttr("__getitem__",j).toInt();
                Log.d("item",String.valueOf(pyRow.callAttr("__getitem__",j).toInt()));
            }
        }
        return javaArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        if(!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        py = Python.getInstance();
        nash = py.getModule("nash");
        mnw = nash.get("main");

        calculateAlloc = findViewById(R.id.calculateBtn);
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "allocations")
                .fallbackToDestructiveMigration()
                .build();

        allocationDao = db.allocationDao();

        calculateAlloc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ArrayList<ArrayList<Integer>> val_matrix = new ArrayList<>();
                String sess = UUID.randomUUID().toString();
                for (int i = 0; i < ppllist.size(); i++) {
                    val_matrix.add(new ArrayList<>(goodsList.size()));
                    int finalI = i;
                    preferenceslog.get(i).forEach((k, v) -> {
                        Allocation alloc = new Allocation(ppllist.get(finalI), k, v, sess);
                        val_matrix.get(finalI).add(v);
//                        val_matrix.get(finalI).toArray();


                        disposable.add(
                                allocationDao.InsertRankingAsync(alloc)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> Log.d("Added Allocation", k + ' ' + v)));

                    });
                }
                Log.d("People Allocations", val_matrix.toString());
                int[][] intArray = val_matrix.stream().map(  u  ->  u.stream().mapToInt(i->i).toArray()  ).toArray(int[][]::new);


                String result = String.valueOf(mnw.call(ppllist.size(), goodsList.size(), intArray));

                int[][] resultArray = toJava2DArray(mnw.call(ppllist.size(), goodsList.size(), intArray));
                Log.d("results array",String.valueOf(resultArray[0][0]));

                Intent i = new Intent(getApplicationContext(), AllocationActivity.class);
                i.putExtra("mnw", result);
                i.putExtra("mnwarray",resultArray);
                i.putExtra("ppllist",ppllist);
                i.putExtra("goodslist",goodsList);
                startActivity(i);

            }
        });

        //if we are coming from addgoods activity, initialize preferences to be empty
        if (getIntent().getIntExtra("isAddGoods", 1) == 1) {
            //empty dictionary
            int ppl_count = getIntent().getStringArrayListExtra("ppl").size();
            preferenceslog = new ArrayList<>();
            for (int i = 0; i < ppl_count; i++) {
                preferenceslog.add((new HashMap<String, Integer>()));
            }
            //assign list of people and goods
            ppllist = getIntent().getStringArrayListExtra("ppl");
            goodsList = getIntent().getStringArrayListExtra("goods");
        } else {
            //get hash from intent
            //index into preferences log and insert hash
            Integer index = getIntent().getIntExtra("id", 0);

            preferenceslog.set(index, (HashMap<String, Integer>) getIntent().getSerializableExtra("preferencesHash"));
        }
        boolean isFull = true;
        for (HashMap<String, Integer> item : preferenceslog) {
            if (item.isEmpty()) {
                isFull = false;
                break;
            }
        }
        if (isFull) {
            calculateAlloc.setVisibility(View.VISIBLE);

        }
        creditsPplList = findViewById(R.id.creditsPplList);
        creditsPplList.setAdapter(new CreditsPplAdapter(ppllist, goodsList, CreditsActivity.this, preferenceslog));
        creditsPplList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        Log.d("Credits People", String.valueOf(getIntent().getStringArrayListExtra("ppl")));
//        Log.d("Credits Goods", String.valueOf(getIntent().getStringArrayListExtra("goods")));
//        Log.d("Preferences Log", String.valueOf(preferenceslog));
    }
}