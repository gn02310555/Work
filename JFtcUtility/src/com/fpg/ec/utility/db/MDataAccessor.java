package com.fpg.ec.utility.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.fpg.ec.utility.ObjectUtil;

/**
 * 多重資料存取者
 * @author Evan
 */
public abstract class MDataAccessor implements Serializable {
	private static final long serialVersionUID = -226140677763492884L;
	protected ArrayList list = new ArrayList();
    protected HashMap map = new HashMap();

    public void clear() {
        this.list = new ArrayList();
        this.map = new HashMap();
    }

    public void set(int i_index, Object i_obj) {
        this.list.set(i_index, i_obj);
    }

    public void add(Object i_obj) {
        if (i_obj instanceof DataAccessor) {
            if (!containsKey(getKey(i_obj))) {
                list.add(i_obj);
                map.put(getKey(i_obj), i_obj);
            }
        } else if (i_obj instanceof Hashtable) {
            if (!containsKey(getKey(i_obj))) {
                DataAccessor daTemp = genObj((Hashtable) i_obj);
                list.add(daTemp);
                map.put(getKey(daTemp), daTemp);
            }
        } else {
            throw new RuntimeException(" add to MDataAccessor object must be DataAccessor or Hashtable " + i_obj.getClass());
        }
    }

    public void add(int i_index, Object i_obj) {
        if (i_obj instanceof DataAccessor) {
            list.add(i_index, i_obj);
            map.put(getKey(i_obj), i_obj);
        } else if (i_obj instanceof Hashtable) {
            DataAccessor daTemp = genObj((Hashtable) i_obj);
            list.add(i_index, daTemp);
            map.put(getKey(daTemp), daTemp);
        } else {
            throw new RuntimeException(" add to MDataAccessor object must be DataAccessor or Hashtable ");
        }
    }

    public int getIndex(Object i_obj) {
        int intReturn = -1;
        for (int i = 0; i < size(); i++) {
            if (equals(i, i_obj)) {
                intReturn = i;
                break;
            }
        }
        if (intReturn < 0)
            throw new RuntimeException("No this object in this MDataAccessor ! obj : " + i_obj);
        return intReturn;
    }

    private boolean equals(int i_index, Object i_obj) {
        return getKey(i_index).equals(getKey(i_obj));
    }

    public Object get(int i_index) {
        Object objReturn = list.get(i_index);
        if (objReturn == null) {
            return genObj(new Hashtable());
        }
        return objReturn;
    }

    public Object get(String i_key) {
        Object objReturn = map.get(i_key);
        if (objReturn == null) {
            return genObj(new Hashtable());
        }
        return objReturn;
    }

    public Object getDeepClone() {
        try {
            return new ObjectUtil().deepCopy(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

    public Object getDeepClone(int i_index) {
        Object objReturn = get(i_index);
        try {
            return new ObjectUtil().deepCopy(objReturn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getDeepClone(String i_key) {
        Object objReturn = get(i_key);
        try {
            return new ObjectUtil().deepCopy(objReturn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(int i_index) {
        DataAccessor daObj = (DataAccessor) list.get(i_index);
        if (daObj != null) {
            map.remove(getKey(daObj));
            list.remove(i_index);
        }
    }

    public String getKey(int i_index) {
        DataAccessor daObj = (DataAccessor) list.get(i_index);
        if (daObj != null) {
            return getKey(daObj);
        }
        return null;
    }

    public void addAll(ArrayList i_list) {
        for (int i = 0; i < i_list.size(); i++) {
            Object obj = i_list.get(i);
            add(obj);
        }
    }

    public int size() {
        return list.size();
    }

    public boolean containsObj(Object i_obj) {
        String strKey = "";
        try {
            strKey = getKey(i_obj);
        } catch (Exception e) {

        }
        return map.containsKey(strKey);
    }

    public boolean containsKey(String i_key) {
        return map.containsKey(i_key.trim());
    }

    public abstract String getKey(Object i_obj);

    public DataAccessor genObj(Hashtable i_obj) {
        throw new RuntimeException("please implement genObj() method in MDataAccessor !");
    }

    public String toString() {
        StringBuffer strbReturn = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            strbReturn.append("\n" + getKey(i) + ":" + get(i));
        }
        return strbReturn.toString() + "\n";
    }

    public ArrayList getArrayListDeepClone() {
        try {
            ArrayList alReturn = (ArrayList) (new ObjectUtil().deepCopy(this.list));
            return alReturn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } 
   
    //#########################
}
