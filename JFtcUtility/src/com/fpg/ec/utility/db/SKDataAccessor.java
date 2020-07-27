package com.fpg.ec.utility.db;

import java.util.Hashtable;

/**
 * SingleKey DataAccessor
 * @author Evan
 *
 */
public abstract class SKDataAccessor extends DataAccessor  implements SingleKey {
	private static final long serialVersionUID = 2812731400191255800L;
	public SKDataAccessor() {
        setData(new Hashtable());
    }
    public SKDataAccessor(Hashtable i_data) {
        setData(i_data);
    }

}
