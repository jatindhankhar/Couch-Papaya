package in.jatindhankhar.couchpapaya;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatin on 13/6/16.
 */
public class ReviewFactory {


    @SerializedName("id")
    private String mId;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    @SerializedName("url")
    private String mUrl;



    public ReviewFactory(String id, String author, String content, String url) {
        this.mId = id;
        this.mAuthor = author;
        this.mContent = content;
        this.mUrl = url;
    }

    public String getId()
    {
        return this.mId;
    }

    public String getAuthor() {
        return this.mAuthor;
    }

    public String getContent()
    {
        return  this.mContent;
    }

    public String getUrl()
    {
        return  this.mContent;
    }

}