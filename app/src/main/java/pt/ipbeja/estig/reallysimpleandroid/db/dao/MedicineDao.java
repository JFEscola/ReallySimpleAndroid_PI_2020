package pt.ipbeja.estig.reallysimpleandroid.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

@Dao
public abstract class MedicineDao implements BaseDao<Medicine>{

    @Query("select * from medicine")
    public abstract List<Medicine> getAll();

    @Query("select * from medicine where id = :medicineId")
    public abstract Medicine getId(long medicineId);
}
