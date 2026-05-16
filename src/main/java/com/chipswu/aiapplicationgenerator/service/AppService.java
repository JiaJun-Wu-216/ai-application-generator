package com.chipswu.aiapplicationgenerator.service;

import com.chipswu.aiapplicationgenerator.modal.dto.app.AppQueryRequest;
import com.chipswu.aiapplicationgenerator.modal.entity.App;
import com.chipswu.aiapplicationgenerator.modal.entity.User;
import com.chipswu.aiapplicationgenerator.modal.vo.AppVO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用服务层接口
 *
 * @author WuJiaJun
 */
public interface AppService extends IService<App> {

    /**
     * 创建应用
     *
     * @param initPrompt 应用初始化提示词
     * @return 新增应用 ID
     */
    Long addApp(String initPrompt);

    /**
     * 更新应用（用户端）
     *
     * @param appId     应用 ID
     * @param appName   应用名称
     * @param loginUser 当前登录用户信息
     */
    void updateApp(Long appId, String appName, User loginUser);

    /**
     * 删除应用（用户端）
     *
     * @param appId     应用 ID
     * @param loginUser 当前登录用户信息
     * @return 业务操作结果
     */
    Boolean deleteApp(Long appId, User loginUser);

    /**
     * 获取当前登陆用户创建的应用列表（分页）【用户端】
     *
     * @param appQueryRequest 应用查询请求信息
     * @param loginUser       当前登录用户信息
     * @return 当前登陆用户创建的应用列表
     */
    Page<AppVO> listMyAppVOByPage(AppQueryRequest appQueryRequest, User loginUser);

    /**
     * 获取当前登陆用户创建的应用列表（分页）【用户端】
     *
     * @param appQueryRequest 应用查询请求信息
     * @return 当前登陆用户创建的应用列表
     */
    Page<AppVO> listGoodAppVOByPage(AppQueryRequest appQueryRequest);

    /**
     * app 转换为 appVO
     *
     * @param app 应用原生信息
     * @return 脱敏后的应用视图信息
     */
    AppVO getAppVO(App app);

    /**
     * app 列表转换为 appVO 列表
     *
     * @param appList 应用原生信息列表
     * @return 脱敏后的应用视图信息列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 根据应用查询请求信息获取应用查询对象
     *
     * @param appQueryRequest 应用查询请求信息
     * @return 应用查询对象
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 通过对话生成应用代码
     *
     * @param appId     应用 ID
     * @param message   用户提示词
     * @param loginUser 当前登录用户
     * @return 生成应用代码
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 应用部署
     *
     * @param appId     应用 ID
     * @param loginUser 当前登录用户
     * @return 可访问的部署地址
     */
    String deployApp(Long appId, User loginUser);
}
