package ereferences.example.com;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewCategoryActivity extends AppCompatActivity {

    //Componenet Init
    private ListView categoryListView;
    private ArrayList<String> categoryNameList;
    private ProgressDialog progressDialog;

    //Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initView();

        categoryNameList = new ArrayList<>();
        final ArrayAdapter<String> categoryListArrayAdapter = new ArrayAdapter<String>(ViewCategoryActivity.this, R.layout.row_layout_category, R.id.row_layout_category_categoryname_tv, categoryNameList);
        categoryListView.setAdapter(categoryListArrayAdapter);

        progressDialog.show();
        mDatabase.child(AppConstant.FIREBASE_TABLE_CATEGORY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categoryList : dataSnapshot.getChildren()) {
                    for (DataSnapshot categoryName : categoryList.getChildren()) {
                        Log.e("TAG", categoryName.getValue() + "");
                        categoryNameList.add(categoryName.getValue().toString());
                    }
                }
                categoryListArrayAdapter.notifyDataSetChanged();
                progressDialog.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initView() {
        categoryListView = findViewById(R.id.activity_view_category_list_lv);

        progressDialog = new ProgressDialog(ViewCategoryActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("getting category name....");
    }
}
