package com.example.task0723;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //메인 화면 위젯
    private ListView listView;
    private ImageButton ibPlay;
    private ImageButton ibPause;
    private ImageButton ibStop;
    private ImageButton ibPrevious;
    private ImageButton ibNext;

    private TextView tvMusicName;

    private ProgressBar progressBarMp3;

    private SeekBar seekBar;

    //sdCard의 절대 경로를 기억하기 위해 변수 선언
    private String sdCardPath;
    //일시 정지 이벤트 구분을 위해 flag 사용
    private Boolean flag=false;
    //노래를 재생하기 위해서 미디어 플레이어를 가져온다
    private MediaPlayer mediaPlayer = new MediaPlayer();

    //내가 만든 화면에 가져올 데이터를 가져와서 저장할 변수를 선언
    private ArrayList<MusicData> mp3MusicDataArrayList = new ArrayList<MusicData>();

    //선택된 음악 파일의 이름 정보를 가져온다
    private MusicData selectedMp3;

    private int index;
    private int flagChangeButton = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //화면 객체 찾는 함수
        findViewByIdFuc();

        //외부 저장장치에 권한을 요청함, 개인이 그 장치에 접근할 수 있도록 허용
        ActivityCompat.requestPermissions(this,new String []{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE );

        //sdCard의 절대 경로를 지정한다, 음악 파일이 있는 폴더에 접근한다
        sdCardPath= Environment.getExternalStorageDirectory().getPath()+"/music/";

        //sd카드 안의 모든 파일을 가져와 mp3 파일만 골라내서 리스트 뷰에 보여준다
        loadMP3FileFromCard();

        //어댑터를 만들 음악 정보를 리스트에 올림
        MusicAdapter adapter = new MusicAdapter(getApplicationContext());
        adapter.setArrayList(mp3MusicDataArrayList);


        //버튼과 프로그래스 바를 초기화, 처음 화면에서 중지 버튼과 프로그래스 바를 숨긴다
        btnStateChange(true,false,View.INVISIBLE);

        //리스트 뷰에서 보여줄 라디오 버튼 그룹을 설정, 어댑터를 설정 고
        listViewMp3SettingFunc(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //선택된 음악 파일의 이름 정보를 가져온다
                selectedMp3=mp3MusicDataArrayList.get(position);
                index=position;
                try{
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(sdCardPath+selectedMp3.getFullName());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    tvMusicName.setText("실행음악: "+selectedMp3.getMp3FileName());


                    Thread thread = new Thread(new Runnable() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

                            while(mediaPlayer.isPlaying() || flag ==true) {
                                if(false!=true){
                                    seekBar.setMax(mediaPlayer.getDuration());
                                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                }
                                SystemClock.sleep(100);
                            }//end of while

                            //재생이 완료된 뒤에 버튼과 seekbar와 progressbar가 초기화 돼야한다
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer.reset();
                                    seekBar.setProgress(0);
                                    progressBarMp3.setVisibility(View.INVISIBLE);
                                    //ibPlay.setEnabled(true);
                                    ibPause.setEnabled(false);
                                    ibStop.setEnabled(false);
                                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                    int currentPosition = mediaPlayer.getCurrentPosition();
                                }
                            });

                        }
                    });
                    //스레드에서 정의한 내용을 실행시킴
                    thread.start();

                    //ibPlay.setEnabled(false);
                    ibPause.setImageResource(R.drawable.pause);
                    ibPause.setEnabled(true);
                    ibStop.setEnabled(true);
                    //flag=false;
                    tvMusicName.setText("실행음악: "+selectedMp3.getMp3FileName());
                    progressBarMp3.setVisibility(View.VISIBLE);
                }catch (IOException e){
                    e.printStackTrace();
                }


            }
        });

        //로그인 화면에서 넘어옴
        Intent intent =getIntent();


    }//end of onCreate

    private void listViewMp3SettingFunc(MusicAdapter adapter) {
        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        listView.setItemChecked(0,true);
    }

    //버튼과 프로그래스 바를 초기화, 처음 화면에서 중지 버튼과 프로그래스 바를 숨긴다
    private void btnStateChange(boolean b, boolean b1, int invisible) {
        //ibPlay.setEnabled(true);
        ibPause.setEnabled(false);
        ibStop.setEnabled(false);
//        ibNext.setEnabled(false);
//        ibPrevious.setEnabled(false);
        progressBarMp3.setVisibility(View.INVISIBLE);
    }

    //위젯 객체 변수 설정 및 이벤트 등록
    private void findViewByIdFuc() {
        listView=findViewById(R.id.listView);
        //ibPlay=findViewById(R.id.ibPlay);
        ibPause=findViewById(R.id.ibPause);
        ibStop=findViewById(R.id.ibStop);
        ibPrevious=findViewById(R.id.ibPrevious);
        ibNext=findViewById(R.id.ibNext);

        tvMusicName=findViewById(R.id.tvMusicName);

        progressBarMp3=findViewById(R.id.progressBarMp3);
        seekBar=findViewById(R.id.seekBar);

        //버튼의 이벤트 요청 함수
       // ibPlay.setOnClickListener(this);
        ibPause.setOnClickListener(this);
        ibStop.setOnClickListener(this);
        ibPrevious.setOnClickListener(this);
        ibNext.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //노래가 진행되면서 진행 위치가 어딨는지를 보여준다
                if(b){
                    mediaPlayer.seekTo(i);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //sd카드 안의 모든 파일을 가져와 mp3 파일만 골라내서 리스트 뷰에 보여준다
    private void loadMP3FileFromCard() {
        //sd카드 안의 모든 내용을 배열로 만든다.
        File [] filelists = new File(sdCardPath).listFiles();

        for(File file : filelists ){
            String fileName = file.getName();

            if(fileName.length()>=4){
                //확장자를 이용해서 원하는 것을 빼온다. 전체 파일 이름에서 뒤의 3자리를 통해서 파일을 가져온다
                String extendName = fileName.substring(fileName.length()-3);

                if(extendName.equals("mp3")){
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(sdCardPath+fileName);
                    byte [] data = mmr.getEmbeddedPicture();
                    Bitmap bitmap = null;
                    if(data!=null) {
                        bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                    }

                    String singer = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                    MusicData musicData = new MusicData(bitmap,title,singer,fileName);
                    mp3MusicDataArrayList.add(musicData);
                }

            }

        }
    }//end of loadMP3FileFromCard

    private void previousSong() {
        index--;

        try {
            if(index<0){
                //배열은  사이즈 -1
                index=mp3MusicDataArrayList.size()-1;
            }
            selectedMp3 = mp3MusicDataArrayList.get(index);
            mediaPlayer.stop();
            mediaPlayer.reset();

            mediaPlayer.setDataSource(sdCardPath + selectedMp3.getFullName());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (mediaPlayer.isPlaying()) {

                        seekBar.setMax(mediaPlayer.getDuration());
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        SystemClock.sleep(100);
                    }
                }
            };
            thread.start();
            //tvMusicName.setText(selectedMp3.getFullName());
            tvMusicName.setText("실행음악: "+selectedMp3.getMp3FileName());
            progressBarMp3.setVisibility(View.VISIBLE);

            //t.setText(selectMP3.getMttitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void nextSong() {
        index++;

        try {
            if(index==mp3MusicDataArrayList.size()){
                index=0;
            }
            selectedMp3 = mp3MusicDataArrayList.get(index);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(sdCardPath + selectedMp3.getFullName());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (mediaPlayer.isPlaying()) {

                        seekBar.setMax(mediaPlayer.getDuration());
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        SystemClock.sleep(100);
                    }
                }
            };
            thread.start();
            //tvMusicName.setText(selectedMp3.getFullName());
            tvMusicName.setText("실행음악: "+selectedMp3.getMp3FileName());
            progressBarMp3.setVisibility(View.VISIBLE);

            //t.setText(selectMP3.getMttitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibPause:
                if(flag==false){
                    mediaPlayer.pause();
                  //  btnPause.setText("이어듣기");
                    flag=true;
                    ibPause.setImageResource(R.drawable.play);
                    //ibPlay.setEnabled(false);
                    ibStop.setEnabled(true);
                    progressBarMp3.setVisibility(View.INVISIBLE);
                }else {
                    mediaPlayer.start();
                 //   btnPause.setText(" 일시정지");
                    flag=false;
                    ibPause.setImageResource(R.drawable.pause);
                    //ibPlay.setEnabled(false);
                    ibStop.setEnabled(true);
                    progressBarMp3.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.ibStop:
                mediaPlayer.stop();
                mediaPlayer.reset();
                //ibPlay.setEnabled(true);
                ibPause.setEnabled(false);
                ibStop.setEnabled(false);
                // btnPause.setText("일시정지");
                tvMusicName.setText("");
                progressBarMp3.setVisibility(View.INVISIBLE);
                break;

            case R.id.ibNext:
                mediaPlayer.reset();
                nextSong();
               // ibPlay.setEnabled(false);
                ibPause.setImageResource(R.drawable.pause);
                ibPause.setEnabled(true);
                ibStop.setEnabled(true);
                ibNext.setEnabled(true);
                break;
            case R.id.ibPrevious:
                mediaPlayer.reset();
                previousSong();
                //ibPlay.setEnabled(false);
                ibPause.setImageResource(R.drawable.pause);
                ibPause.setEnabled(true);
                ibStop.setEnabled(true);
                ibNext.setEnabled(true);
                break;




            default: break;

        }
    }

}