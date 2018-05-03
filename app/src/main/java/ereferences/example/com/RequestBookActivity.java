package ereferences.example.com;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestBookActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText requestBookNameEd;
    private Button requestBookBtn;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initView();

    }

    private void initView() {
        requestBookNameEd = findViewById(R.id.activity_request_book_ed);
        requestBookBtn = findViewById(R.id.activity_request_book_btn);


        requestBookBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_request_book_btn:
                requestBook();
                break;
        }

    }

    private void requestBook() {

        final String requestBookName = requestBookNameEd.getText().toString().trim();
        final String requesterName = firebaseAuth.getCurrentUser().getEmail().replace("@gmail.com", "");
        if (requestBookName.isEmpty()) {
            Toast.makeText(this, "Please Enter Book Name", Toast.LENGTH_SHORT).show();
        } else {
            RequestBookModel requestBookModel = new RequestBookModel(requesterName, requestBookName);
            databaseReference.child(AppConstant.FIREBASE_TABLE_REQUEST_BOOK).push().setValue(requestBookModel).addOnCompleteListener(RequestBookActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RequestBookActivity.this, "Thanks for Requesting ! Admin check your Request", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }
}
