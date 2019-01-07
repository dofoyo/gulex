package com.rhb.gulex.wx;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.util.HttpClientUtil;
import com.rhb.gulex.util.JsonUtil;

@RestController
public class WXMiniLoginController {
	protected static final Logger logger = LoggerFactory.getLogger(WXMiniLoginController.class);

	@Value("${appid}")
	private String appid;

	@Value("${secret}")
	private String secret;
	
	@PostMapping("/wxLogin")
	public ResponseContent<WXMiniSessionModel> wxLogin(String code) {
		
		//System.out.println("wxlogin - code: " + code);
		logger.info("wxlogin - code: " + code);
		
//		https://api.weixin.qq.com/sns/jscode2session?
//				appid=APPID&
//				secret=SECRET&
//				js_code=JSCODE&
//				grant_type=authorization_code
		
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> param = new HashMap<>();
		param.put("appid", appid);
		param.put("secret", secret);
		param.put("js_code", code);
		param.put("grant_type", "authorization_code");
		
		String wxResult = HttpClientUtil.doGet(url, param);
		//System.out.println(wxResult);
		logger.info("wxResult: " + wxResult);

		
		WXMiniSessionModel model = JsonUtil.jsonToPojo(wxResult, WXMiniSessionModel.class);
		
/*		// 存入session到redis
		redis.set("user-redis-session:" + model.getOpenid(), 
							model.getSession_key(), 
							1000 * 60 * 30);*/
		
		return new ResponseContent<WXMiniSessionModel>(ResponseEnum.SUCCESS,model);
	}
	
	
	@GetMapping("/wx")
	public ResponseContent<String> test(){

		//System.out.println(bluechips.size());
		String str = "It is ok!";
		return new ResponseContent<String>(ResponseEnum.SUCCESS, str);
	}
}
