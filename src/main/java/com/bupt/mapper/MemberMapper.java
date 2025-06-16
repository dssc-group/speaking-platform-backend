package com.bupt.mapper;

import com.bupt.pojo.Member;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {
//    @Select("select * from tmxbase_db.t_translation_group_member")
    //分页查询
//    @Select("select * from test_db.t_translation_group_member limit #{start},#{pageSize}")
//    public List<Member> list(@Param("start") Integer start, @Param("pageSize") Integer pageSize);
//    @Select("select count(*) from test_db.t_translation_group_member")
//    public Long count();
//    @Select("select * from test_db.t_translation_group_member")
    public List<Member> list(@Param("stuIndex") String stuIndex, @Param("name") String name,
                             @Param("mail") String mail);
    public Member searchById(Integer userId);
    @Delete("delete from test_db.Users where UserId = #{id}")
    public int delete(Integer id);
    public int updateStuInfo(Member stuInfo);
    public int searchMailExist(String mail);

    public List<Member> getStuByIds(@Param("stuIds") List<Integer> stuIds,@Param("stuIndex") String stuIndex,
                             @Param("name")String name,@Param("mail") String mail);

    public Member getInfoByName(String userName);
}
