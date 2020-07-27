package com.fpg.ec.utility.db;

/**
 * SKDataAccessor需設定UniqueKey時implement
 * @author Evan
 *
 */
public interface HasUniqueKey {
	public String getUniqueKey(DataAccessor i_obj);
}
