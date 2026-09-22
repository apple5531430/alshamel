package com.alshamel.mall.mapper;

import com.alshamel.mall.dto.BatchView;
import com.alshamel.mall.entity.Batch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BatchMapper {

    @Select("SELECT id, batch_no, product_id, warehouse_id, quantity, created_at FROM batch WHERE id = #{id}")
    Batch findById(@Param("id") Long id);

    /**
     * 乐观锁更新：只有当数量仍是 oldQuantity 时才更新，
     * 防止并发调整导致的数量被覆盖。返回受影响行数。
     */
    @Update("UPDATE batch SET quantity = #{newQuantity} WHERE id = #{id} AND quantity = #{oldQuantity}")
    int updateQuantityIfUnchanged(@Param("id") Long id,
                                  @Param("oldQuantity") Integer oldQuantity,
                                  @Param("newQuantity") Integer newQuantity);

    /** 联表查询所有批次（含产品/仓库简要信息），供前端下拉使用 */
    @Select("SELECT b.id, b.batch_no, b.quantity, " +
            "p.sku AS product_sku, p.name AS product_name, p.unit AS product_unit, " +
            "w.code AS warehouse_code, w.name AS warehouse_name " +
            "FROM batch b " +
            "JOIN product p ON p.id = b.product_id " +
            "JOIN warehouse w ON w.id = b.warehouse_id " +
            "ORDER BY b.id")
    List<BatchView> findAllWithDetails();
}
