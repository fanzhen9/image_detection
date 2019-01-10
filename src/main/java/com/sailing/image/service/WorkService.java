package com.sailing.image.service;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WorkService {

    String createTask(Workbook workbook);

    Map submit(String taskId, String apeId, String status);

    void export(HttpServletResponse resp,String taskId) throws IOException;
}
