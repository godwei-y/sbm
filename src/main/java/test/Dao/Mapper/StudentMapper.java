package test.Dao.Mapper;

import org.apache.ibatis.annotations.Param;
import test.Dao.Po.Student;

import java.util.List;

public interface StudentMapper {

    void addStudent(Student student);
    Student selectStudentById(String id);
    Student selectStudentByName(String name);
    List<Student> selectAllStudent();
    void updateStudent(Student student);
    void deleteStudentById(String id);
    //List<Student> query(@Param("name")String name);
}
