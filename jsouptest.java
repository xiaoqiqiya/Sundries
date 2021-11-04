package com.zq;

import cn.hutool.core.date.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Author: 张奇
 * @Description:
 * @Date: 2021/11/3 23:19
 */
public class jsouptest {
    public static final String host = "http://jwc.ys.sdufe.edu.cn/index/";
    static ArrayList<body> objects = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        extracted("http://jwc.ys.sdufe.edu.cn/index/tzgg.htm");
        System.out.println();
    }


    private static void extracted(String documentUrl) throws IOException {
        Document document = Jsoup.connect(documentUrl).get();
        Elements select = document.select(".Next");
        Elements notice = document.getElementsByClass("notice");
        for (Element li : notice.get(0).getElementsByTag("li")) {
            String title=li.getElementsByTag("a").text();
            
            //// TODO: 2021/11/4    title.contains(sql中通过时间进行排序获取第一个tile)  表示当前是最新的文章不需要进行爬取
            String url=li.getElementsByTag("a").get(0).attr("href");
//            url.indexOf("info")
            url=url.substring(url.indexOf("info"));
            body(url);
            System.out.println(title+"==="+url);
        }
        String nextUri=null;
        if (select.size()==2) {
            Element element = select.get(0);
            nextUri=element.attr("href");
            nextUri=!nextUri.contains("tzgg")?"tzgg/"+nextUri:nextUri;
            extracted(host +nextUri);
        }
    }
    public static String doOcr4(String base64)throws TesseractException   {
        String msg = HttpRequest.post("http://81.68.91.96:9999/api/tr-run/").form("img",base64).execute().body();
        JSONObject data1 = JSONObject.parseObject(msg).getJSONObject("data");
        JSONArray raw_out = data1.getJSONArray("raw_out");

        String result = raw_out.getJSONArray(0).getString(1);
        if (StrUtil.isEmpty(result)) {
            for (int i = 0; i < 5; i++) {
                return doOcr4(base64);
            }
            return "error";
        }else
            return result;

    }

    private static  void body(String bodyUrl) throws IOException {
        String host="http://jwc.ys.sdufe.edu.cn/";
        Document document = Jsoup.connect(host+bodyUrl).get();
        String title = document.getElementById("cphContent_lblTitle").text();
        String time = document.getElementById("cphContent_lblTime").text();
        Element content = document.getElementById("vsb_content");
        String text = content.html();
        body body = new body();
        body.setTitle(title);
        body.setBodyText(text);

        body.setDate(DateUtil.parse(time,"yyyy-MM-dd hh:mm"));
        objects.add(body);
    }
}
class body{
    private String title;
    private Date date;
    private String bodyText;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
}
