package chadaporn.piaras.kku.ac.th.audemo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager minst;
    private static Context mct;

    private static final String SHARD_PERFNAME="mypref";
    private static final String KEY_ID="id";
    private static final String KEY_TOKEN="token";
    private static final String KEY_FIRSTNAME="fname";
    private static final String KEY_LASTNAME="lname";
    private static final String KEY_CITIZENID="cid";
    private static final String KEY_PHONE="phone";
    private SharedPrefManager(Context context){
        mct=context;
    }
    public static synchronized SharedPrefManager getInstans(Context context){
        if (minst==null){
            minst=new SharedPrefManager(context);
        }
        return minst;
    }

//    public boolean userLogin(String id,String token, String firstname, String Lastname, String citizenid){
//        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putString(KEY_ID, id);
//        editor.putString(KEY_TOKEN, token);
//        editor.putString(KEY_FIRSTNAME, firstname);
//        editor.putString(KEY_LASTNAME, Lastname);
//        editor.putString(KEY_CITIZENID, citizenid);
//        editor.apply();
//        return true;
//    }

    public boolean userLogin(String id,String token){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_ID, id);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
        return true;
    }

    public boolean userData(String first_name,String last_name, String citizen_id, String phone){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_FIRSTNAME, first_name);
        editor.putString(KEY_LASTNAME, last_name);
        editor.putString(KEY_CITIZENID, citizen_id);
        editor.putString(KEY_PHONE, phone);
        editor.apply();
        return true;
    }

    public boolean isLogin(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_TOKEN,null)!=null){
            return true;
        }
        return false;
    }
    public boolean logout(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }
    public String getUserId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID,null);

    }
    public String getToken(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN,null);

    }
    public String getFirstname(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRSTNAME,null);

    }
    public String getLastname(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LASTNAME,null);

    }
    public String getCitizenId(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CITIZENID,null);

    }


    public String getPhone(){
        SharedPreferences sharedPreferences=mct.getSharedPreferences(SHARD_PERFNAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE,null);

    }
}
