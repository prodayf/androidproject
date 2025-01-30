package es.umh.dadm.db;

/**
 * Nada especial, clase pelicula con constructores getters y setters
 */
public class Movie {
    private int id;
    private int userId;
    private int platformId;
    private byte[] cover;
    private String title;
    private int duration;
    private String genre;
    private float rating;

    // Constructor
    public Movie(int id, int userId, int platformId, byte[] cover, String title, int duration, String genre, float rating) {
        this.id = id;
        this.userId = userId;
        this.platformId = platformId;
        this.cover = cover;
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.rating = rating;
    }

    // Getters
    public int getId() {
        return id;
    }


    public int getUserId() {
        return userId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public byte[] getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public float getRating() {
        return rating;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}