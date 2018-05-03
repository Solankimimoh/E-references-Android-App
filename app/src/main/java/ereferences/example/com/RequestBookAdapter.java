package ereferences.example.com;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class RequestBookAdapter extends RecyclerView.Adapter<RequestBookAdapter.ViewHolder> {

    ArrayList<RequestBookModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    public RequestBookAdapter(Context context, ArrayList<RequestBookModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView bookReviewAuthorTv;
        public TextView bookReviewCommentTv;
        RequestBookModel item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            bookReviewAuthorTv = (TextView) v.findViewById(R.id.row_layout_book_review_author_tv);
            bookReviewCommentTv = (TextView) v.findViewById(R.id.row_layout_book_review_comment_tv);

        }

        public void setData(RequestBookModel item) {
            this.item = item;


            bookReviewAuthorTv.setText(item.getRequesterName());
            bookReviewCommentTv.setText(item.getRequestBookName());

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

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_layout_book_review, parent, false);

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
        void onItemClick(RequestBookModel item);
    }
}