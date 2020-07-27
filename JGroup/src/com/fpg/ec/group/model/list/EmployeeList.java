package com.fpg.ec.group.model.list;

import java.util.Hashtable;

import com.fpg.ec.group.model.Employee;
import com.fpg.ec.utility.db.DataAccessor;
import com.fpg.ec.utility.db.SKDataAccessorList;

/**
 * EmployeeModel List物件(Employee)
 * @author Jason
 */
public class EmployeeList extends SKDataAccessorList {
   
    private static final long serialVersionUID = 7493844310496215597L;
   
    
   
	public DataAccessor genObj(Hashtable i_data){
		return new Employee(i_data);
	}

    public Employee getEmployee(String i_key) {
        return (Employee) super.get(i_key);
    }

    public Employee getEmployee(int i_index) {
        return (Employee) super.get(i_index);
    }	
	
}

