package model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

/***
 * LiveData 자료형 객체
 *
 * @author Kyeongrak Choi
 * @version 0.0.1
 */
public class ImageViewModel extends ViewModel {
    private MutableLiveData<List<Data>> mLiveData = new MutableLiveData<List<Data>>();
    public ImageViewModel() {
        mLiveData.setValue(new LinkedList<>());
    }

    public int getSize() {
        return mLiveData.getValue().size();
    }
    public void add(Data data) {
        mLiveData.getValue().add(data);
    }
    public Data get(int position) {
        return mLiveData.getValue().get(position);
    }
    public void clear() {
        mLiveData.getValue().clear();
    }

    public void addAll(List<Data> datas) {
        mLiveData.setValue(datas);
    }
}
