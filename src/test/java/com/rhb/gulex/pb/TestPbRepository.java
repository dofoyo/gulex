package com.rhb.gulex.pb;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.pb.repository.PbRepository;
import com.rhb.gulex.pb.spider.PbEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestPbRepository {
	@Autowired
	@Qualifier("PbRepositoryImp")
	PbRepository pbRepository;
	
	
	//@Test
	public void test() {
		List<PbEntity> list = pbRepository.getPbEntities();
		for(PbEntity entity : list) {
			System.out.println(entity);
		}
	}
	
	@Test
	public void test2() {
		pbRepository.download();
	}
}
