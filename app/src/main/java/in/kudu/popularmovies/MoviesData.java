
package in.kudu.popularmovies;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesData {

    @SerializedName("page")
    @Expose
    public Integer page;
    @SerializedName("results")
    @Expose
    public List<MovieData> results = new ArrayList<MovieData>();

}
