package com.chipswu.aiapplicationgenerator.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.chipswu.aiapplicationgenerator.constant.UserConstant;
import com.chipswu.aiapplicationgenerator.exception.BusinessException;
import com.chipswu.aiapplicationgenerator.exception.ErrorCode;
import com.chipswu.aiapplicationgenerator.mapper.UserMapper;
import com.chipswu.aiapplicationgenerator.modal.dto.user.request.UserQueryRequest;
import com.chipswu.aiapplicationgenerator.modal.entity.User;
import com.chipswu.aiapplicationgenerator.modal.enums.AccountStatusEnum;
import com.chipswu.aiapplicationgenerator.modal.enums.UserRoleEnum;
import com.chipswu.aiapplicationgenerator.modal.vo.LoginUserVO;
import com.chipswu.aiapplicationgenerator.modal.vo.UserVO;
import com.chipswu.aiapplicationgenerator.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务层接口实现类
 *
 * @author WuJiaJun
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 ID
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验参数
        if (StrUtil.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度过短");
        }
        if (userPassword.length() < 8 || userPassword.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于8位且不能超过16位");
        }
        if (checkPassword.length() < 8 || checkPassword.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "确认密码长度不能小于8位且不能超过16位");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2.校验用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        boolean exists = this.exists(queryWrapper);
        if (exists) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前账户已存在");
        }
        // 3.加密密码
        String encryptPassword = this.getEncryptPassword(userPassword);
        // 4.创建用户，插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("用户" + RandomUtil.randomNumbers(6));
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "注册失败");
        }
        return user.getId();
    }

    /**
     * 获取加密后的密码
     *
     * @param userPassword 原密码
     * @return 加密后密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "ai-application-generator";
        return DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取脱敏后的已登录用户信息
     *
     * @param user 用户对象
     * @return 用户视图对象
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword) {
        // 1.校验参数
        if (StrUtil.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度过短");
        }
        if (userPassword.length() < 8 || userPassword.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于8位且不能超过16位");
        }
        // 2.加密密码
        String encryptPassword = this.getEncryptPassword(userPassword);
        // 3.查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在或密码错误");
        }
        // 4.如果用户存在，当前账户是否被封禁
        if (AccountStatusEnum.BANED.getValue().equals(user.getAccountStatus())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "当前账户已被封禁，请联系管理员处理");
        }
        // 5.记录用户的登录态
        StpUtil.login(user.getId());
        StpUtil.getSession().set(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    @Override
    public User getCurrentLoginUser() {
        // 1.判断当前用户是否登陆，若未登录抛出未登录异常
        if (StpUtil.getLoginIdDefaultNull() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 2.从当前已登录用户的 Session 中获取用户信息
        Object userObject = StpUtil.getSession().get(UserConstant.USER_LOGIN_STATE);
        User currentLoginUser = (User) userObject;
        // 3.判断用户信息是否为空，若为空抛出未登录异常
        if (currentLoginUser == null || currentLoginUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 4.从数据库中获取用户最新信息（防止 Session 中的信息已被修改）
        Long userId = currentLoginUser.getId();
        currentLoginUser = this.getById(userId);
        if (currentLoginUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 5.返回最新查询到的用户信息
        return currentLoginUser;
    }

    /**
     * 退出登录
     *
     * @return 业务操作结果
     */
    @Override
    public boolean userLogout() {
        Object currentLoginUserId = StpUtil.getLoginIdDefaultNull();
        if (currentLoginUserId == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户未登录");
        }
        StpUtil.logout(currentLoginUserId);
        return true;
    }

    /**
     * 获取用户视图对象
     *
     * @param user 用户对象
     * @return 用户视图对象
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        AccountStatusEnum accountStatusEnum = AccountStatusEnum.getEnumByValue(user.getAccountStatus());
        if (accountStatusEnum != null) {
            userVO.setStatusDescription(accountStatusEnum.getText());
        }
        return userVO;
    }

    /**
     * 获取用户视图对象列表
     *
     * @param userList 用户对象列表
     * @return 用户视图对象列表
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    /**
     * 获取用户查询对象
     *
     * @param userQueryRequest 用户查询请求信息
     * @return 用户查询对象
     */
    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }


}
