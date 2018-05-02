package ereferences.example.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;

public class BookReaderActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener, OnDrawListener {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        Intent intent = getIntent();
        File file = new File(intent.getStringExtra("KEY_BOOK"));
        Log.e("EXIST", file.getPath() + "---" + file.getName());
        PDFView pdfView = (PDFView) findViewById(R.id.bookpdfviewer);

        progressDialog = new ProgressDialog(BookReaderActivity.this);
        progressDialog.setTitle("Book Reader");
        progressDialog.setMessage("Loading......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableSwipe(true)
                .onLoad(this)
                .spacing(20)
                .onDraw(this)
                .onPageChange(this)
                .load();

    }

    @Override
    public void loadComplete(int nbPages) {

        progressDialog.dismiss();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Toast.makeText(this, (page + 1) + " / " + pageCount, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }
}
