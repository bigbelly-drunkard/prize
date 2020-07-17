package org.egg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/14 20:42
 */
public class UploadFileUtil {
//    private static final String BASE_PATH = "/export/renting/upload";
    private static final String BASE_PATH = "D:/export/renting/upload";
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileUtil.class);

    /**
     * 上传文件返回文件路径
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String upload(MultipartFile file) throws IOException {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        LOGGER.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        LOGGER.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = BASE_PATH;
        // 解决中文问题，liunx下中文路径，图片显示问题
         fileName = UUID.randomUUID() + suffixName;
        //File dest = new File(new StringBuilder(filePath).append(fileName).append("-").append(System.currentTimeMillis()).toString());
        File dest = new File(new StringBuilder(filePath).append(fileName).toString());
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        LOGGER.info("file upload sucess,fileName={},suffixName={}", fileName, suffixName);
        return dest.getPath();
    }

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath){
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();//"删除单个文件"+name+"成功！"
            LOGGER.info("file deleteFile sucess,filePath={}", filePath);
            return true;
        }//"删除单个文件"+name+"失败！"
        LOGGER.info("file deleteFile fail,filePath={}", filePath);
        return false;
    }
    /**
     * 备用 需改善
     *
     * @param file
     * @param response
     */
    public static void download(File file, HttpServletResponse response) {
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + "");// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("success");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
