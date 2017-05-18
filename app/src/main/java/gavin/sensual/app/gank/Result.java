package gavin.sensual.app.gank;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import gavin.sensual.app.base.Image;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/6
 */
public class Result implements Serializable {

    @SerializedName("error")
    private boolean error;
    @SerializedName("results")
    private List<Image> results;

    public boolean isError() {
        return error;
    }

    public List<Image> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "Result{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
