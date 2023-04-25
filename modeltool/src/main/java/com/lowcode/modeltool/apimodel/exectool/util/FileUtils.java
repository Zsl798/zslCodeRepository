/******************************************************************************
 * Copyright (C) 2019 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.lowcode.modeltool.apimodel.exectool.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * 文件处理工具类
 *
 * @author 陈刚
 * @since 1.0
 * @version 2019年5月7日
 */
public class FileUtils {

    /** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /** objResourcePatternResolver */
    private static ResourcePatternResolver objResourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * 获取临时文件夹路径
     *
     * @return tempPath
     */
    public static String getTempPath() {
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator;
        return tempPath;
    }

    /**
     * 获取当前时间并转化格式 用时间为文件命名
     *
     * @param typeName typeName
     * @return packageName
     */
    public static String getPackageName(String typeName) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String packageName = typeName + "_" + sdf.format(date);
        return packageName;
    }



    /**
     * @param tempFile tempFile
     * @Description：删除当前日期半天之前的文件 2019年4月3日 上午10:27:40
     */
    public static void cleanTempFiles(File tempFile) {
        Date date = new Date();
        File[] files = tempFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.lastModified() < date.getTime() - 1 * 12 * 3600 * 1000) {
                if (file.isDirectory()) {
                    cleanTempFiles(file);
                } else {
                    file.delete();
                }
                file.delete();
            }
        }
    }

    /**
     * @Description：删除当前日期半天之前的文件 2019年4月3日 上午10:27:40
     */
    public static void cleanInputTempFiles() {
        // 清理临时文件夹
        String rootPath = getInputRootPath("dataInput");
        File tempFile = new File(rootPath);
        cleanTempFiles(tempFile);
    }

    /**
     * @Description：删除当前日期半天之前的文件 2019年4月3日 上午10:27:40
     */
    public static void cleanOutputTempFiles() {
        // 清理临时文件夹
        String rootPath = getOutputRootPath();
        File tempFile = new File(rootPath);
        cleanTempFiles(tempFile);
    }

    /**
     * 删除文件
     *
     * @param fileRealPath 文件路径
     */
    public static void cleanTempFiles(String fileRealPath) {
        File file = new File(fileRealPath);
        if (file.isDirectory()) {
            File[] lstChildFile = file.listFiles();
            for (File chFile : lstChildFile) {
                cleanTempFiles(chFile.getPath());
            }
        } else {
            file.delete();
        }
        file.delete();
    }

    /**
     * 上传文件并解压
     *
     * @param file file
     * @param typename typename
     * @return resultPath
     * @throws RuntimeException RuntimeException
     */
    public static String uploadAndUnZipFiles(MultipartFile file, String typename) throws RuntimeException {
        // 上传文件到服务器
        String filePath;
        try {
            filePath = uploadFile(file, typename, "dataInput");
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败");
        }
        // 解压缩
        String resultPath;
        try {
            resultPath = unZipFiles(filePath);
        } catch (Exception e) {
            throw new RuntimeException("文件解压失败", e);
        }
        return resultPath;
    }

    /**
     * 上传文件到服务器
     *
     * @param file file
     * @param typename typename
     * @param uploadType 上传的类型 例：dataInput或者excelImport
     * @return filePath 上传的文件保存路径（带有zip）
     * @throws Exception Exception
     */
    public static String uploadFile(MultipartFile file, String typename, String uploadType) throws Exception {
        String filePath = "";
        String rootPath = getInputRootPath(uploadType);
        // 查看路径是否存在，不存在就创建
        if (!new File(rootPath).exists()) {
            new File(rootPath).mkdirs();
        }
        // 判断文件是否为空
        if (!file.isEmpty()) {
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
            // 文件保存路径
            // 获取系统临时目录
            filePath = rootPath + getPackageName(typename) + fileType;
            // 转存文件
            File objNewFile = new File(filePath);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(file.getInputStream(), objNewFile);
            // file.transferTo(new File(filePath));
        }
        return filePath;
    }

    /**
     * 获取导入文件根路径
     *
     * @param uploadType 上传文件的类型 是数据集数据报道导入或者excel导入
     * @return rootPath
     */
    public static String getInputRootPath(String uploadType) {
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator;
        String rootPath = tempPath + uploadType + File.separator;
        return rootPath;
    }

    /**
     * 获取导出文件根路径
     *
     * @return rootPath
     */
    public static String getOutputRootPath() {
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator;
        // 在临时目录下创建一个文件夹，方便管理
        String rootPath = tempPath + "dataOutput" + File.separator;
        return rootPath;
    }

    /**
     * 解压缩
     *
     * @param filePath 上传的文件保存路径（带有zip）
     * @return resultPath
     * @throws Exception Exception
     */
    public static String unZipFiles(String filePath) throws Exception {
        String unZipPath = getInputRootPath("dataInput");
        String resultPath = "";
        resultPath = unZipFiles(filePath, unZipPath);
        return resultPath;
    }

    /**
     * 解压文件到指定目录 解压后的文件名，和之前一致
     *
     * @param zipPath 上传的文件保存路径（带有zip）
     * @param descDir 指定目录（导入文件根路径）
     * @return unZipPath
     * @throws IOException IOException
     */
    public static String unZipFiles(String zipPath, String descDir) throws IOException {
        // 当前压缩文件
        File zipFile = new File(zipPath);
        zipFile.getName();
        // 创建压缩文件对象
        ZipFile zip = null;
        String resultPath = null;
        try {
            zip = new ZipFile(zipFile, Charset.forName("GBK"));// 解决中文文件夹乱码
            String name = zipFile.getName().substring(zipFile.getName().lastIndexOf('/') + 1,
                    zipFile.getName().lastIndexOf('.'));
            resultPath = descDir + name;
            File pathFile = new File(resultPath);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                // 跳过根目录，获取下一个zipEntry
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                // 解压出的文件路径
                String outPath = (descDir + name + "/" + zipEntryName).replaceAll("\\*", "/");
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                // 判断路径是否存在,不存在则创建文件路径
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                FileOutputStream out = new FileOutputStream(outPath);
                // 用输入流读取压缩文件中指定目录中的文件
                // 定义一个字节数组,相当于缓存
                byte[] buffer = new byte[1024];
                // 得到实际读取到的字节数 读到最后返回-1
                int n = 0;
                // 把in里的东西读到buffer数组里去
                while ((n = in.read(buffer)) > 0) {
                    // 把字节转成String 从0到N变成String
                    out.write(buffer, 0, n);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("数据集导入失败", e);
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (Exception e) {
                    throw new RuntimeException("数据集导入失败", e);
                }
            }
        }
        return resultPath;
    }

    // /**
    // * 解压文件到指定目录
    // * 解压后的文件名，和之前一致
    // * @param zipPath 上传的文件保存路径（带有zip）
    // * @param descDir 指定目录（导入文件根路径）
    // * @return unZipPath
    // * @throws DataTransferException Exception
    // */
    // public static String unZipFiles(String zipPath, String descDir) throws
    // DataTransferException {
    // File file = new File(zipPath);
    // ZipInputStream zin=null;
    // String resultPath = null;
    // try{
    // ZipFile zipFile = new ZipFile(file);
    // // ZipFile zipFile = new
    // ZipFile(zipFile,Charset.forName("GBK"));//解决中文文件夹乱码
    // zin = new ZipInputStream(new FileInputStream(zipPath));
    // BufferedInputStream Bin = new BufferedInputStream(zin);
    // String name =
    // zipFile.getName().substring(zipFile.getName().lastIndexOf('\\')+1,
    // zipFile.getName().lastIndexOf('.'));
    // resultPath = descDir+name;
    // File Fout = null;
    // ZipEntry entry = null;
    // while(((entry = zin.getNextEntry())!=null) && !entry.isDirectory()){
    // Fout = new File(resultPath,entry.getName());
    // if(!Fout.exists()){
    // (new File(Fout.getParent())).mkdirs();
    // }
    // FileOutputStream os = new FileOutputStream(Fout);
    // BufferedOutputStream Bout = new BufferedOutputStream(os);
    // // InputStream in = zipFile.getInputStream(entry);
    // int count = 0;
    // while((count = Bin.read())!=-1){
    // Bout.write(count);
    // }
    // Bout.close();
    // os.close();
    // // in.close();
    // zin.closeEntry();
    // }
    // Bin.close();
    // }catch(Exception e){
    // throw new DataTransferException("数据导入失败",e);
    // }finally{
    // if(zin!=null){
    // try {
    // zin.close();
    // } catch (Exception e) {
    // throw new DataTransferException("关闭输入流失败");
    // }
    // }
    // }
    // return resultPath;
    // }

    // /**
    // * zip解压
    // * @param zipPath zip源文件路径
    // * @param destDirPath 解压后的目标文件夹
    // * @return path
    // * @throws RuntimeException 解压失败会抛出运行时异常
    // */
    // public static String unZipFiles(String zipPath, String destDirPath)
    // throws RuntimeException {
    // //当前压缩文件
    // String resultPath = null;
    // File srcFile = new File(zipPath);
    // long start = System.currentTimeMillis();
    // // 判断源文件是否存在
    // if (!srcFile.exists()) {
    // throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
    // }
    // // 开始解压
    // ZipFile zipFile = null;
    // try {
    // zipFile = new ZipFile(srcFile);
    // Enumeration<?> entries = zipFile.entries();
    // while (entries.hasMoreElements()) {
    // ZipEntry entry = (ZipEntry) entries.nextElement();
    // System.out.println("解压" + entry.getName());
    // String name =
    // zipFile.getName().substring(zipFile.getName().lastIndexOf('\\')+1,
    // zipFile.getName().lastIndexOf('.'));
    // resultPath = destDirPath+name;
    // // 如果是文件夹，就创建个文件夹
    // if (entry.isDirectory()) {
    // String dirPath = destDirPath + entry.getName();
    // File dir = new File(dirPath);
    // dir.mkdirs();
    // } else {
    // // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
    // File targetFile = new File(destDirPath + "/" + entry.getName());
    // // 保证这个文件的父文件夹必须要存在
    // if(!targetFile.getParentFile().exists()){
    // targetFile.getParentFile().mkdirs();
    // }
    // targetFile.createNewFile();
    // // 将压缩文件内容写入到这个文件中
    // InputStream is = zipFile.getInputStream(entry);
    // FileOutputStream fos = new FileOutputStream(targetFile);
    // int len;
    // byte[] buf = new byte[1024];
    // while ((len = is.read(buf)) != -1) {
    // fos.write(buf, 0, len);
    // }
    // // 关流顺序，先打开的后关闭
    // fos.close();
    // is.close();
    // }
    // }
    // long end = System.currentTimeMillis();
    // System.out.println("解压完成，耗时：" + (end - start) +" ms");
    // } catch (Exception e) {
    // throw new RuntimeException("unzip error from ZipUtils", e);
    // } finally {
    // if(zipFile != null){
    // try {
    // zipFile.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // return resultPath;
    // }

    /**
     * 压缩文件
     *
     * @param srcfile 待压缩源文件
     * @param target 目标文件
     * @throws RuntimeException RuntimeException
     */
    public static void zipFiles(File srcfile, File target) throws RuntimeException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(target));
            if (srcfile.isFile()) {
                zipFile(srcfile, out, "");
            } else {
                File[] list = srcfile.listFiles();
                if (list != null) {
                    for (int i = 0; i < list.length; i++) {
                        compress(list[i], out, "");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("压缩失败");
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                throw new RuntimeException("压缩失败");
            }
        }
    }

    /**
     * 压缩所有文件
     *
     * @param pngFilePathLst 待压缩文件路径集合
     * @param target 目标文件
     * @throws RuntimeException RuntimeException
     */
    public static void zipFilePathLst(List<String> pngFilePathLst, String target) {
    	File file = new File(target);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
        		ZipOutputStream out = new ZipOutputStream(fileOutputStream)) {
            for (String pngFilePath : pngFilePathLst) {
            	compress(new File(pngFilePath), out, "");
            }
        } catch (Exception e) {
            throw new RuntimeException("压缩失败");
        }
    }

    /**
     * 压缩文件夹里的文件 起初不知道是文件还是文件夹--- 统一调用该方法
     *
     * @param file 源文件
     * @param out zip输出流
     * @param basedir 父文件夹名称
     */
    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            zipDirectory(file, out, basedir);
        } else {
            zipFile(file, out, basedir);
        }
    }

    /**
     * 压缩单个文件
     *
     * @param srcfile 源文件
     * @param out zip输出流
     * @param basedir 父文件夹名称
     * @throws RuntimeException RuntimeException
     */
    public static void zipFile(File srcfile, ZipOutputStream out, String basedir) throws RuntimeException {
        if (!srcfile.exists())
            return;

        byte[] buf = new byte[1024];
        FileInputStream in = null;
        try {
            int len;
            // 创建 FileInputStream 对象
            in = new FileInputStream(srcfile);
            // 创建新的进入点
            out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));
            // 如果没有到达流的尾部
            while ((len = in.read(buf)) > 0) {
                // 将字节写入当前zip条目中
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException("压缩失败");
        } finally {
            try {
                if (out != null)
                    out.closeEntry();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                throw new RuntimeException("压缩失败");
            }
        }
    }

    /**
     * 压缩文件夹
     *
     * @param dir 待压缩源文件夹
     * @param out zip输出流
     * @param basedir 父文件夹名称
     */
    public static void zipDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;
        // 获取路径数组
        File[] files = dir.listFiles();
        // 循环遍历数组中的文件
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩单个文件
     *
     * @param rootPath 根目录
     * @param packageName 文件名
     */
    public static void zipFile(String rootPath, String packageName) {
        // 压缩文件
        String zipPath = rootPath + packageName;
        File zipFile = new File(zipPath);
        File target = new File(rootPath, packageName + ".zip");
        zipFiles(zipFile, target);
    }

    /**
     *
     * 将文件写入到指定位置存放
     *
     * @param strCustomPluginObj 组件对象
     * @param targetFilePath 文件目标路径
     * @param fileName fileName
     * @return true or false
     * @author zhangtao2
     * @time 2020年7月13日 上午10:51:57
     */
    public static boolean contentWriteToFile(String strCustomPluginObj, String targetFilePath, String fileName) {
        boolean flag = true;
        String filePath = targetFilePath + File.separator + fileName; // 将文件
        // 判断文件夹是否存在
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(strCustomPluginObj);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            LOGGER.error("文件写入失败");
        }
        return flag;
    }

    /**
     *
     * 删除指定目录下的文件
     *
     * @param filePath filePath
     * @author zhangtao2
     * @return true or fasle
     * @time 2020年7月10日 下午5:19:36
     */
    public static boolean deleteFiles(String filePath) {
        File file = new File(filePath);// 根据指定的文件名创建File对象
        if (!file.exists()) { // 要删除的文件不存在
            LOGGER.error("文件" + filePath + "不存在，删除失败！");
            return false;
        }
        if (file.isFile()) { // 如果目标文件是文件
            return deleteFile(filePath);
        }
        return deleteDir(filePath);
    }

    /**
     * 判断指定的文件删除是否成功
     *
     * @param fileName 文件路径
     * @return true or false 成功返回true，失败返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);// 根据指定的文件名创建File对象
        if (file.exists() && file.isFile()) { // 要删除的文件存在且是文件
            if (file.delete()) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * deleteFileLst
     * @param filePathLst filePathLst
     */
    public static void deleteFileLst(List<String> filePathLst) {
    	filePathLst.forEach(filePath -> {
    		File file = new File(filePath); // 根据指定的文件名创建File对象
            if (file.exists() && file.isFile()) { // 要删除的文件存在且是文件
            	file.delete();
            }
    	});
    }

    /**
     * 解析json文件转换成json字符串
     *
     * @param jsonFilePath json文件路径
     * @return json字符串
     * @throws IOException mIOException
     */
    public static String readJsonData(String jsonFilePath) throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        File myFile = new File(jsonFilePath);
        if (!myFile.exists()) {
            return null;
        }
        BufferedReader in = null;
        try {
            FileInputStream fis = new FileInputStream(jsonFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            in = new BufferedReader(inputStreamReader);
            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);
            }
            // catch (IOException e) {
            // e.getStackTrace();
            // }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return strbuffer.toString();
    }

    /**
     * 删除指定的目录以及目录下的所有子文件
     *
     * @param dirName is 目录路径
     * @return true or false 成功返回true，失败返回false
     */
    public static boolean deleteDir(String dirName) {
        // dirName不以分隔符结尾则自动添加分隔符
        String newDirName = dirName;
        if (!dirName.endsWith(File.separator)) {
            newDirName += File.separator;
        }
        File file = new File(newDirName);// 根据指定的文件名创建File对象
        if (!file.exists() || (!file.isDirectory())) { // 目录不存在或者
            LOGGER.error("目录删除失败" + newDirName + "目录不存在！");
            return false;
        }
        File[] fileArrays = file.listFiles();// 列出源文件下所有文件，包括子目录
        for (int i = 0; i < fileArrays.length; i++) {// 将源文件下的所有文件逐个删除
            deleteFiles(fileArrays[i].getAbsolutePath());
        }
        if (file.delete()) {
            return true;
        }
        return false;
    }

    /**
     *
     * 将指定位置下的文件打包成zip包，并放到指定位置
     *
     * @param rootPath 文件所在根目录
     * @param fileName zip包名称
     * @param tagrgetPath zip包存放位置
     * @author zhangtao2
     * @time 2020年7月10日 下午3:50:11
     */
    public static void fileCompactToTargetSite(String rootPath, String fileName, String tagrgetPath) {
        File file = new File(rootPath);
        File target = new File(tagrgetPath, fileName + ".zip");
        // 压缩文件
        FileUtils.zipFiles(file, target);
    }

    /**
     * @param file 上传的文件
     * @return 转存的文件
     * @throws RuntimeException RuntimeException
     */
    public static File excelFileImport(MultipartFile file) throws RuntimeException {
        File toFile = null;
        // 上传文件到服务器
        String filePath;
        try {
            filePath = uploadFile(file, "Excel", "excelImport");
            toFile = new File(filePath);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败");
        }
        return toFile;
    }

    /**
     * getInputStream
     *
     * @param fileName fileName
     * @return InputStream
     * @throws IOException IOException
     * @date 2020年12月5日 下午6:05:33
     */
    public static InputStream getInputStream(String fileName) throws IOException {
        Resource[] colRuleGroupResource = objResourcePatternResolver.getResources("classpath:" + fileName);
        if (null == colRuleGroupResource) {
            colRuleGroupResource = objResourcePatternResolver.getResources("classpath*:" + fileName);
        }
        Resource resource = colRuleGroupResource[0];
        return resource.getInputStream();
    }

    /**
     * 导出doc文件
     *
     * @param response
     * @param fullPath
     * @param fileName
     */
    public static void exportDocFile(HttpServletResponse response, String fullPath,String fileName) {

        // 通过文件流读取到文件，再将文件通过response的输出流，返回给页面下载
        File file = new File(fullPath);
        try (InputStream in = new FileInputStream(file);){
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName+".docx", "UTF-8"))));
            ServletOutputStream out = response.getOutputStream();

            int len = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (len = in.read(buffer))) {
                out.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
