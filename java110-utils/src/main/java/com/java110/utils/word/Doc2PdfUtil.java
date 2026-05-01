package com.java110.utils.word;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.java110.utils.util.AsposeRegister;

import java.io.*;

/**
 * @Author: LiHuaZhi
 * @Date: 2021/7/13 14:21
 * @Description:
 **/
public class Doc2PdfUtil {


    /**
     * 加载配置文件
     *
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
    public static void doc2Pdf(InputStream inputStream, OutputStream outputStream){
        System.out.println("pdf转换中...");
        long old = System.currentTimeMillis();

        try {
            // 验证
            // if (!getLicense()) {
            //     throw new RuntimeException("文件转换失败!");
            // }
            AsposeRegister.registerWord_v_22_5();
            Document document;
            if (inputStream != null) {
                document = new Document(inputStream);
            } else {
                return;
            }
            //  DocumentBuilder builder = new DocumentBuilder(document);
            // 设置纸张大小
            //  builder.getPageSetup().setPaperSize(PaperSize.A3);
            // 设置横向
            // builder.getPageSetup().setOrientation(Orientation.LANDSCAPE);
            document.save(outputStream, SaveFormat.PDF);
            long now = System.currentTimeMillis();
            System.out.println("pdf转换成功，共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件转换失败!");
        } finally {
        }
    }

    /**
     * doc转pdf
     *
     * @param docPath doc文件路径，包含.doc
     * @param pdfPath pdf文件路径，包含.pdf
     * @description 如果流不为空则使用转pdf，反正则用docPath进行转pdf
     */
    public static File doc2Pdf(InputStream inputStream, String docPath, String pdfPath) {
        System.out.println("pdf转换中...");
        long old = System.currentTimeMillis();

        File pdfFile = new File(pdfPath);
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            // 验证
            if (!getLicense()) {
                throw new RuntimeException("文件转换失败!");
            }
            // FontSettings.getDefaultInstance().setFontsFolder("/usr/share/fonts/chinese", true);
            FontSettings.getDefaultInstance().setFontsFolder("C:\\Windows\\Fonts", true);

            Document document;
            if (inputStream != null) {
                document = new Document(inputStream);
            } else {
                document = new Document(docPath);
            }
            //  DocumentBuilder builder = new DocumentBuilder(document);
            // 设置纸张大小
            //  builder.getPageSetup().setPaperSize(PaperSize.A3);
            // 设置横向
            // builder.getPageSetup().setOrientation(Orientation.LANDSCAPE);
            document.save(fos, SaveFormat.PDF);
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
