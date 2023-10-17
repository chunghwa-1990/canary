package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.sys.entity.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
// @Mapper
@Repository
public interface UserMapper extends BaseMapper<UserPO> {

    /**
     * delete
     *
     * @param id
     * @return
     */
    @Update("UPDATE sys_user SET is_deleted = id WHERE id = #{id}")
    int deleteById(@Param("id") String id);
}
