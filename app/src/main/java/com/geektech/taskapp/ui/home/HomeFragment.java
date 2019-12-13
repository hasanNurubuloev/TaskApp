package com.geektech.taskapp.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.geektech.taskapp.App;
import com.geektech.taskapp.FormActivity;
import com.geektech.taskapp.ISendInfo;
import com.geektech.taskapp.MainActivity;
import com.geektech.taskapp.OnItemClickListener;
import com.geektech.taskapp.room.TaskDao;
import com.geektech.taskapp.R;
import com.geektech.taskapp.Task;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    private TaskAdapter adapter;
    Task task;
    MainActivity ma;
    private List<Task> list;
    TaskDao taskDao;
    ISendInfo sendInfo;
    private TaskAdapter.ViewHolder viewHolder;
//    private Task task = new Task();


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        list = new ArrayList<>();
        App.getDatabase().taskDao().getAllive().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                list.clear();
                list.addAll(tasks);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new TaskAdapter(list);
        recyclerView.setAdapter(adapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick( int position) {
                SharedPreferences preferences = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
                Task task = list.get(position);
                Intent intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra("Task", task);
                startActivity(intent);
                Toast.makeText(getContext(), "pos = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemCLonglick(final int position) {
                builder.setTitle("Удалить элемент?")
                        .setMessage("Вы уверены?")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    App.getDatabase().taskDao().delete(list.get(position));
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Элемент не удален", Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog alert = builder.create();
                alert.show();
            }
        });


//        ISendInfo listener = (ISendInfo) getActivity();
//        listener.sendInfo(task);
       return root;
    }

    public void sortList(){
        list.clear();
        list.addAll(App.getDatabase().taskDao().sort());
        adapter.notifyDataSetChanged();
    }





/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 42 && resultCode == RESULT_OK && data!= null){

           task = (Task) data.getSerializableExtra("key");
            list.add(0,task);
            adapter.notifyDataSetChanged();
        }

    }*/

}


// Загуглить  on ACtivity result не работает во фрагменте
