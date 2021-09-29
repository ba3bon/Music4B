package com.example.btl_music4b.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.btl_music4b.Adapter.ViewPagerDiaNhac;
import com.example.btl_music4b.Fragment.Fragment_dia_nhac;
import com.example.btl_music4b.Model.BaiHatModel;
import com.example.btl_music4b.Model.BaiHatThuVienPlayListModel;
import com.example.btl_music4b.Model.BaiHatYeuThichModel;
import com.example.btl_music4b.Model.ResponseModel;
import com.example.btl_music4b.R;
import com.example.btl_music4b.Service.APIService;
import com.example.btl_music4b.Service.Dataservice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayNhacActivity extends AppCompatActivity {
    //private CircleLineVisualizer mVisualizer;
    private SQLiteDatabase db;
    private MediaPlayer mediaPlayer;
    private androidx.appcompat.widget.Toolbar toolbarplaynhac;
    private SeekBar seekBarnhac;
    private ImageView imageViewtim;
    private TextView textViewtennhac, textViewcasi, textViewrunrime, textViewtatoltime;
    private ImageButton imageButtontronnhac, imageButtonpreviewnhac, imageButtonplaypausenhac, imageButtonnexnhac,
    imageButtonlapnhac;
    ViewPager viewPagerplaynhac;
    private int dem = 0;
    private int position = 0;
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;
    public static ArrayList<BaiHatModel> mangbaihat = new ArrayList<>();
    public static ArrayList<BaiHatThuVienPlayListModel> mangbaihetthuvienplaylist = new ArrayList<>();
    public static ArrayList<BaiHatYeuThichModel> mangbaihatyeuthich = new ArrayList<>();
    private String taikhoan, matkhau, name, url;

    Fragment_dia_nhac fragment_dia_nhac;
    public static ViewPagerDiaNhac adapternhac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac);
        GetDataFromIntent();
        db = openOrCreateDatabase("NguoiDung.db", MODE_PRIVATE, null);
        getData();
        AnhXa();
        overridePendingTransition(R.anim.anim_intent_in, R.anim.anim_intent_out);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        enventClick();
        imageViewtim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dem == 0){
                    Animation animation = AnimationUtils.loadAnimation(PlayNhacActivity.this, R.anim.anim_timclick);
                    imageViewtim.setImageResource(R.drawable.iconloved);
                    view.startAnimation(animation);
                    if (mangbaihat.size() > 0){
                        insertYeuThich(taikhoan, mangbaihat.get(position).getIdBaiHat(), mangbaihat.get(position).getTenBaiHat(),
                                mangbaihat.get(position).getTenCaSi(), mangbaihat.get(position).getHinhBaiHat(), mangbaihat.get(position).getLinkBaiHat());
                    }else if (mangbaihetthuvienplaylist.size() > 0){
                        insertYeuThich(taikhoan, mangbaihetthuvienplaylist.get(position).getIdBaiHat(), mangbaihetthuvienplaylist.get(position).getTenBaiHat(),
                                mangbaihetthuvienplaylist.get(position).getTenCaSi(), mangbaihetthuvienplaylist.get(position).getHinhBaiHat(), mangbaihetthuvienplaylist.get(position).getLinkBaiHat());
                    }else if (mangbaihatyeuthich.size() > 0){
                        insertYeuThich(taikhoan, mangbaihatyeuthich.get(position).getIdBaiHat(), mangbaihatyeuthich.get(position).getTenBaiHat(),
                                mangbaihatyeuthich.get(position).getTenCaSi(), mangbaihatyeuthich.get(position).getHinhBaiHat(), mangbaihatyeuthich.get(position).getLinkBaiHat());
                    }
                    dem++;

                }else {
                    imageViewtim.setImageResource(R.drawable.iconlove);
                    if (mangbaihat.size() > 0){
                        deleteYeuThich(taikhoan, mangbaihat.get(position).getIdBaiHat());
                    }else if (mangbaihetthuvienplaylist.size() > 0){
                        deleteYeuThich(taikhoan, mangbaihetthuvienplaylist.get(position).getIdBaiHat());
                    }else if (mangbaihatyeuthich.size() > 0){
                        deleteYeuThich(taikhoan, mangbaihatyeuthich.get(position).getIdBaiHat());
                    }
                    dem--;
                }
            }
        });
    }

    public void insertYeuThich(String un, int idbh, String tbh, String tcs, String hbh, String lbh) {
        Dataservice dataservice = APIService.getService();
        Call<ResponseModel> callback = dataservice.insertyeuthich(un, idbh, tbh, tcs, hbh, lbh);
        callback.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        //Toast.makeText(PlayNhacActivity.this, "Đã thích", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }

        });
    }
    public void deleteYeuThich(String un, int idbh) {
        Dataservice dataservice = APIService.getService();
        Call<ResponseModel> callback = dataservice.deleteyeuthich(un, idbh);
        callback.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        //Toast.makeText(PlayNhacActivity.this, "Đã bỏ thích", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }

        });
    }
    public void checkYeuThich(String un, int idbh) {
        Dataservice dataservice = APIService.getService();
        Call<ResponseModel> callback = dataservice.checkyeuthich(un, idbh);
        callback.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        dem = 1;
                        imageViewtim.setImageResource(R.drawable.iconloved);
                    } else {
                        dem = 0;
                        imageViewtim.setImageResource(R.drawable.iconlove);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }

        });
    }

    private void enventClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mangbaihat.size() > 0){
                    fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getHinhBaiHat());
                    handler.removeCallbacks(this);
                }else if (mangbaihetthuvienplaylist.size() > 0){
                    fragment_dia_nhac.PlayNhac(mangbaihetthuvienplaylist.get(position).getHinhBaiHat());
                    handler.removeCallbacks(this);
                }else if (mangbaihatyeuthich.size() > 0){
                    fragment_dia_nhac.PlayNhac(mangbaihatyeuthich.get(position).getHinhBaiHat());
                    handler.removeCallbacks(this);
                }else {
                        handler.postDelayed(this, 300);
                }
            }
        }, 500);
        imageButtonplaypausenhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imageButtonplaypausenhac.setImageResource(R.drawable.nutpause);
                }else {
                    mediaPlayer.start();
                    imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
                }
            }
        });
        imageButtonlapnhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeat == false){
                    if (checkrandom == true){
                        checkrandom = false;
                        imageButtonlapnhac.setImageResource(R.drawable.iconsyned);
                        imageButtontronnhac.setImageResource(R.drawable.iconsuffle);
                        repeat = true;
                    }else {
                        imageButtonlapnhac.setImageResource(R.drawable.iconsyned);
                        repeat = true;
                    }
                }else {
                    imageButtonlapnhac.setImageResource(R.drawable.iconrepeat);
                    repeat = false;
                }
            }
        });
        imageButtontronnhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkrandom == false){
                    if (repeat == true){
                        repeat = false;
                        imageButtontronnhac.setImageResource(R.drawable.iconshuffled);
                        imageButtonlapnhac.setImageResource(R.drawable.iconrepeat);
                        checkrandom = true;
                    }else {
                        imageButtontronnhac.setImageResource(R.drawable.iconshuffled);
                        checkrandom = true;
                    }
                }else {
                    imageButtontronnhac.setImageResource(R.drawable.iconsuffle);
                    checkrandom = false;
                }
            }
        });
        seekBarnhac.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        imageButtonnexnhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mangbaihat.size() > 0){
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (mangbaihat.size())){
                        imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
                        position++;
                        if (repeat == true){
                            if (position == 0){
                                position = mangbaihat.size();
                            }
                            position -= 1;
                        }
                        if (checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > mangbaihat.size() - 1){
                            position = 0;
                        }
                        checkYeuThich(taikhoan, mangbaihat.get(position).getIdBaiHat());
                        fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getHinhBaiHat());
                        new playMP3().execute(mangbaihat.get(position).getLinkBaiHat());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }else if (mangbaihetthuvienplaylist.size() > 0){
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (mangbaihetthuvienplaylist.size())){
                        imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
                        position++;
                        if (repeat == true){
                            if (position == 0){
                                position = mangbaihetthuvienplaylist.size();
                            }
                            position -= 1;
                        }
                        if (checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(mangbaihetthuvienplaylist.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > mangbaihetthuvienplaylist.size() - 1){
                            position = 0;
                        }
                        checkYeuThich(taikhoan, mangbaihetthuvienplaylist.get(position).getIdBaiHat());
                        fragment_dia_nhac.PlayNhac(mangbaihetthuvienplaylist.get(position).getHinhBaiHat());
                        new playMP3().execute(mangbaihetthuvienplaylist.get(position).getLinkBaiHat());
                        getSupportActionBar().setTitle(mangbaihetthuvienplaylist.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }else if (mangbaihatyeuthich.size() > 0){
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (mangbaihatyeuthich.size())){
                        imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
                        position++;
                        if (repeat == true){
                            if (position == 0){
                                position = mangbaihatyeuthich.size();
                            }
                            position -= 1;
                        }
                        if (checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(mangbaihatyeuthich.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > mangbaihatyeuthich.size() - 1){
                            position = 0;
                        }
                        checkYeuThich(taikhoan, mangbaihatyeuthich.get(position).getIdBaiHat());
                        fragment_dia_nhac.PlayNhac(mangbaihatyeuthich.get(position).getHinhBaiHat());
                        new playMP3().execute(mangbaihatyeuthich.get(position).getLinkBaiHat());
                        getSupportActionBar().setTitle(mangbaihatyeuthich.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }
                imageButtonpreviewnhac.setClickable(false);
                imageButtonnexnhac.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageButtonpreviewnhac.setClickable(true);
                        imageButtonnexnhac.setClickable(true);
                    }
                }, 3000);
            }
        });
        imageButtonpreviewnhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mangbaihat.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (mangbaihat.size())) {
                        imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
                        position--;
                        if (position < 0) {
                            position = mangbaihat.size() - 1;
                        }
                        if (repeat == true) {
                            position += 1;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        checkYeuThich(taikhoan, mangbaihat.get(position).getIdBaiHat());
                        fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getHinhBaiHat());
                        new playMP3().execute(mangbaihat.get(position).getLinkBaiHat());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }else if (mangbaihetthuvienplaylist.size() > 0){
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (mangbaihetthuvienplaylist.size())) {
                        imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
                        position--;
                        if (position < 0) {
                            position = mangbaihetthuvienplaylist.size() - 1;
                        }
                        if (repeat == true) {
                            position += 1;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihetthuvienplaylist.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        checkYeuThich(taikhoan, mangbaihetthuvienplaylist.get(position).getIdBaiHat());
                        fragment_dia_nhac.PlayNhac(mangbaihetthuvienplaylist.get(position).getHinhBaiHat());
                        new playMP3().execute(mangbaihetthuvienplaylist.get(position).getLinkBaiHat());
                        getSupportActionBar().setTitle(mangbaihetthuvienplaylist.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }else if (mangbaihatyeuthich.size() > 0){
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (mangbaihatyeuthich.size())) {
                        imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
                        position--;
                        if (position < 0) {
                            position = mangbaihatyeuthich.size() - 1;
                        }
                        if (repeat == true) {
                            position += 1;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihatyeuthich.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        checkYeuThich(taikhoan, mangbaihatyeuthich.get(position).getIdBaiHat());
                        fragment_dia_nhac.PlayNhac(mangbaihatyeuthich.get(position).getHinhBaiHat());
                        new playMP3().execute(mangbaihatyeuthich.get(position).getLinkBaiHat());
                        getSupportActionBar().setTitle(mangbaihatyeuthich.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }
                imageButtonpreviewnhac.setClickable(false);
                imageButtonnexnhac.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageButtonpreviewnhac.setClickable(true);
                        imageButtonnexnhac.setClickable(true);
                    }
                }, 3000);
            }
        });
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        mangbaihetthuvienplaylist.clear();
        mangbaihatyeuthich.clear();
        if (intent != null){
            if (intent.hasExtra("cakhuc")){
                BaiHatModel baiHat = intent.getParcelableExtra("cakhuc");
                mangbaihat.add(baiHat);
            }else if (intent.hasExtra("cacbaihat")){
                ArrayList<BaiHatModel> baiHatArrayList = intent.getParcelableArrayListExtra("cacbaihat");
                mangbaihat = baiHatArrayList;
            }else if (intent.hasExtra("cakhucthuvien")){
                BaiHatThuVienPlayListModel baiHatThuVienPlayList = intent.getParcelableExtra("cakhucthuvien");
                mangbaihetthuvienplaylist.add(baiHatThuVienPlayList);
            }else if (intent.hasExtra("cacbaihatthuvien")){
                ArrayList<BaiHatThuVienPlayListModel> baiHatThuVienArrayList = intent.getParcelableArrayListExtra("cacbaihatthuvien");
                mangbaihetthuvienplaylist = baiHatThuVienArrayList;
            }else if (intent.hasExtra(("cakhucyeuthich"))){
                BaiHatYeuThichModel baiHatYeuThichModel = intent.getParcelableExtra("cakhucyeuthich");
                mangbaihatyeuthich.add(baiHatYeuThichModel);
            }
        }
    }
    public void getData() {
        String sql = "SELECT * FROM tbNguoiDung";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToLast();
        taikhoan = cursor.getString(1);
        matkhau = cursor.getString(2);
        name = cursor.getString(3);
        url = cursor.getString(4);
    }

    private void AnhXa() {
        //mVisualizer = findViewById(R.id.blob);
        toolbarplaynhac = findViewById(R.id.toolbarplaynhac);
        seekBarnhac = findViewById(R.id.seekBartime);
        viewPagerplaynhac = findViewById(R.id.viewPagerdianhac);
        imageViewtim = findViewById(R.id.imageViewtimplaynhac);
        imageButtontronnhac = findViewById(R.id.imageButtontron);
        imageButtonpreviewnhac = findViewById(R.id.imageButtonpreview);
        imageButtonplaypausenhac = findViewById(R.id.imageButtonplaypause);
        imageButtonnexnhac = findViewById(R.id.imageButtonnext);
        imageButtonlapnhac = findViewById(R.id.imageButtonlap);
        textViewtatoltime = findViewById(R.id.textViewtimetotal);
        textViewcasi = findViewById(R.id.textViewtencasiplaynhac);
        textViewtennhac = findViewById(R.id.textViewtenbaihatplaynhac);
        textViewrunrime = findViewById(R.id.textViewruntime);

        fragment_dia_nhac = new Fragment_dia_nhac();
        adapternhac = new ViewPagerDiaNhac(getSupportFragmentManager());
        adapternhac.AddFragment(fragment_dia_nhac);
        viewPagerplaynhac.setAdapter(adapternhac);
        setSupportActionBar(toolbarplaynhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarplaynhac.setTitleTextColor(Color.BLACK);

        fragment_dia_nhac = (Fragment_dia_nhac) adapternhac.getItem(position);
        if (mangbaihat.size() > 0){
            checkYeuThich(taikhoan, mangbaihat.get(position).getIdBaiHat());
            getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
            new playMP3().onPostExecute(mangbaihat.get(position).getLinkBaiHat());
            imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
        }else if (mangbaihetthuvienplaylist.size() > 0){
            checkYeuThich(taikhoan, mangbaihetthuvienplaylist.get(position).getIdBaiHat());
            getSupportActionBar().setTitle(mangbaihetthuvienplaylist.get(position).getTenBaiHat());
            new playMP3().onPostExecute(mangbaihetthuvienplaylist.get(position).getLinkBaiHat());
            imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
        }else if (mangbaihatyeuthich.size() > 0){
            checkYeuThich(taikhoan, mangbaihatyeuthich.get(position).getIdBaiHat());
            getSupportActionBar().setTitle(mangbaihatyeuthich.get(position).getTenBaiHat());
            new playMP3().onPostExecute(mangbaihatyeuthich.get(position).getLinkBaiHat());
            imageButtonplaypausenhac.setImageResource(R.drawable.nutplay);
        }
        toolbarplaynhac.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mangbaihat.clear();
                mangbaihetthuvienplaylist.clear();
                mangbaihatyeuthich.clear();
                finish();
            }
        });

    }
    class playMP3 extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(baihat);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            TimeSong();
            UpdateTime();
        }
    }
    private void TimeSong(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        textViewtatoltime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBarnhac.setMax(mediaPlayer.getDuration());
        if (mangbaihat.size() > 0){
            textViewtennhac.setText(mangbaihat.get(position).getTenBaiHat());
            textViewcasi.setText(mangbaihat.get(position).getTenCaSi());
        }else if (mangbaihetthuvienplaylist.size() > 0){
            textViewtennhac.setText(mangbaihetthuvienplaylist.get(position).getTenBaiHat());
            textViewcasi.setText(mangbaihetthuvienplaylist.get(position).getTenCaSi());
        }else if (mangbaihatyeuthich.size() > 0){
            textViewtennhac.setText(mangbaihatyeuthich.get(position).getTenBaiHat());
            textViewcasi.setText(mangbaihatyeuthich.get(position).getTenCaSi());
        }
    }
    private void UpdateTime(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    seekBarnhac.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    textViewrunrime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 300);
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true){
                    if (position < (mangbaihat.size())) {
                        //imageButtonplaypausenhac.setImageResource(R.drawable.nutpause);
                        position++;
                        if (repeat == true) {
                            position --;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > mangbaihat.size() - 1) {
                            position = 0;
                        }
                        try {
                            checkYeuThich(taikhoan, mangbaihat.get(position).getIdBaiHat());
                            fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getHinhBaiHat());
                            new playMP3().execute(mangbaihat.get(position).getLinkBaiHat());
                            getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if (position < (mangbaihetthuvienplaylist.size())) {
                        //imageButtonplaypausenhac.setImageResource(R.drawable.nutpause);
                        position++;
                        if (repeat == true) {
                            position --;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihetthuvienplaylist.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > mangbaihetthuvienplaylist.size() - 1) {
                            position = 0;
                        }
                        try {
                            checkYeuThich(taikhoan, mangbaihetthuvienplaylist.get(position).getIdBaiHat());
                            fragment_dia_nhac.PlayNhac(mangbaihetthuvienplaylist.get(position).getHinhBaiHat());
                            new playMP3().execute(mangbaihetthuvienplaylist.get(position).getLinkBaiHat());
                            getSupportActionBar().setTitle(mangbaihetthuvienplaylist.get(position).getTenBaiHat());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if (position < (mangbaihatyeuthich.size())) {
                        //imageButtonplaypausenhac.setImageResource(R.drawable.nutpause);
                        position++;
                        if (repeat == true) {
                            position --;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihatyeuthich.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > mangbaihatyeuthich.size() - 1) {
                            position = 0;
                        }
                        try {
                            checkYeuThich(taikhoan, mangbaihatyeuthich.get(position).getIdBaiHat());
                            fragment_dia_nhac.PlayNhac(mangbaihatyeuthich.get(position).getHinhBaiHat());
                            new playMP3().execute(mangbaihatyeuthich.get(position).getLinkBaiHat());
                            getSupportActionBar().setTitle(mangbaihatyeuthich.get(position).getTenBaiHat());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    imageButtonpreviewnhac.setClickable(false);
                    imageButtonnexnhac.setClickable(false);
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageButtonpreviewnhac.setClickable(true);
                            imageButtonnexnhac.setClickable(true);
                        }
                    }, 3000);
                    next = false;
                    handler1.removeCallbacks(this);
                }else {
                    handler1.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}
