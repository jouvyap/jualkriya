package bravostudio.nyeni.Model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PhotoDataModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("tanggal_buat")
    @Expose
    private String tanggalBuat;
    @SerializedName("dimensi")
    @Expose
    private String dimensi;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("link")
    @Expose
    private String link;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The judul
     */
    public String getJudul() {
        return judul;
    }

    /**
     *
     * @param judul
     * The judul
     */
    public void setJudul(String judul) {
        this.judul = judul;
    }

    /**
     *
     * @return
     * The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     * The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     * The medium
     */
    public String getMedium() {
        return medium;
    }

    /**
     *
     * @param medium
     * The medium
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     *
     * @return
     * The tanggalBuat
     */
    public String getTanggalBuat() {
        return tanggalBuat;
    }

    /**
     *
     * @param tanggalBuat
     * The tanggal_buat
     */
    public void setTanggalBuat(String tanggalBuat) {
        this.tanggalBuat = tanggalBuat;
    }

    /**
     *
     * @return
     * The dimensi
     */
    public String getDimensi() {
        return dimensi;
    }

    /**
     *
     * @param dimensi
     * The dimensi
     */
    public void setDimensi(String dimensi) {
        this.dimensi = dimensi;
    }

    /**
     *
     * @return
     * The harga
     */
    public String getHarga() {
        return harga;
    }

    /**
     *
     * @param harga
     * The harga
     */
    public void setHarga(String harga) {
        this.harga = harga;
    }

    /**
     *
     * @return
     * The link
     */
    public String getLink() {
        return link;
    }

    /**
     *
     * @param link
     * The link
     */
    public void setLink(String link) {
        this.link = link;
    }

}