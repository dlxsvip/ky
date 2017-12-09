package com.ky.logic.security;

import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.service.IUserService;
import com.ky.logic.utils.SpringContextUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yl on 2017/8/2.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private IUserService userService;


    /**
     * 登陆验证时，通过username获取用户的所有权限信息，
     * 并返回User放到spring的全局缓存SecurityContextHolder中，以供授权器使用
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = null;
        if (null == userService) {
            userService = (IUserService) SpringContextUtil.getBean("userService");
        }

        Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
        try {

            UserEntity userEntity = userService.queryByName(username);
            Set<UserPrivilegeEntity> privilegeEntities = userService.getPrivilegesByUserLoginName(username);
            for (UserPrivilegeEntity en : privilegeEntities) {
                auths.add(new SimpleGrantedAuthority(en.getPrivilege()));
            }

            user = new User(username, userEntity.getPassword(), true, true, true, true, auths);
        } catch (Exception e) {
            throw new UsernameNotFoundException("不存在用户");
        }

        return user;
    }
}
