package com.atguigu.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.pojo.Permission;
import com.atguigu.pojo.Role;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户信息,以及用户对应角色，以及角色对应的权限
        User user = userService.findUserByUsername(username);
        if (user == null) {//该用户不存在
            return null; //返回null给框架，框架会进行异常处理
        }
        //2.构建权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<Role> roles = user.getRoles();  //集合数据,由RoleDao帮忙的方法来查询得到,一个用户User会有多个角色Role
        roles.forEach(role -> {
            Set<Permission> permissions = role.getPermissions();  //由permissionDao帮忙查询，一个角色Role会有多种权限Permission
            permissions.forEach(permission -> { //将用户中所有角色的所有权限都记录到权限集合中
                authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
            });
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
