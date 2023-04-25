package com.lowcode.modeltool.apimodel.exectool.facade;

import com.gbasedbt.base64.BASE64Decoder;
import com.lowcode.modeltool.apimodel.projectInfo.model.ProjectInfoVO;
import com.lowcode.modeltool.tool.command.Command;
import com.lowcode.modeltool.tool.command.ExecResult;
import com.lowcode.modeltool.tool.command.ModelCommandFactory;
import com.lowcode.modeltool.tool.command.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 */
@Service(value = "modelHandleFacade")
public class ModelHandleFacade {

    @Autowired
    private ModelCommandFactory modelCommandFactory;

    /**
     * 加载驱动测试
     *
     * @param params
     * @return execResult
     */
    @SuppressWarnings("unchecked")
    public ExecResult pingLoadDriver(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(PingLoadDriverClassImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }

    /**
     * 获取数据表清单
     *
     * @param params
     * @return execResult
     */
    public ExecResult reverseGetAllDBTablesList(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(DBReverseGetAllTablesListImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }

    /**
     * 获取指定数据表DDL
     * 数据库逆向，解析表清单的字段以及索引
     * @param params
     * @return execResult
     */
    public ExecResult reverseGetDBTableDDL(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(DBReverseGetTableDDLImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }

    /**
     * 解析PDM文件
     *
     * @param params
     * @return execResult
     */
    public ExecResult parsePDMFile(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(ParsePDMFileImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }

    /**
     * 解析PDM文件
     *
     * @param file
     * @return
     */
    public ExecResult parsePDMMultipartFile(MultipartFile file) {
        ParsePDMFileImpl command = (ParsePDMFileImpl) modelCommandFactory.getCommand(ParsePDMFileImpl.class);
        ExecResult exec = command.parsePDMFile(file);
        return exec;
    }

    /**
     * 解析excel文件
     *
     * @param params
     * @return execResult
     */
    public ExecResult parseExcelFile(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(ParseExcelFileImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }

    /**
     * 解析excel文件
     *
     * @param file
     * @return
     */
    public ExecResult parseExcelMultipartFile(MultipartFile file) {
        ParseExcelFileImpl command = (ParseExcelFileImpl) modelCommandFactory.getCommand(ParseExcelFileImpl.class);
        ExecResult exec = command.parseExcelFile(file);
        return exec;
    }

    /**
     * 解析http请求
     *
     * @param params
     * @return execResult
     */
    public ExecResult httpParser(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(HttpParserImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }


    /**
     * 生成WORD文档
     *
     * @param params
     * @return execResult
     */
    public ExecResult genDocx(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(GenDocxImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }

    /**
     * 创建doc文档
     *
     * @param projectInfoVO
     * @param path
     * @return 文件路径
     */
    public String createDocFile(ProjectInfoVO projectInfoVO, String path) {
        String fullPath = path + File.separator + projectInfoVO.getProjectName() + ".docx";
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource("tpl/siner-docx-tpl.docx");
        String docxTpl = url.getPath();

        File outPutFile = new File(path);
        if (!outPutFile.exists()) {
            outPutFile.mkdirs();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("sinerFile", projectInfoVO.getContent());
        params.put("docxTpl", docxTpl);
        params.put("imgDir", path);
        params.put("imgExt", ".png");
        params.put("outFile", fullPath);
        Command<ExecResult> command = modelCommandFactory.getCommand(GenDocxImpl.class);
        command.exec(params);
        return fullPath;
    }


    /**
     * 创建base64图片
     *
     * @param imgData，path
     * @param path
     * @return 文件路径
     */
    public void createImgFile(List<Map<String, String>> imgData, String path) {
        for (Map<String, String> object : imgData) {
            String fileName = (String) object.get("fileName");
            String base64Str = (String) object.get("data");
            File imgFile = base64StrToFile(base64Str,fileName,path);
        }
    }


    private File base64StrToFile(String base64Str, String fileName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = path + File.separator + fileName + ".png";
        File imgFile = new File(filePath);
        try (FileOutputStream out = new FileOutputStream(imgFile);){
            byte[] bytes1 = new BASE64Decoder().decodeBuffer(base64Str);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes1);

            int len = 0;
            byte[] buffer = new byte[1024];
            while((len = in.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgFile;
    }

    /**
     * 将DDL语句解析为表结构
     *
     * @param params
     * @return execResult
     */
    public ExecResult parseDDLToTable(Map<String, String> params) {
        Command<ExecResult> command = modelCommandFactory.getCommand(ParseDDLToTableImpl.class);
        ExecResult exec = command.exec(params);
        return exec;
    }




}
