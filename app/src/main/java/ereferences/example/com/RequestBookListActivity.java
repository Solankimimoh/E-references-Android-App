package ereferences.example.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestBookListActivity extends AppCompatActivity implements RequestBookAdapter.ItemListener {

    private RecyclerView requestBookListRecyclerView;
    private ArrayList<RequestBookModel> requestBookModelArrayList;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book_list);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initView();

        final RequestBookAdapter requestBookAdapter = new RequestBookAdapter(RequestBookListActivity.this, requestBookModelArrayList, this);
        requestBookListRecyclerView.setAdapter(requestBookAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RequestBookListActivity.this, LinearLayoutManager.VERTICAL, true);
        requestBookListRecyclerView.setLayoutManager(layoutManager);
        requestBookListRecyclerView.addItemDecoration(new DividerItemDecoration(RequestBookListActivity.this,
                DividerItemDecoration.VERTICAL));

        databaseReference.child(AppConstant.FIREBASE_TABLE_REQUEST_BOOK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot requestBookList : dataSnapshot.getChildren()) {
                    RequestBookModel requestBookModel = new RequestBookModel();
                    requestBookModel.setRequesterName(requestBookList.child("requesterName").getValue().toString());
                    requestBookModel.setRequestBookName(requestBookList.child("requestBookName").getValue().toString());
                    requestBookModelArrayList.add(requestBookModel);
                    requestBookAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void initView() {
        requestBookListRecyclerView = findViewById(R.id.activity_request_book_list_rc);
        requestBookModelArrayList = new ArrayList<>();
    }

    @Override
    public void onItemClick(RequestBookModel item) {

    }
}
