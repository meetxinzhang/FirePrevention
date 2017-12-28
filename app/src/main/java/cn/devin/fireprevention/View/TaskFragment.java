package cn.devin.fireprevention.View;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cn.devin.fireprevention.Model.MyTask;
import cn.devin.fireprevention.MyApplication;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2017/12/2.
 * “任务” 界面
 */

public class TaskFragment extends Fragment implements TaskRvAdapter.OnRecyclerViewItemClickListener{
    private static final String TAG = "TaskFragment";
    private static final String ARG_POSITION = "position";
    private List<MyTask> list = new ArrayList<>();

    //视图
    private RecyclerView recyclerView;
    private TaskRvAdapter adapter;

    public static TaskFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task,container,false);

        //开始--任务列表设置--开始！
        recyclerView = rootView.findViewById(R.id.task_rv);
        //创建一个线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //垂直方向滚动
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(manager);
        //设置适配器
        adapter = new TaskRvAdapter(list);
        adapter.setOnRecyclerViewItemClickListener(this);
        recyclerView.setAdapter(adapter);
        //结束--任务列表设置--结束！

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //刷新数据
        list.clear();
        //测试
        list.add(new MyTask(new LatLng(30.134509, 112.99911),
                new int[]{20,30},
                "haha",
                "hehe"));
        adapter.updateList(list);
        //通知适配器
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRvItemClick(View view, MyTask myTask) {
        Log.d(TAG, "onRvItemClick: " + myTask.getSubject());
        //获取 MapFragment 对象
        FragmentManager manager = getActivity().getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) manager.getFragments().get(0);
        //响应点击事件
        if(mapFragment != null){
            mapFragment.tencentMap.moveCamera(AnimationOfMap.reFocus(myTask.getDestination()));
            Toast.makeText(MyApplication.getContext(),"开始追踪任务",Toast.LENGTH_SHORT).show();
        }
    }
}
