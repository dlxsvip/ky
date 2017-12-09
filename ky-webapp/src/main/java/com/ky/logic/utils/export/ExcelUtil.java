package com.ky.logic.utils.export;

import com.ky.logic.utils.LoggerUtil;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yl on 2017/8/23.
 */
public enum ExcelUtil {
    INSTANCE;

    private WritableWorkbook workbook = null;
    private List<WritableSheet> sheetList = new ArrayList<>();
    private WritableCellFormat wcf_center;
    private WritableCellFormat wcf_left;


    /**
     * 创建工作簿
     *
     * @param response 响应到请求中
     */
    public void createWorkbook(HttpServletResponse response, String fileName) {
        try {
            response.reset();
            response.setContentType("application/OCTET-STREAM;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".xls");

            /** **********创建工作簿************ */
            workbook = Workbook.createWorkbook(response.getOutputStream());
            /** ************设置单元格字体************** */
            setWorkbook();
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "createWritableWorkbook", "创建工作簿异常" + e.getMessage());
        }
    }

    /**
     * 创建工作簿
     *
     * @param filePath Excel 文件路径
     */
    public void createWorkbook(String filePath) {
        try {
            /** **********创建工作簿************ */
            workbook = Workbook.createWorkbook(new File(filePath));
            /** ************设置单元格字体************** */
            setWorkbook();
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "createWritableWorkbook", "创建工作簿异常" + e.getMessage());
        }
    }

    /**
     * 创建 Sheet 页
     *
     * @param sheetName Sheet 页名
     * @param data      数据
     */
    public void createSheet(String sheetName, List<List<String>> data) {
        WritableSheet sheet = workbook.createSheet(sheetName, 0);
        SheetSettings sheetSet = sheet.getSettings();
        sheetSet.setProtected(false);
        sheetList.add(sheet);
        try {

            for (int i = 0; i < data.size(); i++) {
                List<String> d = data.get(i);
                for (int j = 0; j < d.size(); j++) {
                    sheet.addCell(new Label(j, i, d.get(j), this.wcf_center));
                }
            }

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存中的内容写到EXCEL文件中
     */
    public void writeSheet() {
        try {
            workbook.write();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            sheetList = new ArrayList<>();
            workbook = null;
        }

    }


    private void setWorkbook() throws Exception {

        WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
        WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        // 用于标题居中
        this.wcf_center = new WritableCellFormat(BoldFont);
        wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
        wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
        wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
        wcf_center.setWrap(true); // 文字是否换行

        // 用于正文居左
        this.wcf_left = new WritableCellFormat(NormalFont);
        wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
        wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
        wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
        wcf_left.setWrap(true); // 文字是否换行

    }
}
