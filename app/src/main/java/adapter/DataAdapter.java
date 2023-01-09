package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.Data;
import model.ImageViewHolder;
import model.ImageViewModel;


/***
 * 서버로부터 Data 를 받아 List<Data> 를 전달한다.
 *
 * @author Kyeongrak Choi
 * @version 0.0.1
 * @date 2023.01.09
 */
public class DataAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private ImageViewModel mDataList = null;
    public DataAdapter(ImageViewModel dataList) {
        super();
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ImageViewHolder.createView(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Data data = get(position);
        holder.setData(data);

        // Image url 을 기준으로 "좋아요" 등록을 한다.
        SharedPreferences sp = holder.getContext().getSharedPreferences("MY", Context.MODE_PRIVATE);
        CheckBox cb = holder.getCheckBox();
        cb.setChecked(sp.getBoolean(data.getmImageUrl(), false));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean fromUser) {
                if (fromUser) {
                    SharedPreferences sp = holder.getContext().getSharedPreferences("MY", Context.MODE_PRIVATE);
                    SharedPreferences.Editor et = sp.edit();
                    et.putBoolean(data.getmImageUrl(), compoundButton.isChecked());
                    et.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.getSize();
    }

    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(getItemCount());
    }
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }
    public Data get(int position) {
        return mDataList.get(position);
    }

    public void addAll(List<Data> datas) {
        for (Data data : datas) {
            mDataList.add(data);
        }
        notifyDataSetChanged();
    }
}
