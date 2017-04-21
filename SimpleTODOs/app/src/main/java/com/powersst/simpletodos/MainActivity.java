package com.powersst.simpletodos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayDeque;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoCheckedChangeListener{

    //private TextView mTodoListTextView;
    private RecyclerView mTodoListRecyclerView;

    private EditText mTodoEntryEditText;
    private ArrayDeque<String> mTodoList;

    private TodoAdapter mTodoAdapter;

    private Toast mTodoToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //entry point for the application
        setContentView(R.layout.activity_main); //Sets layout to the activity_main.xml file

        mTodoListRecyclerView = (RecyclerView)findViewById(R.id.rv_todo_list);
        mTodoEntryEditText = (EditText)findViewById(R.id.et_todo_entry_box);
        mTodoListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTodoListRecyclerView.setHasFixedSize(true);

        //Item Animator
        mTodoListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Todo_Adapter
        mTodoAdapter = new TodoAdapter(this);
        mTodoListRecyclerView.setAdapter(mTodoAdapter);

        //Initialize Toast to Null
        mTodoToast = null;


        //Button Item
        Button addTodoButton = (Button)findViewById(R.id.btn_add_todo);

        //Button Click
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String todoText = mTodoEntryEditText.getText().toString();

                if(!TextUtils.isEmpty(todoText)) {
                    mTodoListRecyclerView.scrollToPosition(0);
                    mTodoAdapter.addTodo(todoText);
                    mTodoEntryEditText.setText("");
                }
            }
        });


        //Swipe to Delete (Item Touch Helper)
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ((TodoAdapter.TodoViewHolder)viewHolder).removeFromList();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mTodoListRecyclerView);


    }

    @Override
    public void onTodoCheckedChange(String todo, boolean isChecked) {
        if (mTodoToast != null){
            mTodoToast.cancel();
        }

        String statusMessage = isChecked ? "COMPLETED" : "MARKED INCOMPLETE";
        mTodoToast =  Toast.makeText(this, statusMessage + ": " + todo, Toast.LENGTH_SHORT);
        mTodoToast.show();
    }
}
