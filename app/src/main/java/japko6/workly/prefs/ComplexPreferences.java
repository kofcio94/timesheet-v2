package japko6.workly.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ComplexPreferences {
    private static ComplexPreferences complexPreferences;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static Gson GSON = new Gson();
    Type typeOfObject = new TypeToken<Object>() {
    }.getType();

    private ComplexPreferences(Context context, String namePreferences, int mode) {
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "complex_preferences";
        }
        preferences = context.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static ComplexPreferences getComplexPreferences(Context context, String namePreferences) {

        if (complexPreferences == null) {
            complexPreferences = new ComplexPreferences(context, namePreferences,
                    Context.MODE_PRIVATE);
        }

        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }

        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("key is empty or null");
        }
        editor.putString(key, GSON.toJson(object));
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {

        String gson = preferences.getString(key, null);

        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                Log.e("ComplexPreferences", "Object storaged with key " + key + " is instanceof other class" +
                        e.toString());
                return null;
                //                                throw detailsNew IllegalArgumentException("Object storaged with key " + key
                //                                        + " is instanceof other class");
            }
        }
    }

}
