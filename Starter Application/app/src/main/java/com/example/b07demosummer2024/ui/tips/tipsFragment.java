package com.example.b07demosummer2024.ui.tips;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.data.Date;
import com.example.b07demosummer2024.data.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.DataHandler;
import com.example.b07demosummer2024.questions.*;
import com.example.b07demosummer2024.tips.TipsModel;
import com.example.b07demosummer2024.tips.TipsRecyclerViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class tipsFragment extends Fragment {

    static String[] QUESTION_JSONS = {"questions.json",
                                    "questions-branches.json",
                                    "questions-followup.json"};
    ArrayList<TipsModel> tipsModels = new ArrayList<>();

    public tipsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.tipsRecyclerView);

        setUpTipModels(recyclerView);

        if (getView() == null) {
            Log.e("tipsFragment", "View not found");
        }
    }

    private void setUpTipModels(RecyclerView recyclerView) {
        new DataHandler(getContext(), dh -> {
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<Integer> images = new ArrayList<>();

            for (String json : QUESTION_JSONS) {
                LinkedHashMap<String, Question> questionMap = JsonReader.getQuestionMap(getContext(), json);
                for (Question q : questionMap.values()) {
                    String tip = dh.getTipByQuestion(q);
                    if (tip != null && !tip.isEmpty()) {
                        strings.add(tip);
                        int resId = getContext().getResources().getIdentifier(q.getId(), "drawable", getContext().getPackageName());
                        if (resId != 0) {
                            images.add(resId);
                        } else {
                            images.add(R.drawable.no_image_found);
                        }
                    }
                }
            }

            for (int i = 0; i < strings.size(); i++) {
                tipsModels.add(new TipsModel(strings.get(i), images.get(i)));
            }
            TipsRecyclerViewAdapter adapter = new TipsRecyclerViewAdapter(this.getContext(), tipsModels);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        });
    }
}
