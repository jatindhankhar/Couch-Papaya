package in.jatindhankhar.couchpapaya;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatin on 13/6/16.
 */
public class TrailerFactory {
    @SerializedName("id")
    private String mId;

    @SerializedName("key")
    private String mKey;

    @SerializedName("name")
    private String mName;


    public TrailerFactory(String id,String key,String name)
    {
        this.mId = id;
        this.mKey = key;
        this.mName = name;
    }

    public String getId()
    {
        return  this.mId;
    }

    public String getKey()
    {
        return this.mKey;
    }

    public String getName()
    {
        return this.mName;
    }

}
