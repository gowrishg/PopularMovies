package in.kudu.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VideoData {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("iso6391")
    @Expose
    public String iso6391;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("site")
    @Expose
    public String site;
    @SerializedName("size")
    @Expose
    public Integer size;
    @SerializedName("type")
    @Expose
    public String type;

}
