package com.example.belfastinanutshell;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BarsFragment extends Fragment {
    private Button mapBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        return inflater.inflate(R.layout.fragment_bars,container,false);
        View rootView = inflater.inflate(R.layout.fragment_bars, container, false);
        Button mapBtn = (Button) rootView.findViewById(R.id.mapBtn);


        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        return rootView;

    }
}
