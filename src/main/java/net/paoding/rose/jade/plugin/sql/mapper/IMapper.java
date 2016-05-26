/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import net.paoding.rose.jade.statement.StatementMetaData;

/**
 * 
 * {@link IMapper}用以封装提取的一些信息，用于生成SQL、设置参数或解析结果。
 * 
 * {@link IEntityMapper}用以封装实体类的信息（对应DO类）。<p>
 * {@link IOperationMapper}用以封装语句信息（对应DAO的方法）
 * 
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IMapper<O> {

    /**
     * 信息的原始起源地，比如某个DO实例或{@link StatementMetaData}实例等等
     * 
     * @return
     */
    public O getOriginal();

    /**
     * 原始起源地名称（比如类名、字段名等）
     * 
     * @return
     */
    String getOriginalName();

    /**
     * 初始化 （此时 {@link #getOriginal()}和 {@link #getOriginalName()}应该已就绪)
     */
    void init();

    /**
     * 
     * 
     * @return
     */
    String getDestName();

}
