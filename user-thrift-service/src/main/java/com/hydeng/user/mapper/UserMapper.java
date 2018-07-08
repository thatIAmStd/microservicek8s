package com.hydeng.user.mapper;

import com.hydeng.thrift.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-24
 */
@Mapper
public interface UserMapper {

    @Select("SELECT id,username,password,real_name as realName,email,mobile from pe_user" +
            " where id = #{id}")
    UserInfo getUserById(@Param("id") int id);

    @Select("SELECT id,username,password,real_name as realName,email,email,mobile from pe_user" +
            " where username = #{username}")
    UserInfo getUserByName(@Param("username") String userName);


    @Insert("insert into pe_user (username,password,real_name,email,mobile) " +
            "values (#{u.username},#{u.password},#{u.realName},#{u.email},#{u.mobile})")
    void registerUser(@Param("u") UserInfo userInfo);

    @Select("select u.id,u.username,u.password,u.real_name as realName,u.email,u.mobile,t.intro,t.description " +
            " from pe_user u,pr_user_course t where u.id = #{id} and u.id = t.user_id")
    UserInfo getTeacherById(@Param("id") int id);
}
