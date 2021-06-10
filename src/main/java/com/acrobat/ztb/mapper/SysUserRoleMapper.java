package com.acrobat.ztb.mapper;

import com.acrobat.ztb.model.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    public List<SysUserRole> selectByUserId(@Param("userId") Long userId);
}