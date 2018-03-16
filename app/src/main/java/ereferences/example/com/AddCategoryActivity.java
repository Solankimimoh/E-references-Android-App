package ereferences.example.com;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    //validation plugin init
    AwesomeValidation mAwesomeValidation = new AwesomeValidation(COLORATION);

    //component declaration
    private EditText categoryNameEd;
    private Button categoryInsertBtn;

    //    Firebase Init
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        mDatabase = FirebaseDatabase.getInstance().getReference(AppConstant.FIREBASE_TABLE_CATEGORY);

        initView();

//        initValidationRules();


    }

    private void initValidationRules() {
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        mAwesomeValidation.addValidation(AddCategoryActivity.this, R.id.activity_signup_registration_email_ed, android.util.Patterns.EMAIL_ADDRESS, R.string.val_err_email);

    }

    private void initView() {
        //        componenet init
        categoryNameEd = findViewById(R.id.activity_add_category_name_ed);
        categoryInsertBtn = findViewById(R.id.activity_add_category_add_btn);

        //        progressbar init
        progressDialog = new ProgressDialog(AddCategoryActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Insert Category");
        progressDialog.setMessage("Inserting name......");


        //        listener init
        categoryInsertBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_add_category_add_btn:
                insertCategoryToDB();
                break;
        }
    }

    private void insertCategoryToDB() {

        progressDialog.show();

        final String categoryName = categoryNameEd.getText().toString().trim();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
        } else {

            mDatabase.push()
                    .setValue(new CategoryModel(categoryName)
                            , new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(AddCategoryActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddCategoryActivity.this, "Category Inserted ! Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                        progressDialog.hide();

                                    }
                                }
                            });


        }


    }
}
