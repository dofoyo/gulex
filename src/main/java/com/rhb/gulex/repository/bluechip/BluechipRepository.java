package com.rhb.gulex.repository.bluechip;

import java.util.Collection;
import java.util.Set;

public interface BluechipRepository {
	public void save(Collection<BluechipEntity> bluechips);
	public Set<BluechipEntity> getBluechips();
	
}
