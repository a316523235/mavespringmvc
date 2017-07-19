package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/14.
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/index")
    public String index() {
        logger.info("the first jsp page");

        return "index";
    }

    @RequestMapping("/redirect")
    public ModelAndView redirect() throws Exception {
        String url = "https://s.click.taobao.com/lJWRmfw?pid=mm_123638303_26178903_104410021&yingxiao=1&a=uland";
        String newUrl = getRedirectUrl(url);
        String checkUrl = newUrl.replace("coupon/edetail?", "cp/coupon?ctoken=6jJYfP0ScvDwdNGNIYyHiceland&");
        HttpURLConnection conn = (HttpURLConnection) new URL(checkUrl).openConnection();
        conn.setConnectTimeout(3000);
        if(conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
        InputStream is = conn.getInputStream();
        String result = IOUtils.toString(is, "UTF-8");
        conn.disconnect();
        JSONObject jsonObject = JSON.parseObject(result.toString());
        ModelAndView mav = new ModelAndView("redirect");
        mav.addObject("newUrl", newUrl);
        mav.addObject("success", jsonObject.getBoolean("success").toString());
        mav.addObject("retStatus", jsonObject.getJSONObject("result").getInteger("retStatus").toString());
        return mav;
    }

    private String getRedirectUrl(String url) throws Exception {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setConnectTimeout(3000);
        return httpURLConnection.getHeaderField("Location");
    }
}
