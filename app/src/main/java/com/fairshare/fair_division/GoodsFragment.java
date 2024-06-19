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
 * Use the {@link GoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoodsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ImageButton fab;
    Button nextBtn;
    RecyclerView goodsList;
    EditText goodsInput;
    TextView noGoods;
    ArrayList<String> goods = new ArrayList<>();

    public GoodsFragment() {
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

        fab = v.findViewById(R.id.goodsFab);
        noGoods = v.findViewById(R.id.noGoodsText);
        goodsList = v.findViewById(R.id.goodsList);
        nextBtn = v.findViewById(R.id.goodsNextBtn);
        goodsInput = v.findViewById(R.id.addGoodInput);

        noGoods.setVisibility(View.VISIBLE);

        fab.setOnClickListener(view -> {
            if(goodsInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Add a new good's name!", Toast.LENGTH_SHORT).show();
            }
            else {
                for(String good : goods) {
                    if(good.equalsIgnoreCase(goodsInput.getText().toString().trim())) {
                        Toast.makeText(getContext(), "This good is already in the list!", Toast.LENGTH_SHORT).show();
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
            i.putExtra("isgood",1);
            startActivity(i);
        });
        return v;
    }
}