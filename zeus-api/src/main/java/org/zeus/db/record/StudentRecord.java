package org.zeus.db.record;

import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.zeus.db.DbException;
import org.zeus.db.JdbcConnectionManager;
import org.zeus.db.entity.Student;

import java.util.List;
import java.util.Map;

@Slf4j
@Table("student")
@IdName("id")
@Cached
public class StudentRecord extends Model {

    public Student toEntity() throws DbException {
        try {
            return JdbcConnectionManager.GSON.fromJson(this.getString("json_entity"), Student.class);
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static StudentRecord toModel(Student a) throws DbException {
        try {
            StudentRecord ar = new StudentRecord();
            ar.set("id", a.getId()).set("name", a.getName()).set("rollno", a.getRollNo())
                    .set("email", a.getEmail()).set("dob", a.getDob())
                    .set("gender", a.getGender()).set("orgid", a.getOrgId())
                    .set("json_entity", JdbcConnectionManager.GSON.toJson(a));
            return ar;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static List<StudentRecord> findByFilter(Map<String, Object> filterMap) {
        if (filterMap == null || filterMap.isEmpty()) {
            return findAll();
        }
        JdbcConnectionManager.FilterConditionArgs filterConditionArgs = JdbcConnectionManager.buildAndFilterFromMap(filterMap);
        return StudentRecord.where(filterConditionArgs.getFilter(), filterConditionArgs.getArgs());
    }
}
