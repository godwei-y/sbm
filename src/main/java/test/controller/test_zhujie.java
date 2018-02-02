package test.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import test.Dao.Mapper.StudentMapper;
import test.Dao.Po.ResultModel;
import test.Dao.Po.Student;
import test.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
@Api(value = "Student操作类",description = "学生操作类")
public class test_zhujie {

    @RequestMapping("/test1")
    @ResponseBody
    public String test1(){
        return "test ok!";//直接响应json
    }

    @RequestMapping("/test2")
    public String test2(){
        return "html/test3";//将返回结果解析为跳转路径
    }

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentMapper studentMapper;

    @ResponseBody
    @RequestMapping(value = "/finStudentByName",method = RequestMethod.GET)
    @ApiOperation(value = "查询一个记录",notes = "根据姓名查")
    @ApiResponses({@ApiResponse(code = 200,message = "success",response = Student.class),@ApiResponse(code = 500,message = "NUllPointerException")})
    public ResultModel finStudentByName(String name, Model model, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        ResultModel resultModel=new ResultModel();
        if(null == name || name.equals("")){
            model.addAttribute("finStudentByNameErrMsg","请输入name");
            resultModel.setSuccess("false");
            resultModel.setError_info("请输入name");
            return resultModel;
        }
        Student result;
        result=studentService.selectStudentByName(name);
        if(null==result){
            resultModel.setSuccess("true");
            List<Student> result_list=new ArrayList<>();
            result_list.add(result);
            resultModel.setResult(result_list);
        }
        else {
            resultModel.setSuccess("false");
            resultModel.setError_info("你要查询的记录不存在！");
        }
        return resultModel;
    }


    @ResponseBody
    @RequestMapping(value = "/finStudentAll",method = RequestMethod.GET)
    @ApiOperation(value = "查询所有记录",notes = "查询所有记录")
    public List<Student> finStudentAll(Model model,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        List<Student> result;
        result=studentService.selectAllStudent();
        model.addAttribute("students",result);
        return result;
    }

    @RequestMapping(value = "/finStudentById",method = RequestMethod.GET)
    @ApiOperation(value = "查询一个记录",notes = "根据id查")
    public String finStudentById(String id,Model model){
        if(null==id ||id.equals("")){
            model.addAttribute("finStudentByIdErrMsg","请输入id！");
            return "index";
        }
        Student result;
        result=studentService.selectStudentById(id);
        if(null==result ||result.equals("")){
            model.addAttribute("finStudentByIdErrMsg","该Id不存在！");
            return "index";
        }
        model.addAttribute("students",result);
        return "html/test3";
    }

    @RequestMapping(value = "/addStudent",method = RequestMethod.GET)
    @ApiOperation(value = "新增一个记录",notes = "新增一个记录")
    public String finStudentById(Student student,Model model){
        if(null==student.getId() ||student.getId().equals("")){
            model.addAttribute("addStudentErrMsg","请输入id！");
            return "index";
        }
        Student result;
        result=studentService.selectStudentById(student.getId());
        if(null!=result){
            model.addAttribute("addStudentErrMsg","该Id已存在！");
            return "index";
        }
        result=studentService.addStudent(student);
        model.addAttribute("students",result);
        return "html/test3";
    }

    @RequestMapping(value = "/deleteById",method = RequestMethod.GET)
    @ApiOperation(value = "删除一个记录",notes = "根据Id删除")
    public String deleteById(String id,Model model){
        if(null==id ||id.equals("")){
            model.addAttribute("deleteByIdErrMsg","请输入id！");
            return "index";
        }
        Student result1;
        result1=studentService.selectStudentById(id);
        if(null==result1){
            model.addAttribute("deleteByIdErrMsg","该Id不存在！");
            return "index";
        }

        List<Student> result=studentService.deleteStudentById(id);
        model.addAttribute("students",result);
        return "html/test3";
    }

    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    @ApiOperation(value = "更新一个记录",notes = "更新一个记录")
    public ResultModel update(Student student,Model model,HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        ResultModel resultModel = new ResultModel();
        if (null == student.getId() || student.getId().equals("")) {
            model.addAttribute("updateErrMsg", "请输入id！");
            resultModel.setSuccess("false");
            resultModel.setError_info("请输入id");
            return resultModel;
        }
        List<Student> result;
        result = studentService.selectAllStudent();
        int k;
        for (k = 0; k < result.size(); k++) {
            if (result.get(k).getId().equals(student.getId())) break;
        }
        if (k == result.size()) {
            resultModel.setSuccess("false");
            resultModel.setError_info("该id不存在！");
            return resultModel;
        }
        result = studentService.updateStudent(student);
        model.addAttribute("students", result);
        resultModel.setSuccess("true");
        resultModel.setResult(result);
        return resultModel;
    }

    //分页查询所有数据
    @RequestMapping(value = "/queryPage", method=RequestMethod.GET)
    public String selectAll(String pageNum,Model model){
        int pageSize=5;
        int pageNum1;
        if(null==pageNum ||pageNum.equals("")||Integer.parseInt(pageNum)<=1)
            pageNum="1";
        pageNum1=Integer.parseInt(pageNum);
        List<Student> list=this.studentService.selectAll(pageNum1,pageSize);
        PageInfo<Student> page=new PageInfo<>(list);
        model.addAttribute("students",list);
        model.addAttribute("pageNum",pageNum);
        int prePage,nextPage;
        if(Integer.parseInt(pageNum)==page.getPages())
            nextPage=1;
        else
            nextPage=Integer.parseInt(pageNum)+1;
        if(Integer.parseInt(pageNum)==1)
            prePage=page.getPages();
        else prePage=Integer.parseInt(pageNum)-1;
        model.addAttribute("prePage",prePage);
        model.addAttribute("nextPage",nextPage);
        model.addAttribute("firstPage",1);
        model.addAttribute("lastPage",page.getPages());
        return "html/test4";
    }
}
