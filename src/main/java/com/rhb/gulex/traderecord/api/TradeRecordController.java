package com.rhb.gulex.traderecord.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.bluechip.service.BluechipService;
import com.rhb.gulex.simulation.service.SimulationService;
import com.rhb.gulex.traderecord.service.TradeRecordService;

@RestController
public class TradeRecordController {
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;
	
	@Autowired
	@Qualifier("SimulationServiceImp")
	SimulationService simulationService;
	
	@Autowired
	@Qualifier("BluechipServiceImp")
	BluechipService bluechipService;
	
	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "trade/dzh/";
	
	
	@GetMapping("/downdzh")
	public ResponseEntity<InputStreamResource> dwonDzhs(){
		String marketCode;
		StringBuffer sb = new StringBuffer();
		List<TradeRecordDzh> tradeRecordDzhs = tradeRecordService.getDzhs();
		for(TradeRecordDzh dzh : tradeRecordDzhs) {
			marketCode = dzh.getCode().indexOf("60")==0 ? "SH" : "SZ";
			sb.append(marketCode);
			sb.append(dzh.getCode());
			sb.append(",");
		}
		InputStream  in_nocode = new ByteArrayInputStream(sb.toString().getBytes());   
		
        HttpHeaders headers = new HttpHeaders();  
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");  
        headers.add("Content-Disposition", "attachment;filename=dzh.txt");  
        headers.add("Pragma", "no-cache");  
        headers.add("Expires", "0");  
		
        return ResponseEntity  
                .ok()  
                .headers(headers)  
                .contentLength(sb.length())  
                .contentType(MediaType.parseMediaType("application/octet-stream"))  
                .body(new InputStreamResource(in_nocode));  
		
	}
	
	@GetMapping("/dzh")
	public ResponseContent<List<TradeRecordDzh>> getDzhs(){
		
		List<TradeRecordDzh> tradeRecordDzhs = tradeRecordService.getDzhs();

		return new ResponseContent<List<TradeRecordDzh>>(ResponseEnum.SUCCESS, tradeRecordDzhs);
	}

	
    @PostMapping("/updzh")
    public ResponseContent<String> updzh(@RequestParam("file") MultipartFile[] files) {
    	ResponseContent<String> rs = null;
		
    	for(MultipartFile file : files) {
        	String fullname = this.dataPath + this.subPath + file.getOriginalFilename();
        	
        	System.out.println(fullname);
        	
        	File dest = new File(fullname);
        	
        	try {
    			file.transferTo(dest);
    			
    			//questionService.addImage(file.getOriginalFilename());
    			//questionService.setImageToQuestion(questionid, file.getOriginalFilename(), 0);
    			
    			rs = new ResponseContent<String>(ResponseEnum.SUCCESS,"");
    			
    		} catch (IllegalStateException | IOException e) {
    			rs = new ResponseContent<String>(ResponseEnum.ERROR,"");
    			e.printStackTrace();
    		} 
    		
    	}
    	
        return rs;
    }

    @PostMapping("/refresh")
    public ResponseContent<String> refresh() {
    	ResponseContent<String> rs = null;
		
    	tradeRecordService.refresh();
    	
		bluechipService.init();
		
		simulationService.init();
    	
		rs = new ResponseContent<String>(ResponseEnum.SUCCESS,"");

        return rs;
    }
    
}
