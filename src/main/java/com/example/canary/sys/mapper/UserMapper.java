package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.sys.entity.UserPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Repository
public interface UserMapper extends BaseMapper<UserPO> {

    @Update("UPDATE s_sys_user SET is_deleted = #{userId} WHERE id = #{userId}")
    int deleteById(@Param("userId") String userId);
}
