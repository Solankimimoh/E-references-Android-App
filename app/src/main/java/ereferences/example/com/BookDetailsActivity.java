package ereferences.example.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class BookDetailsActivity extends AppCompatActivity {

    private TextView bookNameTv;
    private TextView bookCategoryTv;
    private Button bookDownloadOfflineBtn;
    private Button bookOnlineReadBtn;
    private ImageView bookThumbImg;
    private Intent bookDetailsIntent;

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


        }


    }

    private void initview() {
        bookNameTv = findViewById(R.id.activity_book_details_book_name_tv);
        bookCategoryTv = findViewById(R.id.activity_book_details_book_category);
        bookDownloadOfflineBtn = findViewById(R.id.activity_book_details_download_btn);
        bookOnlineReadBtn = findViewById(R.id.activity_book_details_online_read_btn);
        bookThumbImg = findViewById(R.id.activity_book_details_thumb_img);

        bookDetailsIntent = getIntent();

    }


}
