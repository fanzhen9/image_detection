package com.sailing.image.web;

import com.sailing.image.data.LoadData;
import com.sailing.image.service.WorkService;
import com.sailing.image.utils.ExcleUtilJX;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
@RequestMapping
public class UploadController {


    @Autowired
    private WorkService workService;

    /**
     * 创建任务
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @PostMapping("/createTask")
    public Map uploadExcle(@RequestParam("file")MultipartFile file) throws IOException, InvalidFormatException {

        FileInputStream in = (FileInputStream) file.getInputStream(); // 文件流
        String fileName = file.getOriginalFilename();
        Workbook workbook = ExcleUtilJX.getWorkbok(in,fileName);
        String taskId = workService.createTask(workbook);
        Map map = new HashMap();
        map.put("taskId",taskId);
        return map;
    }

    /**
     * 获取任务列表
     * @return
     */
    @GetMapping("/getTask")
    public Set<String> task(){
        return LoadData.taskMap.keySet();
    }

    /**
     * 提交设备状态
     * @param Task
     * @param apeId
     * @param status
     * @return
     */
    @GetMapping("/submit")
    public Map submit(String Task,String apeId,String status){
        return workService.submit(Task,apeId,status);
    }

    /**
     * 导出excle
     * @param resp
     * @param taskId
     * @throws IOException
     */
    @GetMapping("/export")
    public void exportResult(HttpServletResponse resp,String taskId) throws IOException {
        workService.export(resp,taskId);
    }

    @GetMapping("/clean")
    public String cleanAll(){
        LoadData.result.clear();
        LoadData.taskMap.clear();
        return "ok";
    }
}
