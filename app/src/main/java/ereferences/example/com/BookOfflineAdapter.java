package ereferences.example.com;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class BookOfflineAdapter extends RecyclerView.Adapter<BookOfflineAdapter.ViewHolder> {

    ArrayList<DownloadBookModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    public BookOfflineAdapter(Context context, ArrayList<DownloadBookModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        DownloadBookModel item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.row_book_thumb_layout_book_name_tv);
            imageView = (ImageView) v.findViewById(R.id.row_book_thumb_layout_book_thumb_img);

        }

        public void setData(DownloadBookModel item) {
            this.item = item;


            textView.setText(item.getBookName());
            imageView.setImageBitmap(BitmapFactory.decodeFile(item.getBookThumb()));

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
        void onItemClick(DownloadBookModel item);
    }
}