package com.fairshare.fair_division;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Objects;
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
    static boolean isgood;
    Button calculateAlloc;
    AppDatabase db;
    CompositeDisposable disposable = new CompositeDisposable();
    AllocationDao allocationDao;
    FirebaseFunctions functions;
    ProgressBar loadingIcon;
    View background;

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

        functions = FirebaseFunctions.getInstance();
        calculateAlloc = findViewById(R.id.calculateBtn);
        loadingIcon = findViewById(R.id.loadingIcon);
        background = findViewById(R.id.blurBackground);
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "allocations")
                .fallbackToDestructiveMigration()
                .build();

        allocationDao = db.allocationDao();

        calculateAlloc.setOnClickListener(view -> {
            showLoading();
            ArrayList<ArrayList<Integer>> val_matrix = new ArrayList<>();
            String sess = UUID.randomUUID().toString();
            for (int i = 0; i < ppllist.size(); i++) {
                val_matrix.add(new ArrayList<>(goodsList.size()));
                int finalI = i;
                preferenceslog.get(i).forEach((k, v) -> {
                    Allocation alloc = new Allocation(ppllist.get(finalI), k, v, sess, isgood);
                    val_matrix.get(finalI).add(v);

                    disposable.add(
                            allocationDao.InsertRankingAsync(alloc)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> Log.d("Added Allocation", k + ' ' + v + ' ' + isgood)));

                });
            }

            if(isgood){
                getGoodsAllocation(ppllist.size(), goodsList.size(), val_matrix)
                        .addOnCompleteListener(task -> hideLoading())
                        .addOnSuccessListener(results -> {
                            Bundle resultsBundle = new Bundle();
                            Intent i = new Intent(getApplicationContext(), AllocationActivity.class);

                            ArrayList<ArrayList<Integer>> res = (ArrayList<ArrayList<Integer>>) results.get("alloc");
                            resultsBundle.putSerializable("results", res);
                            i.putExtra("mnwarray", resultsBundle);
                            i.putExtra("ppllist",ppllist);
                            i.putExtra("goodslist",goodsList);
                            startActivity(i);
                        })
                        .addOnFailureListener(exception -> {
                            Snackbar.make(view, "An unknown error occurred", Snackbar.LENGTH_LONG).show();
                            exception.printStackTrace();
                        });
            }else{

                    getChoreAllocation(ppllist.size(), goodsList.size(), val_matrix)
                            .addOnCompleteListener(task -> hideLoading())
                            .addOnSuccessListener(results -> {
                                Bundle resultsBundle = new Bundle();
                                Intent i = new Intent(getApplicationContext(), AllocationActivity.class);

                                ArrayList<ArrayList<Integer>> res = (ArrayList<ArrayList<Integer>>) results.get("alloc");
                                resultsBundle.putSerializable("results", res);
                                i.putExtra("mnwarray", resultsBundle);
                                i.putExtra("ppllist",ppllist);
                                i.putExtra("goodslist",goodsList);
                                startActivity(i);
                            })
                            .addOnFailureListener(exception -> {
                                Snackbar.make(view, "An unknown error occurred", Snackbar.LENGTH_LONG).show();
                                exception.printStackTrace();
                            });



//                result = String.valueOf(rr_alloc.call(ppllist.size(), goodsList.size(), intArray));
//                resultArray = toJava2DArray(rr_alloc.call(ppllist.size(), goodsList.size(), intArray));
            }

        });

        //if we are coming from addgoods activity, initialize preferences to be empty
        if (getIntent().getIntExtra("isAddGoods", 1) == 1) {
            //empty dictionary
            isgood = getIntent().getIntExtra("isgood", 1) == 1;
            int ppl_count = Objects.requireNonNull(getIntent().getStringArrayListExtra("ppl")).size();
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
            int index = getIntent().getIntExtra("id", 0);

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

    }



    private Task<HashMap<String, Object>> getGoodsAllocation(int agents, int goods, ArrayList<ArrayList<Integer>> val_matrix) {
        Map<String, Object> data = new HashMap<>();
        data.put("agents", agents);
        data.put("items", goods);
        data.put("values", val_matrix);

        return functions.getHttpsCallable("mnw")
                .call(data)
                .addOnCompleteListener(task -> Log.d("Completed Function Call", "done"))
                .continueWith(task -> {
                    HashMap<String, Object> result = (HashMap<String, Object>) task.getResult().getData();
                    assert result != null;
                    return result;
                });
    }

    private Task<HashMap<String, Object>> getChoreAllocation(int agents, int chores, ArrayList<ArrayList<Integer>> val_matrix) {
        Map<String, Object> data = new HashMap<>();
        data.put("chores", chores);
        data.put("values", val_matrix);
        data.put("agents", agents);

        if (agents == 2) {
            for(int i = 0; i < val_matrix.size(); i++) {
                for(int j = 0; j < val_matrix.get(i).size(); j++) {
                    val_matrix.get(i).set(j, -1 * val_matrix.get(i).get(j));
                }
            }
            data.put("values", val_matrix);
            return functions.getHttpsCallable("chores_2agents")
                    .call(data)
                    .addOnCompleteListener(task -> Log.d("Completed Chore Call", "done"))
                    .continueWith(task -> {
                        HashMap<String, Object> result = (HashMap<String, Object>) task.getResult().getData();
                        assert result != null;
                        return result;
                    });
        }

        if(agents == 3) {
            return functions.getHttpsCallable("chores_3agents")
                    .call(data)
                    .addOnCompleteListener(task -> Log.d("Completed Chore Call 3 Agents", "done"))
                    .continueWith(task -> {
                        HashMap<String, Object> result = (HashMap<String, Object>) task.getResult().getData();
                        assert result != null;
                        return result;
                    });


        }

        return functions.getHttpsCallable("get_rr_allocation")
                .call(data)
                .addOnCompleteListener(task -> Log.d("Completed Chore Multi-Agent Call", "done"))
                .continueWith(task -> {
                    HashMap<String, Object> result = (HashMap<String, Object>) task.getResult().getData();
                    assert result != null;
                    return result;
                });
    }

    private void showLoading() {
        loadingIcon.setVisibility(View.VISIBLE);
        background.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingIcon.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
    }
}