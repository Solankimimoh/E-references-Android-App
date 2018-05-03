package ereferences.example.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

public class BookOfflineActivity extends AppCompatActivity implements BookOfflineAdapter.ItemListener {

    private RecyclerView recyclerView;
    private ArrayList<DownloadBookModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_offline);

        initView();

        File directory = new File(getCacheDir().getPath() + "/thumbs");
        Log.e("Files", "Size: " + directory.getPath());

        File[] files = directory.listFiles();

        if (files != null) {

            Log.e("Files", "Size: " + files.length);

            for (int i = 0; i < files.length; i++) {
                Log.e("Files", "FileName:" + directory.getPath() + "/" + files[i].getName());
                Log.e("Files", "FileName:" + FilenameUtils.removeExtension(files[i].getName()));
                DownloadBookModel downloadBookModel = new DownloadBookModel(FilenameUtils.removeExtension(files[i].getName()), directory.getPath() + "/" + files[i].getName());
                arrayList.add(downloadBookModel);
            }
        } else {
            Toast.makeText(this, "No Offline Books Available", Toast.LENGTH_SHORT).show();
        }

        BookOfflineAdapter bookOfflineAdapter = new BookOfflineAdapter(BookOfflineActivity.this, arrayList, this);

        recyclerView.setAdapter(bookOfflineAdapter);



        recyclerView.setLayoutManager(new GridLayoutManager(BookOfflineActivity.this, 2));

    }

    private void initView() {
        recyclerView = findViewById(R.id.activity_book_offline_books_list_rc);
        arrayList = new ArrayList<>();

    }

    @Override
    public void onItemClick(DownloadBookModel item) {

        Log.e("PATH", getCacheDir().getPath() + "/books/" + item.getBookName() + ".pdf");
        final String path = getCacheDir().getPath() + "/books/" + item.getBookName() + ".pdf";

        Intent gotoBookReader = new Intent(BookOfflineActivity.this, BookReaderActivity.class);
        gotoBookReader.putExtra("KEY_BOOK", path);
        startActivity(gotoBookReader);

    }
}
