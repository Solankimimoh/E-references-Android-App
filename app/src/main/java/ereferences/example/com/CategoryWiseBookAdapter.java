package ereferences.example.com;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class CategoryWiseBookAdapter extends RecyclerView.Adapter<CategoryWiseBookAdapter.ViewHolder> {

    ArrayList<BookDataModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    public CategoryWiseBookAdapter(Context context, ArrayList<BookDataModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        BookDataModel item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.row_book_thumb_layout_book_name_tv);
            imageView = (ImageView) v.findViewById(R.id.row_book_thumb_layout_book_thumb_img);

        }

        public void setData(BookDataModel item) {
            this.item = item;



            if (!item.getBookUrl().isEmpty() && !item.getBookName().isEmpty()) {
                textView.setText(item.getBookName());


                Log.e("URL", item.getBookName() + "sdfsdfdfdfdfdf");
                Glide.with(mContext).load(item.getThumbUrl())
                        .placeholder(R.drawable.no_thumb)
                        .crossFade()
                        .error(android.R.color.holo_red_light)
                        .fallback(android.R.color.holo_orange_light)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_book_thumb_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(BookDataModel item);
    }
}