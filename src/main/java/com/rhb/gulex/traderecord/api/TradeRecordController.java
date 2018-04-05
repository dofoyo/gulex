package com.rhb.gulex.traderecord.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.traderecord.service.TradeRecordService;

@RestController
public class TradeRecordController {
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;
	
	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "trade/dzh/";
	
	
	@GetMapping("/nodzh")
	public ResponseContent<List<TradeRecordDzh>> getBluechipViews(){
		
		List<TradeRecordDzh> TradeRecordDzhs = tradeRecordService.getNoDzh();

		return new ResponseContent<List<TradeRecordDzh>>(ResponseEnum.SUCCESS, TradeRecordDzhs);
	}
	
    @PostMapping("/dzh")
    public ResponseContent<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		
    	String fullname = this.dataPath + this.subPath + file.getOriginalFilename();
    	
    	System.out.println(fullname);
    	
    	File dest = new File(fullname);
    	
    	ResponseContent<String> rs = null;
    	try {
			file.transferTo(dest);
			
			//questionService.addImage(file.getOriginalFilename());
			//questionService.setImageToQuestion(questionid, file.getOriginalFilename(), 0);
			
			rs = new ResponseContent<String>(ResponseEnum.SUCCESS,file.getOriginalFilename());
			
		} catch (IllegalStateException | IOException e) {
			rs = new ResponseContent<String>(ResponseEnum.ERROR,"");
			e.printStackTrace();
		} 
    	
        return rs;
    }

}
