package cn.yanf.cn.yanf.wordftl;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/11/18.
 */
public class TestFtl {
    private String filePath; //文件路径
    private String fileName; //文件名称
    private String fileOnlyName; //文件唯一名称

    public String createWord() {
        /** 用于组装word页面需要的数据 */
        Map<String, Object> dataMap = new HashMap<String, Object>();

        /** 组装数据 */
        dataMap.put("userName", "张三");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        dataMap.put("currDate", sdf.format(new Date()));

        dataMap.put("content", "这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容这是其它内容");

        List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "标题" + i);
            map.put("content", "内容" + (i * 2));
            map.put("author", "作者" + (i * 3));
            newsList.add(map);
        }
        dataMap.put("newList", newsList);

        /** 文件名称，唯一字符串 */
        Random r = new Random();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        StringBuffer sb = new StringBuffer();
        sb.append(sdf1.format(new Date()));
        sb.append("_");
        sb.append(r.nextInt(100));

        //文件路径
        filePath = "c:/upload";

        //文件唯一名称
        fileOnlyName = "用freemarker导出的Word文档_" + sb + ".doc";

        //文件名称
        fileName = "用freemarker导出的Word文档.doc";

        /** 生成word */
        WordUtil.createWord(dataMap, "ftltest.ftl", filePath, fileOnlyName);

        return "createWordSuccess";
    }
    public void createWordImage(){
        /** 用于组装word页面需要的数据 */
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("image",getImageStr("C:/Users/Administrator/Desktop/1.JPG"));
        /** 文件名称，唯一字符串 */
        Random r = new Random();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        StringBuffer sb = new StringBuffer();
        sb.append(sdf1.format(new Date()));
        sb.append("_");
        sb.append(r.nextInt(100));

        //文件路径
        filePath = "c:/upload";

        //文件唯一名称
        fileOnlyName = "用freemarker导出的Word文档_" + sb + ".doc";

        //文件名称
        fileName = "用freemarker导出的Word文档.doc";

        /** 生成word */
        WordUtil.createWord(dataMap, "image.ftl", filePath, fileOnlyName);
    }
    private String getImageStr(String imagePath) {
        String imgFile = imagePath;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
    public static void main(String[] args){
        TestFtl testFtl=new TestFtl();
        //System.out.println(testFtl.createWord());
        testFtl.createWordImage();
    }
}
