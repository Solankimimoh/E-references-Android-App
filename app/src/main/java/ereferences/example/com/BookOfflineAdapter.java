package ereferences.example.com;

import android.content.Context;
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
            textView = (TextView) v.findViewById(R.id.row_layout_offline_books_list_book_img);
            imageView = (ImageView) v.findViewById(R.id.row_layout_offline_books_list_book_name_tv);

        }

        public void setData(DownloadBookModel item) {
            this.item = item;


            textView.setText(item.getBookName());
//            imageView.setImageResource(item.drawable);

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

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_layout_offline_books_list, parent, false);

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