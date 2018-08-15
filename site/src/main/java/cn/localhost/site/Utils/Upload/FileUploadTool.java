package cn.localhost.site.Utils.Upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;

@PropertySource(value = {"classpath:application.properties"},encoding="utf-8")
@Component
public class FileUploadTool {
    @Resource
    TransfMediaTool transfMediaTool = new TransfMediaTool();

    @Value("${file.logoRealPathDir}")
    String logoRealPathDir;

    // 文件最大
//    @Value("${video.upload_maxsize}")
    private static long upload_maxsize = 800*1024*1024;
    // 文件允许格式
    private static String[] allowFiles = { ".png", ".jpg", ".jpeg", ".mp4"};
    // 允许转码的视频格式（ffmpeg）
    private static String[] allowFLV = { ".avi", ".mpg", ".wmv", ".3gp",
            ".mov", ".asf", ".asx", ".vob" };
    // 允许的视频转码格式(mencoder)
    private static String[] allowAVI = { ".wmv9", ".rm", ".rmvb" };

    public FileEntity createFile(MultipartFile multipartFile, HttpServletRequest request, int type, String uuid) throws Exception{
        boolean bflag = false;
        String fileName = multipartFile.getOriginalFilename().toString();
        // 判断文件不为空
        if (multipartFile.getSize() != 0 && !multipartFile.isEmpty()) {
            bflag = true;
            // 判断文件大小
            if (multipartFile.getSize() <= upload_maxsize) {
                bflag = true;
                // 文件类型判断
                if (this.checkFileType(fileName)) {
                    bflag = true;
                } else {
                    bflag = false;
                    throw new Exception("文件类型不允许");
                }
            } else {
                bflag = false;
                throw new Exception("文件大小超范围");
            }
        } else {
            bflag = false;
            throw new Exception("文件为空");

        }
        if (bflag) {
            String logoPathDir = "/video/";
//            String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir);
            // 上传到本地磁盘
            String nowPath = null;
            switch (type){
                case 0:
                    nowPath = logoRealPathDir+"/picture";
                    break;
                case 1:
                    nowPath = logoRealPathDir+"/video";
                    break;
                case 3:
                    nowPath = logoRealPathDir+"/profile_photo";
                    break;
            }
            File logoSaveFile = new File(nowPath);
            if (!logoSaveFile.exists()) {
                logoSaveFile.mkdirs();
            }
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            System.out.println("fileName:" + name);
            // 新的文件名
            String newFileName = this.
                    getName(uuid+fileName.substring(fileName.lastIndexOf("."), fileName.length()));
            // 文件扩展名
            String fileEnd = this.getFileExt(fileName);
            // 绝对路径
            String fileNamedirs = nowPath + File.separator + uuid + fileEnd;
//            String fileNamedirs = nowPath + File.separator + newFileName + fileEnd;
            System.out.println("SavePath:" + fileNamedirs);
            File filedirs = new File(fileNamedirs);
            // 转入文件
            try {
                multipartFile.transferTo(filedirs);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 相对路径
            FileEntity entity = new FileEntity();
            entity.setType(fileEnd);
            String fileDir = logoPathDir + newFileName + fileEnd;
            StringBuilder builder = new StringBuilder(fileDir);
            String finalFileDir = builder.substring(1);
            // size存储为String
            String size = this.getSize(filedirs);
            // 源文件保存路径
            String aviPath = filedirs.getAbsolutePath();
            // 转码Avi
//            boolean flag = false;
            if (this.checkAVIType(fileEnd)) {
                // 设置转换为AVI格式后文件的保存路径
                String codcAviPath = logoRealPathDir + File.separator + newFileName + ".avi";
                // 获取配置的转换工具（mencoder.exe）的存放路径
                String mencoderPath = request.getSession().getServletContext().getRealPath("/tools/mencoder.exe");
                aviPath = transfMediaTool.processAVI(mencoderPath, filedirs.getAbsolutePath(), codcAviPath);
                fileEnd = this.getFileExt(codcAviPath);
            }
            if (aviPath != null) {
                // 转码Flv
                if (this.checkMediaType(fileEnd)) {
                    try {
                        // 设置转换为flv格式后文件的保存路径
                        String codcFilePath = logoRealPathDir + File.separator + newFileName + ".flv";
                        // 获取配置的转换工具（ffmpeg.exe）的存放路径
                        String ffmpegPath = request.getSession().getServletContext().getRealPath("/tools/ffmpeg.exe");
                        transfMediaTool.processFLV(ffmpegPath, aviPath,    codcFilePath);
                        fileDir = logoPathDir + newFileName + ".flv";
                        builder = new StringBuilder(fileDir);
                        finalFileDir = builder.substring(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                entity.setSize(size);
                entity.setPath(finalFileDir);
                entity.setTitleOrig(name);
                entity.setTitleAlter(newFileName);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                entity.setUploadTime(timestamp);
                return entity;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    private boolean checkFileType(String fileName) {
        Iterator<String> type = Arrays.asList(allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 视频类型判断(flv)
     *
     * @return
     */
    private boolean checkMediaType(String fileEnd) {
        Iterator<String> type = Arrays.asList(allowFLV).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileEnd.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 视频类型判断(AVI)
     *
     * @return
     */
    private boolean checkAVIType(String fileEnd) {
        Iterator<String> type = Arrays.asList(allowAVI).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileEnd.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件扩展名
     *
     * @return string
     */
    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 依据原始文件名生成新文件名
     * @return
     */
    private String getName(String fileName) {
        Iterator<String> type = Arrays.asList(allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.contains(ext)) {
                String newFileName = fileName.substring(0, fileName.lastIndexOf(ext));
                return newFileName;
            }
        }
        return "";
    }

    /**
     * 文件大小，返回kb.mb
     *
     * @return
     */
    private String getSize(File file) {
        String size = "";
        long fileLength = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024) {
            size = df.format((double) fileLength) + "BT";
        } else if (fileLength < 1048576) {
            size = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            size = df.format((double) fileLength / 1048576) + "MB";
        } else {
            size = df.format((double) fileLength / 1073741824) + "GB";
        }
        return size;
    }
}
