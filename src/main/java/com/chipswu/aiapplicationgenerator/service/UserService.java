package com.chipswu.aiapplicationgenerator.service;

import com.chipswu.aiapplicationgenerator.modal.dto.request.UserQueryRequest;
import com.chipswu.aiapplicationgenerator.modal.entity.User;
import com.chipswu.aiapplicationgenerator.modal.vo.LoginUserVO;
import com.chipswu.aiapplicationgenerator.modal.vo.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 用户服务层接口
 *
 * @author WuJiaJun
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 获取加密后的密码
     *
     * @param userPassword 原密码
     * @return 加密后密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获取脱敏后的已登录用户信息
     *
     * @param user 用户对象
     * @return 用户视图对象
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword);

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    User getCurrentLoginUser();

    /**
     * 退出登录
     *
     * @return 业务操作结果
     */
    boolean userLogout();

    /**
     * 获取用户视图对象
     *
     * @param user 用户对象
     * @return 用户视图对象
     */
    UserVO getUserVO(User user);

    /**
     * 获取用户视图对象列表
     *
     * @param userList 用户对象列表
     * @return 用户视图对象列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 获取用户查询对象
     *
     * @param userQueryRequest 用户查询请求信息
     * @return 用户查询对象
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);
}
