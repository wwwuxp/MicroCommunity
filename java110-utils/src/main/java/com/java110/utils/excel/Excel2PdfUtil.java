package com.java110.utils.excel;

import com.aspose.cells.*;
import com.java110.utils.util.AsposeRegister;
import com.java110.utils.word.Doc2PdfUtil;

import java.io.*;

/**
 * @Author: LiHuaZhi
 * @Date: 2021/7/13 14:21
 * @Description:
 **/
public class Excel2PdfUtil {

    /**
     * 加载配置文件
     * @return
     */
    private static boolean getLicense() {
        boolean result = false;
        try (InputStream in = Doc2PdfUtil.class.getClassLoader().getResourceAsStream("license.xml")) {
            License license = new License();
            license.setLicense(in);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void excel2Pdf(InputStream inputStream, OutputStream outputStream){
        System.out.println("pdf转换中...");
        long old = System.currentTimeMillis();
        try {
            // 验证
            // if (!getLicense()) {
            //     throw new RuntimeException("文件转换失败!");
            // }
            AsposeRegister.registerExcel_v_22_6();
            // 设置字体包位置 - 不同环境可用从配置文件中获取
            // IndividualFontConfigs configs = new IndividualFontConfigs();
            // // configs.setFontFolder("/usr/share/fonts/chinese", true);
            // configs.setFontFolder("C:\\Windows\\Fonts", true);
            // LoadOptions loadOptions = new LoadOptions();
            // loadOptions.setFontConfigs(configs);

            Workbook workbook;
            if (inputStream != null) {
                // workbook = new Workbook(inputStream, loadOptions);
                workbook = new Workbook(inputStream);
            }else {
                return;
            }
            PdfSaveOptions opts = new PdfSaveOptions();
            // 设置excel不换行在pdf显示
            // opts.setAllColumnsInOnePagePerSheet(true);
            // 设置一个sheet在一页pdf
            opts.setOnePagePerSheet(true);
            workbook.save(outputStream, opts);
            long now = System.currentTimeMillis();
            System.out.println("pdf转换成功，共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件转换失败!");
        } finally {
        }
    }

    /**
     * @param inputStream
     * @param docPath
     * @param pdfPath
     * @description 如果流不为空则使用转pdf，反之则用docPath进行转pdf
     */
    public static File excel2Pdf(InputStream inputStream, String docPath, String pdfPath) {
        System.out.println("pdf转换中...");
        long old = System.currentTimeMillis();
        File pdfFile = new File(pdfPath);
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            // 验证
            if (!getLicense()) {
                throw new RuntimeException("文件转换失败!");
            }
            // 设置字体包位置 - 不同环境可用从配置文件中获取
            IndividualFontConfigs configs = new IndividualFontConfigs();
            // configs.setFontFolder("/usr/share/fonts/chinese", true);
            configs.setFontFolder("C:\\Windows\\Fonts", true);
            LoadOptions loadOptions = new LoadOptions();
            loadOptions.setFontConfigs(configs);

            Workbook workbook;
            if (inputStream != null) {
                workbook = new Workbook(inputStream, loadOptions);
            } else {
                workbook = new Workbook(docPath, loadOptions);
            }
            PdfSaveOptions opts = new PdfSaveOptions();
            // 设置excel不换行在pdf显示
            // opts.setAllColumnsInOnePagePerSheet(true);
            // 设置一个sheet在一页pdf
            opts.setOnePagePerSheet(true);
            workbook.save(fos, opts);
            long now = System.currentTimeMillis();
            System.out.println("pdf转换成功，共耗时：" + ((now - old) / 1000.0) + "秒");
            return pdfFile;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件转换失败!");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
