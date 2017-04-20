package com.powersst.simpletodos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayDeque;

public class MainActivity extends AppCompatActivity {

    //private TextView mTodoListTextView;
    private RecyclerView mTodoListRecyclerView;

    private EditText mTodoEntryEditText;
    private ArrayDeque<String> mTodoList;

    private TodoAdapter mTodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //entry point for the application
        setContentView(R.layout.activity_main); //Sets layout to the activity_main.xml file

        //mTodoListTextView = (TextView)findViewById(R.id.tv_todo_list);
        mTodoListRecyclerView = (RecyclerView)findViewById(R.id.rv_todo_list);
        mTodoEntryEditText = (EditText)findViewById(R.id.et_todo_entry_box);
        //mTodoList = new ArrayDeque<String>();

        mTodoListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTodoListRecyclerView.setHasFixedSize(true);

        mTodoAdapter = new TodoAdapter();
        mTodoListRecyclerView.setAdapter(mTodoAdapter);

        Button addTodoButton = (Button)findViewById(R.id.btn_add_todo);

        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String todoText = mTodoEntryEditText.getText().toString();

                if(!TextUtils.isEmpty(todoText)) {
                    //String currentText = mTodoListTextView.getText().toString();
                    //mTodoList.push(todoText);
                    mTodoAdapter.addTodo(todoText);

                    //mTodoListTextView.setText("");
                    //for (String todo : mTodoList) {
                        //mTodoListTextView.append(todo + "\n\n");
                    //}
                    mTodoEntryEditText.setText("");
                }
            }
        });
    }
}
