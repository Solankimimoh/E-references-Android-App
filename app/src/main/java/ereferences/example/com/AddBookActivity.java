package ereferences.example.com;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddBookActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    //This is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    //Component init
    private Spinner categoryListSpinner;
    private EditText bookTitleEd;
    private Button uploadBookBtn;
    private TextView textViewStatus;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private ArrayList<String> categoryNameList;

    //The firebase objects for storage and database
    StorageReference mStorageReference;
    private DatabaseReference mDatabase;


    //    variable
    private String category;
    private String bookUrl;
    private String thumbUrl;
    private Map<String, String> uploadData = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        //getting firebase objects
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        initView();
        categoryNameList = new ArrayList<>();
        final ArrayAdapter<String> categoryListArrayAdapter = new ArrayAdapter<String>(AddBookActivity.this, R.layout.row_layout_category, R.id.row_layout_category_categoryname_tv, categoryNameList);
        categoryListSpinner.setAdapter(categoryListArrayAdapter);

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

        //component widgets
        categoryListSpinner = findViewById(R.id.activity_add_book_category_sp);
        bookTitleEd = findViewById(R.id.activity_add_book_title_ed);
        uploadBookBtn = findViewById(R.id.activity_add_book_upload_btn);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        progressDialog = new ProgressDialog(AddBookActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("getting category bookName....");

//        listener init
        uploadBookBtn.setOnClickListener(this);
        categoryListSpinner.setOnItemSelectedListener(this);

    }

    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        } //creating an intent for file chooser

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        } //creating an intent for file chooser

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadPdfFile(data.getData(), 0);
                generateImageFromPdf(data.getData());
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it
    private void uploadPdfFile(final Uri data, final int code) {
        progressBar.setVisibility(View.VISIBLE);

        final StorageReference sRef = FirebaseStorage.getInstance().getReference().child(AppConstant.STORAGE_PATH_UPLOADS_BOOKS + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File upload successfully");
                        bookUrl = taskSnapshot.getDownloadUrl().toString();
                        Log.e("BOOK", taskSnapshot.getDownloadUrl().toString());
                        uploadData.put("bookTitle", bookTitleEd.getText().toString().trim());
                        uploadData.put("bookUrl", taskSnapshot.getDownloadUrl().toString());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.e("TAG UPLOAD", taskSnapshot.getBytesTransferred() + "  " + progress + "====" + (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        textViewStatus.setText("" + progress + "% Uploading...");
                    }
                });

    }


    private void uploadThumbFile(final Uri data) {
        progressBar.setVisibility(View.VISIBLE);

        final StorageReference sRef = FirebaseStorage.getInstance().getReference().child(AppConstant.STORAGE_PATH_UPLOADS_THUMBNAIL + System.currentTimeMillis() + ".png");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File upload successfully");
                        thumbUrl = taskSnapshot.getDownloadUrl().toString();

                        uploadData.put("category", category);
                        uploadData.put("thumbUrl", taskSnapshot.getDownloadUrl().toString());
                        Log.e("BOOK", uploadData + "");
//                        Upload upload1= new Upload(upload.getCategory(),)
                        mDatabase.child(AppConstant.FIREBASE_TABLE_BOOK).child(mDatabase.push().getKey()).setValue(uploadData);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.e("TAG UPLOAD", taskSnapshot.getBytesTransferred() + "  " + progress + "====" + (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        //   textViewStatus.setText("" + progress + "% Uploading...");
                    }
                });

    }

    void generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            //todo with exception
        }
    }

    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";

    private void saveImage(Bitmap bmp) {
        Log.e("LOCATION", FOLDER);
        FileOutputStream out = null;
        try {
            File folder = new File(FOLDER);
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(folder, random() + ".png");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            uploadThumbFile(Uri.fromFile(file));

        } catch (Exception e) {
            //todo with exception
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                //todo with exception
            }
        }
    }

    public static String random() {
        final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_book_upload_btn:
                getPDF();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        final String selectedItem;
        selectedItem = parent.getSelectedItem().toString();

        mDatabase.child(AppConstant.FIREBASE_TABLE_CATEGORY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot categoryList : dataSnapshot.getChildren()) {
                    for (DataSnapshot categoryName : categoryList.getChildren()) {
                        Log.e("TAG", categoryName.getValue() + "");
                        if (categoryName.getValue().toString().equals(selectedItem)) {
                            category = categoryList.getKey();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
