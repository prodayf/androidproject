package es.umh.dadm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // variaibles para las tablas de bbdd
    private static final String DATABASE_NAME = "BDPelis.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDOS = "apellidos";
    public static final String COLUMN_FECHA_NACIMIENTO = "fecha_nacimiento";
    public static final String COLUMN_PREGUNTA_SEGURIDAD = "pregunta_seguridad";
    public static final String COLUMN_RESPUESTA_SEGURIDAD = "respuesta_seguridad";
    public static final String COLUMN_INTERESES = "intereses";


    // TABLA PLATAFORMAS

    public static final String TABLE_PLATFORMS = "plataformas";
    public static final String COLUMN_PLATFORM_ID = "id";
    public static final String COLUMN_USER_ID_PLATFORM = "user_id";
    public static final String COLUMN_PLATFORM_IMAGE = "imagen";
    public static final String COLUMN_PLATFORM_NAME = "nombre";
    public static final String COLUMN_PLATFORM_URL = "url";
    public static final String COLUMN_PLATFORM_USERNAME = "usuario";
    public static final String COLUMN_PLATFORM_PASSWORD = "contrasena";


    // TABBLA PELICULAS

    public static final String TABLE_MOVIES = "peliculas";
    public static final String COLUMN_MOVIE_ID = "id";
    public static final String COLUMN_USER_ID_MOVIE = "user_id";
    public static final String COLUMN_PLATFORM_ID_MOVIE = "platform_id";
    public static final String COLUMN_MOVIE_COVER = "caratula";
    public static final String COLUMN_MOVIE_TITLE = "titulo";
    public static final String COLUMN_MOVIE_DURATION = "duracion";
    public static final String COLUMN_MOVIE_GENRE = "genero";
    public static final String COLUMN_MOVIE_RATING = "calificacion";


    //los querys para crear las tablas de las bases de datos.

    //me gusta mas execSql que rawSql
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creacion de las tablas y sus relaciones
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT NOT NULL, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_APELLIDOS + " TEXT, " +
                COLUMN_FECHA_NACIMIENTO + " DATE, " +
                COLUMN_PREGUNTA_SEGURIDAD + " TEXT, " +
                COLUMN_RESPUESTA_SEGURIDAD + " TEXT, " +
                COLUMN_INTERESES + " TEXT)";
        db.execSQL(CREATE_TABLE_QUERY);

        String CREATE_PLATFORMS_TABLE_QUERY = "CREATE TABLE " + TABLE_PLATFORMS + " (" +
                COLUMN_PLATFORM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID_PLATFORM + " INTEGER, " +
                COLUMN_PLATFORM_IMAGE + " BLOB, " +
                COLUMN_PLATFORM_NAME + " TEXT, " +
                COLUMN_PLATFORM_URL + " TEXT, " +
                COLUMN_PLATFORM_USERNAME + " TEXT, " +
                COLUMN_PLATFORM_PASSWORD + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_USER_ID_PLATFORM + ") REFERENCES " +
                TABLE_NAME + "(" + COLUMN_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_PLATFORMS_TABLE_QUERY);

        String CREATE_MOVIES_TABLE_QUERY = "CREATE TABLE " + TABLE_MOVIES + " (" +
                COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID_MOVIE + " INTEGER, " +
                COLUMN_PLATFORM_ID_MOVIE + " INTEGER, " +
                COLUMN_MOVIE_COVER + " BLOB, " +
                COLUMN_MOVIE_TITLE + " TEXT, " +
                COLUMN_MOVIE_DURATION + " INTEGER, " +
                COLUMN_MOVIE_GENRE + " TEXT, " +
                COLUMN_MOVIE_RATING + " REAL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID_MOVIE + ") REFERENCES " +
                TABLE_NAME + "(" + COLUMN_ID + ") ON DELETE CASCADE, " + // Modificación aquí
                "FOREIGN KEY (" + COLUMN_PLATFORM_ID_MOVIE + ") REFERENCES " +
                TABLE_PLATFORMS + "(" + COLUMN_PLATFORM_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_MOVIES_TABLE_QUERY);






    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLATFORMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    //funcion para insertar usuarios
    public void insertarUsuario(String email, String nombre, String apellidos, String fechaNacimiento,
                                String preguntaSeguridad, String respuestaSeguridad, String intereses) {
        // Obtiene una instancia de la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Query de inserción
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_EMAIL + ", " +
                COLUMN_NOMBRE + ", " +
                COLUMN_APELLIDOS + ", " +
                COLUMN_FECHA_NACIMIENTO + ", " +
                COLUMN_PREGUNTA_SEGURIDAD + ", " +
                COLUMN_RESPUESTA_SEGURIDAD + ", " +
                COLUMN_INTERESES +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Ejecuta la consulta de inserción
        db.execSQL(insertQuery, new String[]{email, nombre, apellidos, fechaNacimiento,
                preguntaSeguridad, respuestaSeguridad, intereses});

        // Cierra la conexión con la base de datos
        db.close();
    }



    //funcion para insertar plataforams
    public void insertarPlataforma(int id, byte[] imagen, String nombre, String url, String usuario, String password) {
        // Obtiene una instancia de la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Query de inserción
        String insertQuery = "INSERT INTO " + TABLE_PLATFORMS + " (" +
                COLUMN_USER_ID_PLATFORM + ", " +
                COLUMN_PLATFORM_IMAGE + ", " +
                COLUMN_PLATFORM_NAME + ", " +
                COLUMN_PLATFORM_URL + ", " +
                COLUMN_PLATFORM_USERNAME + ", " +
                COLUMN_PLATFORM_PASSWORD +
                ") VALUES (?, ?, ?, ?, ?, ?)";

        // Ejecuta la consulta de inserción
        db.execSQL(insertQuery, new Object[]{id, imagen, nombre, url, usuario, password});

        // Cierra la conexión con la base de datos
        db.close();
    }

    //funcion que me devuelve todas las plataformas
    public List<Platform> getAllPlatforms() {
        List<Platform> platforms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PLATFORMS + " ORDER BY " + COLUMN_PLATFORM_NAME + " ASC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex(COLUMN_PLATFORM_ID);
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID_PLATFORM);
            int nameIndex = cursor.getColumnIndex(COLUMN_PLATFORM_NAME);
            int imageUrlIndex = cursor.getColumnIndex(COLUMN_PLATFORM_IMAGE);
            int urlIndex = cursor.getColumnIndex(COLUMN_PLATFORM_URL);
            int usernameIndex = cursor.getColumnIndex(COLUMN_PLATFORM_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PLATFORM_PASSWORD);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                int userId = cursor.getInt(userIdIndex);
                String name = cursor.getString(nameIndex);
                byte[] imageData = cursor.getBlob(imageUrlIndex); // Obtener el BLOB correctamente
                String url = cursor.getString(urlIndex);
                String username = cursor.getString(usernameIndex);
                String password = cursor.getString(passwordIndex);

                // Crear una nueva instancia de Platform con los datos obtenidos
                Platform platform = new Platform(id, userId, name, imageData, url, username, password);
                platforms.add(platform);
            }
            cursor.close();
        }

        return platforms;
    }

    //funcion que me devuelve las plataformas con cierto ID
    public Platform getPlatformById(int platformId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PLATFORMS + " WHERE " + COLUMN_PLATFORM_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(platformId)});

        Platform platform = null;
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID_PLATFORM);
            int nameIndex = cursor.getColumnIndex(COLUMN_PLATFORM_NAME);
            int imageUrlIndex = cursor.getColumnIndex(COLUMN_PLATFORM_IMAGE);
            int urlIndex = cursor.getColumnIndex(COLUMN_PLATFORM_URL);
            int usernameIndex = cursor.getColumnIndex(COLUMN_PLATFORM_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PLATFORM_PASSWORD);

            if (userIdIndex >= 0 && nameIndex >= 0 && imageUrlIndex >= 0 && urlIndex >= 0 && usernameIndex >= 0 && passwordIndex >= 0) {
                int userId = cursor.getInt(userIdIndex);
                String name = cursor.getString(nameIndex);
                byte[] imageData = cursor.getBlob(imageUrlIndex);
                String url = cursor.getString(urlIndex);
                String username = cursor.getString(usernameIndex);
                String password = cursor.getString(passwordIndex);

                platform = new Platform(platformId, userId, name, imageData, url, username, password);
            }
            cursor.close();
        }

        return platform;
    }

    //delete de plataformas. deberia funcionar el cascade, pero realmente no lo hace...
    public void deletePlatform(int platformId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLATFORMS, COLUMN_PLATFORM_ID + " = ?", new String[]{String.valueOf(platformId)});
        db.close();
    }







    //parte de peliculas


    //insert de peliculas
    public void insertMovie(int userId, int platformId, byte[] coverImage, String title, int duration, String genre, float rating) {
        // Obtiene una instancia de la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Query de inserción
        String insertQuery = "INSERT INTO " + TABLE_MOVIES + " (" +
                COLUMN_USER_ID_MOVIE + ", " +
                COLUMN_PLATFORM_ID_MOVIE + ", " +
                COLUMN_MOVIE_COVER + ", " +
                COLUMN_MOVIE_TITLE + ", " +
                COLUMN_MOVIE_DURATION + ", " +
                COLUMN_MOVIE_GENRE + ", " +
                COLUMN_MOVIE_RATING +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Ejecuta la consulta de inserción
        db.execSQL(insertQuery, new Object[]{userId, platformId, coverImage, title, duration, genre, rating});

        // Cierra la conexión con la base de datos
        db.close();
    }

    //devolver todas las peliculas
    public List<Movie> getAllMovies() {
        List<Movie> peliculas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIES + " ORDER BY " + COLUMN_MOVIE_TITLE + " ASC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex(COLUMN_MOVIE_ID);
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID_MOVIE);
            int platformIdIndex = cursor.getColumnIndex(COLUMN_PLATFORM_ID_MOVIE);
            int coverIndex = cursor.getColumnIndex(COLUMN_MOVIE_COVER);
            int titleIndex = cursor.getColumnIndex(COLUMN_MOVIE_TITLE);
            int durationIndex = cursor.getColumnIndex(COLUMN_MOVIE_DURATION);
            int genreIndex = cursor.getColumnIndex(COLUMN_MOVIE_GENRE);
            int ratingIndex = cursor.getColumnIndex(COLUMN_MOVIE_RATING);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                int userId = cursor.getInt(userIdIndex);
                int platformId = cursor.getInt(platformIdIndex);
                byte[] cover = cursor.getBlob(coverIndex); // Obtener el BLOB correctamente
                String title = cursor.getString(titleIndex);
                int duration = cursor.getInt(durationIndex);
                String genre = cursor.getString(genreIndex);
                float rating = cursor.getFloat(ratingIndex);

                // Crear una nueva instancia de Pelicula con los datos obtenidos
                Movie pelicula = new Movie(id, userId, platformId, cover, title, duration, genre, rating);
                peliculas.add(pelicula);
            }
            cursor.close();
        }

        return peliculas;
    }

    //delete de peliculas cuando se da un ID
    public void deleteMoviesByPlatform(int platformId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, COLUMN_PLATFORM_ID_MOVIE + " = ?", new String[]{String.valueOf(platformId)});
        db.close();
    }

    public void deleteMovie(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movieId)});
        db.close();
    }

    public List<Movie> getMoviesByPlatformAndUser(int platformId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIES + " WHERE " +
                COLUMN_PLATFORM_ID_MOVIE + " = ? AND " + COLUMN_USER_ID_MOVIE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(platformId), String.valueOf(userId)});

        List<Movie> movieList = new ArrayList<>();

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(COLUMN_MOVIE_ID);
                    int coverIndex = cursor.getColumnIndex(COLUMN_MOVIE_COVER);
                    int titleIndex = cursor.getColumnIndex(COLUMN_MOVIE_TITLE);
                    int durationIndex = cursor.getColumnIndex(COLUMN_MOVIE_DURATION);
                    int genreIndex = cursor.getColumnIndex(COLUMN_MOVIE_GENRE);
                    int ratingIndex = cursor.getColumnIndex(COLUMN_MOVIE_RATING);

                    if (idIndex >= 0 && titleIndex >= 0 && durationIndex >= 0 && genreIndex >= 0 && ratingIndex >= 0) {
                        int id = cursor.getInt(idIndex);
                        byte[] cover = cursor.getBlob(coverIndex);
                        String title = cursor.getString(titleIndex);
                        int duration = cursor.getInt(durationIndex);
                        String genre = cursor.getString(genreIndex);
                        float rating = cursor.getFloat(ratingIndex);

                        // Crear una instancia de Movie y agregarla a la lista
                        Movie movie = new Movie(id, userId, platformId, cover, title, duration, genre, rating);
                        movieList.add(movie);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return movieList;
    }

    public Movie getMovieById(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIES + " WHERE " + COLUMN_MOVIE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(movieId)});

        Movie movie = null;
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID_MOVIE);
            int platformIdIndex = cursor.getColumnIndex(COLUMN_PLATFORM_ID_MOVIE);
            int coverIndex = cursor.getColumnIndex(COLUMN_MOVIE_COVER);
            int titleIndex = cursor.getColumnIndex(COLUMN_MOVIE_TITLE);
            int durationIndex = cursor.getColumnIndex(COLUMN_MOVIE_DURATION);
            int genreIndex = cursor.getColumnIndex(COLUMN_MOVIE_GENRE);
            int ratingIndex = cursor.getColumnIndex(COLUMN_MOVIE_RATING);

            if (userIdIndex >= 0 && platformIdIndex >= 0 && coverIndex >= 0 && titleIndex >= 0 && durationIndex >= 0 && genreIndex >= 0 && ratingIndex >= 0) {
                int userId = cursor.getInt(userIdIndex);
                int platformId = cursor.getInt(platformIdIndex);
                byte[] coverData = cursor.getBlob(coverIndex);
                String title = cursor.getString(titleIndex);
                int duration = cursor.getInt(durationIndex);
                String genre = cursor.getString(genreIndex);
                float rating = cursor.getFloat(ratingIndex);

                movie = new Movie(movieId, userId, platformId, coverData, title, duration, genre, rating);
            }
            cursor.close();
        }

        return movie;
    }








}