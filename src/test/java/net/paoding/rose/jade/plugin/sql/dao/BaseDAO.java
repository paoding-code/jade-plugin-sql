package net.paoding.rose.jade.plugin.sql.dao;

import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.plugin.sql.GenericDAO;

public interface BaseDAO<E, ID> extends GenericDAO<E, ID> {

    @SQL("update {table_name} set status=:2 where id=:1")
    void updateStatus(ID id, int status);
    
    @SQL("select status from {table_name} where id=:1")
    int getStatus(ID id);
}
