package com.ky.logic.controller;

import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.info.ResponseMsg;
import com.ky.logic.model.request.UserRoleRequest;
import com.ky.logic.model.response.UserRoleResponse;
import com.ky.logic.service.IPrivilegeService;
import com.ky.logic.service.IRoleService;
import com.ky.logic.service.IUserService;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yyl
 * @create 2017-07-06 17:24
 * @since 1.0
 **/
@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource(name = "roleService")
    private IRoleService roleService;

    @Resource(name = "privilegeService")
    private IPrivilegeService privilegeService;

    @Resource(name = "userService")
    private IUserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg create(@RequestBody UserRoleRequest roleParam) {
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            UserRoleEntity roleEntity = roleService.getByRole(roleParam.getRole());
            if (null != roleEntity) {
                responseMsg.createFailedResponse(null, "该角色已存在", "添加失败");
            }

            UserRoleEntity role = new UserRoleEntity();
            role.setRoleName(roleParam.getRoleName());
            role.setRole(roleParam.getRole());
            role.setDescription(roleParam.getDescription());
            role.setDefault(false);
            Set<UserPrivilegeEntity> privilegeSet = new HashSet<>();

            String[] arrIds = roleParam.getPrivileges().split(",");
            UserPrivilegeEntity privilege = null;
            for (String id : arrIds) {
                privilege = privilegeService.getPrivilegeById(id);
                if (null != privilege) {
                    privilegeSet.add(privilege);
                }
            }

            role.setPrivilege(privilegeSet);
            String roleId = roleService.createRole(role);
            responseMsg.createSuccessResponse(roleId);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "添加失败", e.getMessage());
        }

        return responseMsg;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg update(@RequestBody UserRoleRequest roleParam) {
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            String roleId = roleService.updateRole(roleParam);
            responseMsg.createSuccessResponse(roleId);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "修改失败", e.getMessage());
        }

        return responseMsg;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg delete(@RequestParam String roleId) {
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            UserRoleEntity role = roleService.query(roleId);
            if (null == role) {
                responseMsg.createFailedResponse(null, "角色不存在", roleId + "角色不存在");
                return responseMsg;
            }

            if (role.isDefault()) {
                responseMsg.createFailedResponse(null, "默认角色不允许删除", role.getRole() + "默认角色不允许删除");
                return responseMsg;
            }

            roleService.deleteRole(roleId);
            responseMsg.createSuccessResponse(role.getId());
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "删除失败", e.getMessage());
        }

        return responseMsg;
    }

    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg queryByPage(@RequestParam(required = false) String roleId,
                                   @RequestParam(required = false) String roleName,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) String createTime,
                                   @RequestParam(required = false) String updateTime,
                                   @RequestParam(required = false) String orderBy,
                                   @RequestParam(required = false) Boolean orderAsc,
                                   @RequestParam(required = false) Integer pageNum,
                                   @RequestParam(required = false) Integer pageSize) {

        ResponseMsg responseMsg = new ResponseMsg();
        try {
            UserRoleEntity request = new UserRoleEntity();
            request.setId(roleId);
            request.setRoleName(roleName);
            request.setDescription(description);
            if (StringUtils.isNotEmpty(createTime)) {
                request.setCreateTime(DateUtil.parse(createTime, DateUtil.YYYY_MM_DD));
            }

            if (StringUtils.isNotEmpty(updateTime)) {
                request.setUpdateTime(DateUtil.parse(updateTime, DateUtil.YYYY_MM_DD));
            }


            Paging paging = new Paging();
            paging.setOrderBy(orderBy);
            paging.setOrderAsc(orderAsc);
            paging.setPageNum(pageNum);
            paging.setPageSize(pageSize);

            Page<UserRoleEntity> page = roleService.queryRolePage(request, paging);

            // 装换
            Page<UserRoleResponse> roleResponsePage = entity2response(page);
            responseMsg.createSuccessResponse(roleResponsePage);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "查询角色信息列表异常", e.getMessage());
        }

        return responseMsg;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg query(@RequestParam String roleId) {
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            UserRoleEntity response = roleService.query(roleId);
            responseMsg.createSuccessResponse(response);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "查询角色详情异常", e.getMessage());
        }
        return responseMsg;
    }


    //后台 --> 前台
    private Page<UserRoleResponse> entity2response(Page<UserRoleEntity> page) {
        Page<UserRoleResponse> response = new Page<>();

        List<UserRoleEntity> roleEntityList = page.getResult();
        if (null == roleEntityList) {
            return response;
        }

        List<UserRoleResponse> roleResponseList = new ArrayList<>();
        for (UserRoleEntity role : roleEntityList) {
            roleResponseList.add(entity2response(role));
        }

        response.setResult(roleResponseList);
        response.setTotalRows(page.getTotalRows());
        response.setTotalPages(page.getTotalPages());
        response.setStartRow(page.getStartRow());
        response.setPageSize(page.getPageSize());

        return response;
    }


    //后台 --> 前台
    private UserRoleResponse entity2response(UserRoleEntity role) {
        UserRoleResponse response = new UserRoleResponse();
        response.setId(role.getId());
        response.setRole(role.getRole());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        response.setPrivilege(role.getPrivilege());
        response.setIsDefault(role.isDefault());
        Long num = userService.queryNumByRoleId(role.getId());
        response.setUserNum(num);
        return response;
    }
}
