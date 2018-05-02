package ereferences.example.com;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.folioreader.FolioReader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView bookNameTv;
    private TextView bookCategoryTv;
    private Button bookDownloadOfflineBtn;
    private Button bookOnlineReadBtn;
    private ImageView bookThumbImg;
    private Intent bookDetailsIntent;
    private FolioReader folioReader;
    private String bookUrl;
    private String bookThumbUrl;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        initview();

        setData();
    }

    private void setData() {

        if (bookDetailsIntent.getExtras() != null) {
            bookNameTv.setText(bookDetailsIntent.getStringExtra(AppConstant.KEY_BOOK_NAME));
            bookCategoryTv.setText(bookDetailsIntent.getStringExtra(AppConstant.KEY_BOOK_CATEGORY));

            Glide.with(BookDetailsActivity.this).load(bookDetailsIntent.getStringExtra(AppConstant.KEY_BOOK_THUMB))
                    .placeholder(R.drawable.no_thumb)
                    .crossFade()
                    .error(android.R.color.holo_red_light)
                    .fallback(android.R.color.holo_orange_light)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bookThumbImg);

            bookUrl = bookDetailsIntent.getStringExtra(AppConstant.KEY_BOOK_URL);


        }


    }

    private void initview() {
        bookNameTv = findViewById(R.id.activity_book_details_book_name_tv);
        bookCategoryTv = findViewById(R.id.activity_book_details_book_category);
        bookDownloadOfflineBtn = findViewById(R.id.activity_book_details_download_btn);
        bookOnlineReadBtn = findViewById(R.id.activity_book_details_online_read_btn);
        bookThumbImg = findViewById(R.id.activity_book_details_thumb_img);
        bookOnlineReadBtn = findViewById(R.id.activity_book_details_online_read_btn);

        folioReader = FolioReader.getInstance(BookDetailsActivity.this);

        bookDetailsIntent = getIntent();

        bookOnlineReadBtn.setOnClickListener(this);
        bookDownloadOfflineBtn.setOnClickListener(this);

        progressDialog = new ProgressDialog(BookDetailsActivity.this);
        progressDialog.setTitle("Opening Book");
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_book_details_online_read_btn:
                readBookOnline();
                break;
            case R.id.activity_book_details_download_btn:
                downloadBook();
        }
    }

    private void downloadBook() {

        progressDialog.setTitle("Book Dowload offline");
        progressDialog.setMessage("Book downloading.....");
        progressDialog.show();


        File booksDir = new File(getCacheDir(), "books");
        booksDir.mkdir();
        final File thumbDir = new File(getCacheDir(), "thumbs");
        thumbDir.mkdir();

        Log.e("DOWNLOAD", booksDir.getPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        } //creating an intent for file chooser


        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://e-references.appspot.com");
        final StorageReference pathReference = storageRef.child(AppConstant.STORAGE_PATH_UPLOADS_BOOKS + bookNameTv.getText().toString() + ".pdf");
        final StorageReference thumbPathReference = storageRef.child(AppConstant.STORAGE_PATH_UPLOADS_THUMBNAIL + bookNameTv.getText().toString() + ".png");

        final File myFile = new File(booksDir, bookNameTv.getText().toString() + ".pdf");
        final File thumbFile = new File(thumbDir, bookNameTv.getText().toString() + ".png");
        pathReference.getFile(myFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Toast.makeText(BookDetailsActivity.this, "dggdfgfgdg : " + myFile.getPath(), Toast.LENGTH_SHORT).show();

                thumbPathReference.getFile(thumbFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(BookDetailsActivity.this, "Book Downloded Offline", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(BookDetailsActivity.this, "FAIL : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readBookOnline() {
        progressDialog.show();
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            } //creating an intent for file chooser


            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://e-references.appspot.com");
            final StorageReference pathReference = storageRef.child(AppConstant.STORAGE_PATH_UPLOADS_BOOKS + bookNameTv.getText().toString() + ".pdf");

            File outputDir = getCacheDir();
            final File myFile = File.createTempFile(bookNameTv.getText().toString(), ".pdf", outputDir);

            pathReference.getFile(myFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Toast.makeText(BookDetailsActivity.this, "dggdfgfgdg : " + myFile.getPath(), Toast.LENGTH_SHORT).show();
                    Log.e("FILEPATH", myFile.getPath() + " ---" + myFile.getName());
                    Intent gotoBookReader = new Intent(BookDetailsActivity.this, BookReaderActivity.class);
                    gotoBookReader.putExtra("KEY_BOOK", myFile.getPath());
                    progressDialog.dismiss();
                    startActivity(gotoBookReader);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(BookDetailsActivity.this, "FAIL : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
