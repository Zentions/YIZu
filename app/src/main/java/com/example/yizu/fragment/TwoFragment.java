package com.example.yizu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yizu.R;
import com.example.yizu.ShowActivity;
import com.example.yizu.adapter.FragmentTwoAdapter;
import com.example.yizu.bean.Evaluation;

import java.util.ArrayList;
import java.util.List;
//import com.qfdqc.views.pulltoloadmoreview.utils.PublicStaticClass;
//import com.qfdqc.views.pulltoloadmoreview.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {
    View  mView;
    private Double StarRating;//星级
    private String  TextEvaluation;//文字评价
    private String rentedPerson;//租用方
    FragmentTwoAdapter adapterTwo;
    private ShowActivity showActivity;
    private List<Evaluation> evaluationsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Evaluation evaluation1,evaluation2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_two, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(showActivity));
        evaluation1=new Evaluation(5.8,"hahhahahahahahahahahahahah","sdshdgh","2017.07.08");
         evaluationsList.add(evaluation1);
        evaluation2=new Evaluation(7.2,"scbshdcbdsdbchbeefcgvcscsbdjcbjsccnsjcnjncjsnjachauhcuascghhXbchzxbcyagchacbyusgcyhbcysgceyegcsdyavgaygcyasgdcysadgvyagcdysagcaywegcfys","gsvcg","2017.07.12");
        evaluationsList.add(evaluation2);
        showActivity = (ShowActivity) getActivity();
        GridLayoutManager layoutManager = new GridLayoutManager(showActivity, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapterTwo = new FragmentTwoAdapter(evaluationsList);
        recyclerView.setAdapter(adapterTwo);
        return mView;
    }



}
