package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            List<String> months = new ArrayList<>();
            List<Integer> memberCount;
            Map<String, Object> map = new HashMap<>();
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH, -12);      //先将月份往前移12个月，再往后一个月一个月移
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 12; i++) {
                instance.add(Calendar.MONTH, 1);
                months.add(sdf.format(instance.getTime()));
            }
            memberCount = memberService.findMemberCountByMonth(months); //不放在循环中，减少远程调用的次数
            map.put("months", months);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map<String, Object> map = new HashMap<>();
            List<String> setmealNames = new ArrayList<>();//存放所有被预约过的套餐名称
            List<Map<String, Object>> setmealCount = setmealService.getSetmealCount();//所有被预约过的套餐名称和对应的预约数
            setmealCount.forEach(countMap -> setmealNames.add((String) countMap.get("name")));
            map.put("setmealNames", setmealNames);
            map.put("setmealCount", setmealCount);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
//        //2.获取模板文件
//        String templatePath = request.getSession().getServletContext().getRealPath("template") +
//                File.separator + "report_template.xlsx";  //File.separator在Linux上表示左斜线，在Windows上表示为右斜线
//        try (ServletOutputStream out = response.getOutputStream();Workbook workbook = new XSSFWorkbook(new File(templatePath))){//工作簿
//            //1.拿数据
//            Map<String, Object> map = reportService.getBusinessReportData();
//            //取出返回结果数据，准备将报表数据写入到Excel文件中
//            String reportDate = (String) map.get("reportDate");
//            Integer todayNewMember = (Integer) map.get("todayNewMember");
//            Integer totalMember = (Integer) map.get("totalMember");
//            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
//            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
//            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
//            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
//            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
//            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
//            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
//            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
//            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) map.get("hotSetmeal");
//
//            //4.写数据
//            Sheet sheet = workbook.getSheetAt(0); //拿到Sheet1
//            AtomicReference<Row> row = new AtomicReference<>(sheet.getRow(2));  //拿到第三行，以下类似
//            row.get().getCell(5).setCellValue(reportDate);
//            row.set(sheet.getRow(4));
//            row.get().getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
//            row.get().getCell(7).setCellValue(totalMember);//总会员数
//
//            row.set(sheet.getRow(5));
//            row.get().getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
//            row.get().getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数
//
//            row.set(sheet.getRow(7));
//            row.get().getCell(5).setCellValue(todayOrderNumber);//今日预约数
//            row.get().getCell(7).setCellValue(todayVisitsNumber);//今日出游数
//
//            row.set(sheet.getRow(8));
//            row.get().getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
//            row.get().getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数
//
//            row.set(sheet.getRow(9));
//            row.get().getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
//            row.get().getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数
//
//            AtomicInteger rowNum = new AtomicInteger(12);
//
//            hotSetmeal.forEach(hotmealMap -> {
//                String name = (String) hotmealMap.get("name");
//                Long setmeal_count = (Long) hotmealMap.get("setmeal_count");
//                BigDecimal proportion = (BigDecimal) hotmealMap.get("proportion");
//                row.set(sheet.getRow(rowNum.getAndIncrement()));
//                row.get().getCell(4).setCellValue(name);//套餐名称
//                row.get().getCell(5).setCellValue(setmeal_count);//预约数量
//                row.get().getCell(6).setCellValue(proportion.doubleValue());//占比
//            });
//
//            //5.输出文件，以流形式下载，另存为操作,使用响应流
//            workbook.write(out);
//            out.flush();
//            //6.关闭
//        } catch (Exception e) {
//            e.printStackTrace();
//            //跳转错误页面
//            try {
//                request.getRequestDispatcher("/pages/error/downloadError.html").forward(request,response);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
        //获得Excel模板文件绝对路径
        //file.separator这个代表系统目录中的间隔符，说白了就是斜线。
        String templateRealPath = request.getSession().getServletContext().getRealPath("template") +
                File.separator + "report_template.xlsx";
        try (ServletOutputStream out = response.getOutputStream(); XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(templateRealPath))) {
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReportData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) result.get("hotSetmeal");


            //读取模板文件创建Excel表格对象
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for (Map<String, Object> map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载

            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
