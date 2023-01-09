package model;

/***
 * 서버로부터 받아온 Image, Title 객체
 *
 * @author Kyeongrak Choi
 * @version 0.0.1
 */
public class ImageData implements Data {
    private String mImageUrl = null;
    private String mImageTitle = null;

    @Override
    public void setData(String imageUrl, String imageTitle) {
        mImageUrl = imageUrl;
        mImageTitle = imageTitle;
    }

    @Override
    public String getmImageUrl() {
        return mImageUrl;
    }

    @Override
    public String getImageTitle() {
        return mImageTitle;
    }
}
