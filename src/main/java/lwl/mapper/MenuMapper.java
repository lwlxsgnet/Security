package lwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lwl.domain.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    // 根据用户id查询权限
    @Select("select distinct perms " +
            "from sys_user_role sur left join sys_role sr on sur.role_id = sr.id " +
            "left join sys_role_menu srm on sur.role_id = srm.role_id " +
            "left join sys_menu sm on srm.menu_id = sm.id " +
            "where user_id = #{userId} and sr.status = 0 and sm.status = 0")
    List<String> selectPermsByUserId (@Param("userId") Long userId);
}
