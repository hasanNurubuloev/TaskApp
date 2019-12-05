package com.geektech.taskapp.onboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.geektech.taskapp.MainActivity;
import com.geektech.taskapp.R;
import com.google.android.material.tabs.TabLayout;

public class OnBoardActivity extends AppCompatActivity {
Button skip;

//BoardFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        ViewPager viewPager = findViewById(R.id.viewPager);
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager, true);


       // fragment = (BoardFragment) getSupportFragmentManager().findFragmentById(R.id.boardFragment);
    }

    public void onClick(View view) {
       startActivity( new Intent(this, MainActivity.class));

    }

    public class SectionPagerAdapter extends FragmentPagerAdapter{

        public SectionPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, SectionPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            BoardFragment fragment = new BoardFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pos", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}

