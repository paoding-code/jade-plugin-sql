/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序条件：
 * <p>
 * <code>Plum.asc("name", "age").desc("sex")</code> &gt; ORDER BY name ASC, age ASC, sex DESC
 * </p>
 * @author Alan.Geng[gengzhi718@gmail.com]
 * 
 * @see Plum#orderBy(Direction, String...)
 * @see Plum#asc(String...)
 * @see Plum#desc(String...)
 */
public class Order {

    private List<Group> groups;

    public Order(String[] fields, Direction direction) {
        groups = new ArrayList<Group>(1);
        groups.add(new Group(direction, fields));
    }

    public static enum Direction {

        /**
         * 不排序
         */
        NONE,

        /**
         * 升序
         */
        ASC,

        /**
         * 降序
         */
        DESC;
    }

    public static class Group {

        private Direction direction;

        private String[] fields;

        public Group(Direction direction, String[] fields) {
            super();
            this.direction = direction;
            this.fields = fields;
        }

        public Direction getDirection() {
            return direction;
        }

        public String[] getFields() {
            return fields;
        }
    }

    /**
     * 增加升序排序字段
     * @param fields
     * @return
     */
    public Order asc(String... fields) {
    	checkOrderFields(fields);
        groups.add(new Group(Direction.ASC, fields));
        return this;
    }

    /**
     * 增加降序排序字段
     * @param fields
     * @return
     */
    public Order desc(String... fields) {
    	checkOrderFields(fields);
        groups.add(new Group(Direction.DESC, fields));
        return this;
    }

    /**
     * 增加指定方向的排序字段
     * @param direction
     * @param fields
     * @return
     */
    public Order orderBy(Direction direction, String... fields) {
    	if(direction == null) {
    		throw new IllegalArgumentException("Direction must not be null.");
    	}
    	checkOrderFields(fields);
        groups.add(new Group(direction, fields));
        return this;
    }
    
    private static void checkOrderFields(String[] fields) {
    	if(fields == null) {
    		throw new IllegalArgumentException("Fields must not be null.");
    	} else if(fields.length == 0) {
    		throw new IllegalArgumentException("Fields must not be empty.");
    	}
    }

    public List<Group> getGroups() {
        return groups;
    }

}
