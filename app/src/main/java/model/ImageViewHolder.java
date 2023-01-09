package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.darklake.myapplication.R;

/***
 * ImageViewHolder 객체를 만들고 View binding 한다.
 *
 * @author Kyeongrak Choi
 * @version 0.0.1
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {
    private Context mContext = null;
    private ImageView mImage = null;
    private TextView mTitle = null;
    private CheckBox mCheckBox = null;

    public Context getContext() {
        return mContext;
    }
    public CheckBox getCheckBox() {
        return mCheckBox;
    }
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mImage = (ImageView) itemView.findViewById(R.id.image);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.cb_like);
    }

    public void setData(Data data) {
        mTitle.setText(data.getImageTitle());
        Glide.with(mContext).load(data.getmImageUrl()).into(mImage);
    }

    public static ImageViewHolder createView(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        return new ImageViewHolder(view);
    }
}
