package bravostudio.nyeni.Model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Feed {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("link")
    @Expose
    private List<String> link = new ArrayList<String>();
    @SerializedName("id")
    @Expose
    private List<String> id = new ArrayList<String>();

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
     * The link
     */
    public List<String> getLink() {
        return link;
    }

    /**
     *
     * @param link
     * The link
     */
    public void setLink(List<String> link) {
        this.link = link;
    }

    /**
     *
     * @return
     * The id
     */
    public List<String> getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(List<String> id) {
        this.id = id;
    }

}