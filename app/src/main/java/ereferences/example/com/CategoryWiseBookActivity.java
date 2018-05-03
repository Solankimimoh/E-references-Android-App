package ereferences.example.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryWiseBookActivity extends AppCompatActivity implements CategoryWiseBookAdapter.ItemListener {


    private RecyclerView categoryWiseBookRecyclerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayList<BookDataModel> bookDataModelArrayList;
    private String categoryPushKey;
    private String categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_book);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        initView();
        final Intent intent = getIntent();

        final CategoryWiseBookAdapter categoryWiseBookAdapter = new CategoryWiseBookAdapter(CategoryWiseBookActivity.this, bookDataModelArrayList, this);
        categoryWiseBookRecyclerView.setAdapter(categoryWiseBookAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(CategoryWiseBookActivity.this, 2);
        categoryWiseBookRecyclerView.setLayoutManager(layoutManager);

        if (intent.hasExtra("KEY_CATEGORY_NAME") && intent.hasExtra("KEY_CATEGORY_PUSH_KEY")) {
            categoryPushKey = intent.getStringExtra("KEY_CATEGORY_PUSH_KEY");
            categoryName = intent.getStringExtra("KEY_CATEGORY_NAME");


            databaseReference.child(AppConstant.FIREBASE_TABLE_BOOK).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot bookListSnapshot : dataSnapshot.getChildren()) {
                        if (bookListSnapshot.child(AppConstant.FIREBASE_TABLE_CATEGORY).getValue().equals(categoryPushKey)) {
                            BookDataModel bookDataModel = new BookDataModel();
                            bookDataModel.setBookKey(bookListSnapshot.getKey());
                            bookDataModel.setBookName(bookListSnapshot.child("bookName").getValue().toString());
                            bookDataModel.setBookUrl(bookListSnapshot.child("bookUrl").getValue().toString());
                            bookDataModel.setCategory(bookListSnapshot.child("category").getValue().toString());
                            bookDataModel.setThumbUrl(bookListSnapshot.child("thumbUrl").getValue().toString());
                            bookDataModelArrayList.add(bookDataModel);
                            categoryWiseBookAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CategoryWiseBookActivity.this, "No Books Available for this category", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    private void initView() {
        categoryWiseBookRecyclerView = findViewById(R.id.activity_category_wise_book_rc);
        bookDataModelArrayList = new ArrayList<>();
    }

    @Override
    public void onItemClick(BookDataModel item) {

        final Intent gotoBookDetails = new Intent(CategoryWiseBookActivity.this, BookDetailsActivity.class);

        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_PUSH_KEY, item.getBookKey());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_NAME, item.getBookName());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_CATEGORY, categoryName);
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_URL, item.getBookUrl());
        gotoBookDetails.putExtra(AppConstant.KEY_BOOK_THUMB, item.getThumbUrl());

        startActivity(gotoBookDetails);

    }
}
