package com.ky.logic.controller;

import com.ky.logic.common.init.PrivilegeConfigs;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.info.ResponseInfo;
import com.ky.logic.service.IPrivilegeService;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yyl
 * @create 2017-07-06 17:24
 * @since 1.0
 */
@Controller
@RequestMapping("/privilege")
public class PrivilegeController {

    @Resource(name = "privilegeService")
    private IPrivilegeService privilegeService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo create(@RequestBody UserPrivilegeEntity privilege) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            UserPrivilegeEntity privilegeEntity = privilegeService.getByPrivilege(privilege.getPrivilege());
            if (null != privilegeEntity) {
                responseInfo.createFailedResponse(null, "此权限已存在", "添加失败");
                return responseInfo;
            }

            String privilegeId = privilegeService.createPrivilege(privilege);
            responseInfo.createSuccessResponse(privilegeId);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "添加失败", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo update(@RequestBody UserPrivilegeEntity privilege) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            String privilegeId = privilegeService.updatePrivilege(privilege);
            responseInfo.createSuccessResponse(privilegeId);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "修改失败", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo delete(@RequestParam String privilegeId) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            privilegeService.deletePrivilege(privilegeId);
            responseInfo.createSuccessResponse(privilegeId);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "删除失败", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    @ResponseBody
    public ResponseInfo queryByPage(@RequestParam(required = false) String privilegeId,
                                    @RequestParam(required = false) String privilegeName,
                                    @RequestParam(required = false) String privilege,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) String createTime,
                                    @RequestParam(required = false) String updateTime,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) Boolean orderAsc,
                                    @RequestParam(required = false) Integer pageNum,
                                    @RequestParam(required = false) Integer pageSize) {

        ResponseInfo responseInfo = new ResponseInfo();
        try {
            UserPrivilegeEntity request = new UserPrivilegeEntity();
            request.setId(privilegeId);
            request.setPrivilege(privilege);
            request.setPrivilegeName(privilegeName);
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

            Page<UserPrivilegeEntity> page = privilegeService.queryPrivilegePage(request, paging);

            responseInfo.createSuccessResponse(page);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "查询权限列表异常", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public ResponseInfo query(@RequestParam String privilegeId) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            UserPrivilegeEntity response = privilegeService.query(privilegeId);
            responseInfo.createSuccessResponse(response);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "查询权限详情异常", e.getMessage());
        }
        return responseInfo;
    }

    @RequestMapping(value = "/queryByConfig", method = RequestMethod.GET)
    @ResponseBody
    public ResponseInfo queryByConfig() {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            List<Map<String, String>> privilegeMap = PrivilegeConfigs.getPrivilegeMap();
            responseInfo.createSuccessResponse(privilegeMap);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "查询权限配置文件异常", e.getMessage());
        }
        return responseInfo;
    }
}
