package com.geektech.taskapp.onboard;


/*
* 1. Фон каждой страницы должен быть разного цвета
2. Добавить кнопку SKIP (и он не должен двигаться вместе с фрагментом)
3. Точки (TabLayout)        */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geektech.taskapp.MainActivity;
import com.geektech.taskapp.R;
import com.google.android.material.tabs.TabLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class BoardFragment extends Fragment {
//TabLayout tabLayout;
//ViewPager viewPager;

    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        int pos = getArguments().getInt("pos");
        TextView textView = view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.imageView);
        Button button = view.findViewById(R.id.button);

        switch (pos) {
            case 0:
                button.setVisibility(View.INVISIBLE);
                textView.setText("Privet");
                view.setBackgroundResource(R.color.colorPrimary);
                imageView.setImageResource(R.drawable.onboard_page1);
                break;
            case 1:
                button.setVisibility(View.INVISIBLE);
                textView.setText("kak dela");
                view.setBackgroundResource(R.color.colorPrimaryDark);

                imageView.setImageResource(R.drawable.onboard_page2);

                break;
            case 2:
                button.setVisibility(View.VISIBLE);
                textView.setText("chto delaesh");
                view.setBackgroundResource(R.color.colorAccent);
                imageView.setImageResource(R.drawable.onboard_page3);

                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
                preferences.edit().putBoolean("isShown", true).apply();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });


        return view;
    }


}
