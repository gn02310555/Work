package com.fpg.ec.group.dao;

import java.util.List;

import com.fpg.ec.group.model.Dp;

/**
 * DpDAO 介面(Dp)
 * @author Jason
 */
public interface DpDAO {

	// ### Base Method  ##################################################
	
    public void insert(Dp key);

    public void insertSelective(Dp key);

    public int deleteBySelective(Dp key);

    public List selectListBySelective(Dp key);

    
    public Dp selectByPrimaryKey(String xuid);
   
 	public int updateByPrimaryKey(Dp key);
 
    public int updateByPrimaryKeySelective(Dp key);
    
    public int deleteByPrimaryKey(String xuid);
      
}
