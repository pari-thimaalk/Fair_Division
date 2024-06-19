package com.fairshare.fair_division;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoresFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ImageButton fab;
    Button nextBtn;
    RecyclerView goodsList;
    EditText goodsInput;
    TextView noGoods;
    ArrayList<String> goods = new ArrayList<>();

    public ChoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoodsFragment newInstance(String param1, String param2) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_goods, container, false);
//        addPplBtn = view.findViewById(R.id.addPplBtn);
//        addPplBtn.setOnClickListener(v -> {
//            Intent i = new Intent(getContext(), AddPeopleActivity.class);
//            startActivity(i);
//        });
        TextView tv = v.findViewById(R.id.managegoods);
        tv.setText("Manage Chores");
        EditText et = v.findViewById(R.id.addGoodInput);
        et.setHint("Add New Chore");
        TextView tv1 = v.findViewById(R.id.noGoodsText);
        tv1.setText("No Chores found");
        fab = v.findViewById(R.id.goodsFab);
        noGoods = v.findViewById(R.id.noGoodsText);
        goodsList = v.findViewById(R.id.goodsList);
        nextBtn = v.findViewById(R.id.goodsNextBtn);
        goodsInput = v.findViewById(R.id.addGoodInput);

        noGoods.setVisibility(View.VISIBLE);

        fab.setOnClickListener(view -> {
            if(goodsInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Add a new chore's name!", Toast.LENGTH_SHORT).show();
            } else {
                for(String chore : goods) {
                    if(chore.equalsIgnoreCase(goodsInput.getText().toString().trim())) {
                        Toast.makeText(getContext(), "This chore is already in the list!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                goods.add(goodsInput.getText().toString().trim());
                goodsList.setAdapter(new PplAdapter(goods));
                goodsList.setLayoutManager(new LinearLayoutManager(getContext()));
                noGoods.setVisibility(View.GONE);
                if(goods.size() >=2) {
                    nextBtn.setVisibility(View.VISIBLE);
                }
                goodsInput.setText("");
            }
        });


        nextBtn.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), AddPeopleActivity.class);
            i.putStringArrayListExtra("goods", goods);
//            i.putExtra("isAddGoods",1);
            i.putExtra("isgood",0);
            startActivity(i);
        });
        return v;
    }
}