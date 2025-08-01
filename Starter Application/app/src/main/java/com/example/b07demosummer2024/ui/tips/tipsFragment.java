package com.example.b07demosummer2024.ui.tips;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.DataHandler;
import com.example.b07demosummer2024.questions.*;
import java.util.ArrayList;

public class tipsFragment extends Fragment {

    private LinearLayout layout;

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

        if (getView() == null) {
            Log.e("tipsFragment", "View not found");
        }

        Question question = new SelectionQuestion("a", "situation", new ArrayList<>(), 1);
        new DataHandler(getContext(), dh -> {
            dh.getAnswerByQuestion(question);
            Log.d("DataHandler", dh.getTipById("abuse_types"));
        });
    }
}
