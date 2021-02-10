package pt.ipbeja.estig.reallysimpleandroid.db.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

interface BaseDao<ENTITY> {

    @Insert
    long insert(ENTITY t);

    @Update
    int update(ENTITY t);

    @Delete
    int delete(ENTITY t);

}
