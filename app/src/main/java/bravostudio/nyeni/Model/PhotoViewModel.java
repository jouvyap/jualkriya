package bravostudio.nyeni.Model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PhotoViewModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<PhotoDataModel> data = new ArrayList<PhotoDataModel>();
    @SerializedName("like")
    @Expose
    private String like;
    @SerializedName("isLike")
    @Expose
    private Boolean isLike;

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     *
     * @param error
     * The error
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     *
     * @return
     * The data
     */
    public List<PhotoDataModel> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<PhotoDataModel> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The like
     */
    public String getLike() {
        return like;
    }

    /**
     *
     * @param like
     * The like
     */
    public void setLike(String like) {
        this.like = like;
    }

    /**
     *
     * @return
     * The isLike
     */
    public Boolean getIsLike() {
        return isLike;
    }

    /**
     *
     * @param isLike
     * The isLike
     */
    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

}