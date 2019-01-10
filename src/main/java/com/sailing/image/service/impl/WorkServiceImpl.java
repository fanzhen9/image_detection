package com.sailing.image.service.impl;

import com.sailing.image.dao.SnapshotDao;
import com.sailing.image.data.LoadData;
import com.sailing.image.dto.ApeStatus;
import com.sailing.image.service.WorkService;
import com.sailing.image.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    private SnapshotDao snapshotDao;


    @Override
    public String createTask(Workbook workbook) {
        String task = UUID.randomUUID().toString();
        int sheetCount = workbook.getNumberOfSheets(); //获取sheet的数量
        Sheet sheet = workbook.getSheetAt(0);//获取第一个sheet
        Integer rows = sheet.getLastRowNum();//获取总行数
        Queue apeQueue = new LinkedList();
        for(int i=1;i<=rows;i++){
            String ape = sheet.getRow(i).getCell(0).toString();
            apeQueue.add(new ApeStatus(ape));
        }
        LoadData.taskMap.put(task,apeQueue);

        return task;
    }

    @Override
    public Map submit(String taskId, String apeId, String status) {
        Map map = new HashMap();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datas = sdf.format(date);
        String startTime = datas+" 00:00:00";
        String endTime = datas+" 23:59:59";
        List<Map> list = null;
        Queue<?> apeQueue = LoadData.taskMap.get(taskId);
        ApeStatus apeStatus = (ApeStatus) apeQueue.poll();
        if(apeStatus == null){
            //消费完了
            list = new ArrayList<>();
            map.put("code","2");
            map.put("msg","所有摄像头都被消费完");
            map.put("content",list);

        }else{
            list = snapshotDao.queryPerson(startTime,endTime,apeStatus.getApeId());
            map.put("code","0");
            map.put("msg","成功");
            map.put("content",list);
        }
        if(!StringUtils.isEmpty(apeId)) {
            if (LoadData.result.containsKey(taskId)) {
                List<ApeStatus> resultList = (List<ApeStatus>) LoadData.result.get(taskId);
                ApeStatus status1 = new ApeStatus(apeId,status);
                //apeStatus.setStatus(status1);
                resultList.add(status1);
            } else {
                List<ApeStatus> resultList = new ArrayList<>();
                ApeStatus status1 = new ApeStatus(apeId,status);
                resultList.add(status1);
                LoadData.result.put(taskId, resultList);
            }
        }


        return map;
    }

    @Override
    public void export(HttpServletResponse resp, String taskId) throws IOException {

        if(!LoadData.result.containsKey(taskId)){
            resp.getWriter().write("task not exit");
            return;
        }

        List<ApeStatus> pageMap = (List<ApeStatus>) LoadData.result.get(taskId);
        String[] header = {"设备ID","状态"};
        String[] headers = new String[]{"apeId", "status"};
        List<Object[]> datas = new ArrayList<>();

        for (ApeStatus map : pageMap) {
            Object[] objects = new Object[headers.length];
            objects[0]= map.getApeId();
            objects[1]= map.getStatus();
            /*for (int j = 0; j < headers.length; j++) {
                if (map.get(headers[j]) == null) {
                    objects[j] = "";
                } else {
                    objects[j] = map.get(headers[j]);
                }
            }*/

            datas.add(objects);
        }
        ExcelUtil.excelExport("apeStatus",header,datas,resp);
    }
}
