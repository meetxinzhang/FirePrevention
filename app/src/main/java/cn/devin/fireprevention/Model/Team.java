package cn.devin.fireprevention.Model;

import java.util.List;

/**
 * Created by Devin on 2018/3/30.
 */

public class Team {
    private List<Person> persons;

    public Team(Person person){
        persons.add(person);
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person){
        persons.add(person);
    }

    public void subPerson(int id){
        for (int i=0;i<persons.size();i++){
            if (id == persons.get(i).getId()){
                // 去掉这一项

            }
        }
    }
}
