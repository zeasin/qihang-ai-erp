package cn.qihangerp.mapper;

import cn.qihangerp.model.SysMenu;
import cn.qihangerp.model.SysRole;
import cn.qihangerp.model.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT * FROM sys_user WHERE user_name = #{userName} AND del_flag = '0'")
    SysUser selectByUserName(String userName);

    @Select("SELECT r.* FROM sys_role r JOIN sys_user_role ur ON r.role_id = ur.role_id WHERE ur.user_id = #{userId}")
    List<SysRole> selectRolesByUserId(Long userId);

    @Select("SELECT r.role_key FROM sys_role r JOIN sys_user_role ur ON r.role_id = ur.role_id WHERE ur.user_id = #{userId}")
    List<String> selectRoleKeysByUserId(Long userId);

    @Select("SELECT DISTINCT m.perms FROM sys_menu m JOIN sys_role_menu rm ON m.menu_id = rm.menu_id JOIN sys_user_role ur ON rm.role_id = ur.role_id WHERE ur.user_id = #{userId} AND m.perms IS NOT NULL AND m.perms != ''")
    Set<String> selectPermissionsByUserId(Long userId);

    @Select("SELECT m.* FROM sys_menu m JOIN sys_role_menu rm ON m.menu_id = rm.menu_id JOIN sys_user_role ur ON rm.role_id = ur.role_id WHERE ur.user_id = #{userId} AND m.status = '0' ORDER BY m.parent_id, m.order_num")
    List<SysMenu> selectMenusByUserId(Long userId);

    @Update("UPDATE sys_user SET login_date = NOW(), login_ip = #{loginIp} WHERE user_id = #{userId}")
    void updateLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp);
}
