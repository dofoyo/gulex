package com.rhb.gulex.pb.spider;


import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.rhb.gulex.util.HttpDownload;
import com.rhb.gulex.util.ParseString;

@Service("DownloadPBFromCsindex")
public class DownloadPBFromCsindex implements DownloadPB {
	@Value("${dataPath}")
	private String dataPath;
	

	private String getPb(String str) {
		String regexp = "<td>|</td>";
		List<String> list = ParseString.subStrings(str,regexp);
		return list.get(1);
		
	}
	
	private List<String> getTrs(String str){
		String regexp = "<tr>|</tr>";
		List<String> list = ParseString.subStrings(str,regexp);
		return list;
	}
	
	private String getTbody(String str) {
		String regexp = "<tbody.*?>|</tbody>";
		String body = ParseString.subString(str, regexp);
		return body;
	}
	

	@Override
	public PbEntity download(String date) {
		String url = "http://www.csindex.com.cn/zh-CN/downloads/industry-price-earnings-ratio?date="+date+"&type=zy3";
		System.out.println(url);
		String result = HttpDownload.getResult(url);
		result = result.replaceAll("[\\t\\n\\r]", "").replaceAll(" ", "");
		String body = this.getTbody(result);
		
		PbEntity entity = null;
		List<String> trs = this.getTrs(body);
		if(trs.size()>0) {
			entity = new PbEntity();
			entity.setDate(date);
			entity.setShag(getPb(trs.get(0)));
			entity.setSzag(getPb(trs.get(1)));
			entity.setHsag(getPb(trs.get(2)));
			entity.setSszb(getPb(trs.get(3)));
			entity.setZxb(getPb(trs.get(4)));
			entity.setCyb(getPb(trs.get(5)));
			
			System.out.println(entity);			
		}

		
		return entity;
	}



}
