package test;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import java.util.Properties;


@MapperScan("test.Dao.Po")
@SpringBootApplication
@EnableSwagger2
@EnableCaching
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public PageHelper pageHelper(){
            PageHelper pageHelper=new PageHelper();
        Properties p=new Properties();
        p.setProperty("offsetAsPageNUm","true");
        p.setProperty("rowBoundsWithCount","true");
        p.setProperty("reasonable","true");
        p.setProperty("dialect","mysql");
        p.setProperty("supportMethodsArguments","false");
        p.setProperty("pageSizeZero","true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

/*    @Bean
    private MultipartConfigElement multipartConfigElement(){
        //对上传文件作限制
        MultipartConfigFactory factory=new MultipartConfigFactory();
        //设置文件大小
        factory.setMaxFileSize("1024KB");
        //设置宗上传数据总大小
        factory.setMaxRequestSize("12MB");
        //设置上传到..目的地：工程绝对路径
        //factory.setLocation("D:/....springboot/src.../resources/uploadFile/");
        return factory.createMultipartConfig();
    }*/
}
