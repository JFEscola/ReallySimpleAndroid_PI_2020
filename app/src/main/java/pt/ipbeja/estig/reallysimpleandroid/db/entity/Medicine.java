package pt.ipbeja.estig.reallysimpleandroid.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "medicine")
public class Medicine implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private String time;

    public Medicine(long id, String name, String time){
        this.id = id;
        this.name = name;
        this.time = time;

    }

    public Medicine(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
