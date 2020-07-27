package com.fpg.ec.utility.db;

/**
 * SKDataAccessor需設定UniqueKey時implement
 * @author Evan
 *
 */
public interface HasMultiUniqueKey {
	public String getUniqueKey(String iKey, DataAccessor i_obj);
}
