package cn.devin.fireprevention.View;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import cn.devin.fireprevention.Model.MyTask;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2017/12/27.
 * 任务列表适配器
 */

public class TaskRvAdapter extends RecyclerView.Adapter<TaskRvAdapter.ViewHolder>{
    List<MyTask> list;

    /**
     * 构造方法
     * @param list 任务列表
     */
    public TaskRvAdapter(List<MyTask> list){
        this.list = list;
    }

    public void updateList(List<MyTask> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_task,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //获取当前实体
        final MyTask myTask = list.get(position);
        Log.i("adapter","onBindVH0: "+ myTask.getSubject());
        //设置属性
        holder.subject_tv.setText(myTask.getSubject());
        //设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onRecyclerViewItemClickListener != null){
                    onRecyclerViewItemClickListener.onRvItemClick(view,myTask);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * 自定义的 ViewHoider
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subject_tv;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            subject_tv = itemView.findViewById(R.id.subject_tv_task);
            cardView = itemView.findViewById(R.id.cardView_task);
        }

    }


    //定义监听器外部实现接口
    //传入的点击监听对象
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public interface OnRecyclerViewItemClickListener {
        void onRvItemClick(View view,MyTask myTask);
    }
    //模仿ListView的设置监听对象方法
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
