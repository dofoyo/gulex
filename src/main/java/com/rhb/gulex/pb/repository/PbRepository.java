package com.rhb.gulex.pb.repository;

import java.util.List;

import com.rhb.gulex.pb.spider.PbEntity;

public interface PbRepository {
	public List<PbEntity> getPbEntities();
	public void init();
	public void download();
}
