package com.example.task0723;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {
    //어댑터를 만들어서 연결하려고 할 때 context가 팔요함
    private Context context;
    //연결해서 보여주려고 하는 화면에 넣어줄 데이터를 arraylist로 만듦
    private ArrayList<MusicData> arrayList;

    //생성자에는 context가 들어가야됨
    public MusicAdapter(Context context) {
        this.context = context;
    }

    //전체 갯수
    @Override
    public int getCount() {
        return arrayList.size();
    }
    //뿌려져야할 MusicData의 데이터 정보 리스트에서  position을 통해 위치를 알 수 있다
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }
    //뿌려저야할 MusicData의 위치 정보 (arraylist에 있는 정보) ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //xml 객체를 만들기 위한 작업을 한다
        //리스트 뷰를 보기 위해 setContentView와 같은 기능을 하는 layoutInflater를 사용한다
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if(view == null ){
            view = layoutInflater.inflate(R.layout.music_layout,null);
        }

        //music_layout.xml 안에 있는 위젯을 찾아서 등록한다
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView tvMusicTitle = view.findViewById(R.id.tvMusicTitle);
        TextView tvSinger = view.findViewById(R.id.tvSinger);

        //노래의 정보를 arraylist에 순서대로 넣는다
        MusicData musicData = arrayList.get(position);

       imageView.setImageBitmap(musicData.getMp3Picture());
        //imageView.setImageResource(musicData.getMp3Picture());
        tvMusicTitle.setText(musicData.getMp3FileName());
         tvSinger.setText(musicData.getSinger());

        return view;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<MusicData> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<MusicData> arrayList) {
        this.arrayList = arrayList;
    }
}
