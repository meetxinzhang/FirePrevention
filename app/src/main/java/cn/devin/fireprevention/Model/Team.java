package cn.devin.fireprevention.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devin on 2018/3/30.
 */

public class Team {

    private List<Person> list=new ArrayList<Person>();//这个列表存放客户端信息，存放的是一个个的对象\

    //构造函数
    public Team(Person person)
    {
        list.add(person);
    }

    public List<Person> getPersons() {
        return list;
    }

    //添加
    public void addPerson(Person person) {
        list.add(person);
    }

    //删除
    public void subPerson(int id)
    {
        for(int i=0;i<list.size();i++)
        {
            if(id==list.get(i).getId())
            {
                //去掉这一项
            }

        }
    }

}
