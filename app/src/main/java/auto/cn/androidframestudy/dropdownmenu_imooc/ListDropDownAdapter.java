package auto.cn.androidframestudy.dropdownmenu_imooc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import auto.cn.androidframestudy.R;

public class ListDropDownAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private int checkItemPosition=-1;
    public void setCheckItem(int position){
        checkItemPosition=position;
        notifyDataSetChanged();
    }
    public ListDropDownAdapter(Context context, List<String> datas) {
        this.context=context;
        this.list=datas;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.item_drop_down_list,null,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        fillValue(position,holder);

        return convertView;
    }
    private void fillValue(int position, ViewHolder holder) {
        holder.tv.setText(list.get(position));
        if(checkItemPosition!=-1){
            if(checkItemPosition==position){
                holder.tv .setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                holder.tv.setBackgroundResource(R.color.check_bg);
            }else{
                holder.tv .setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                holder.tv.setBackgroundResource(R.color.white);
            }
        }
    }

    class ViewHolder{
        TextView tv;
        ViewHolder(View view){
            tv=view.findViewById(R.id.tv);

        }
    }
}
