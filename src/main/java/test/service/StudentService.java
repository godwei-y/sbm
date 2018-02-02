package test.service;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import test.Dao.Mapper.StudentMapper;
import test.Dao.Po.Student;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@CacheConfig(cacheNames = "studentCache")
public class StudentService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StudentMapper studentMapper;

    public Student selectStudentById(String id) {
        String key = "student_";
        ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            System.out.println("根据Id从缓存查询student信息");
            Student student = operations.get(key);
            return student;
        } else {
            System.out.println("根据Id从缓存查询student信息");
            Student student = this.studentMapper.selectStudentById(id);
            operations.set(key, student, 50, TimeUnit.SECONDS);//50过期
            return student;
        }
    }

    public Student selectStudentByName(String name) {
        String key = "student_";
        ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            System.out.println("根据name从缓存查询student信息");
            Student student = operations.get(key);
            return student;
        } else {
            System.out.println("根据name从缓存查询student信息");
            Student student = this.studentMapper.selectStudentByName(name);
            operations.set(key, student, 50, TimeUnit.SECONDS);//50过期
            return student;
        }
    }

    public List<Student> selectAllStudent() {
        String key = "student";
        ValueOperations<String, List<Student>> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            System.out.println("从缓存查询所有student信息");
            List<Student> students = operations.get(key);
            return students;
        } else {
            System.out.println("从数据库查询所有student信息");
            List<Student> students = this.studentMapper.selectAllStudent();
            operations.set(key, students, 50, TimeUnit.SECONDS);//50过期
            return students;
        }
    }

    public Student addStudent(Student student) {
        String key = "student_";
        String keyAll = "student";
        ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        System.out.println("新增student信息到数据库，再缓存到redis中！");
        this.studentMapper.addStudent(student);
        operations.set(key, student, 50, TimeUnit.SECONDS);
        //删除selectAllStudent()中的缓存
        redisTemplate.delete(keyAll);
        return student;
    }

    public List<Student> updateStudent(Student student) {
        String key = "student_";
        String keyAll = "student";
        ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) redisTemplate.delete(key);
        System.out.println("更新student信息到数据库，再缓存到redis中！");
        this.studentMapper.updateStudent(student);
        operations.set(key, student, 50, TimeUnit.SECONDS);
        //删除selectAllStudent()中的缓存
        redisTemplate.delete(keyAll);
        return selectAllStudent();

    }

    public List<Student> deleteStudentById(String id) {
        String key = "student_";
        String keyAll = "student";
        ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        System.out.println("从redis中删除student缓存信息！");
        if (hasKey) redisTemplate.delete(key);
        System.out.println("从数据库中删除student缓存信息！");
        this.studentMapper.deleteStudentById(id);
        //删除selectAllStudent()中的缓存
        redisTemplate.delete(keyAll);
        return selectAllStudent();
    }
    //分页查询所有数据，由于每页最多只有100条，一次查询数据量少，所以可以不用缓存
    public List<Student> selectAll(int pageNUm,int pageSize){
        PageHelper.startPage(pageNUm,pageSize);
        List<Student> list=studentMapper.selectAllStudent();
        return list;
    }
}
